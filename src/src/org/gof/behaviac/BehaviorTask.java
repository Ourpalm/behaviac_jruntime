package org.gof.behaviac;

import java.util.Map;

import org.gof.behaviac.composites.ReferencedBehavior;
import org.gof.behaviac.members.IInstantiatedVariable;

public abstract class BehaviorTask {
	public EBTStatus m_status;
	protected BehaviorNode m_node;
	protected BranchTask m_parent;
	protected int m_id;
	protected boolean m_bHasManagingParent;

	public static void DestroyTask(BehaviorTask task) {
	}

	public void Init(BehaviorNode node) {
		Debug.Check(node != null);

		this.m_node = node;
		this.m_id = this.m_node.getId();
	}

	public void Clear() {
		this.m_status = EBTStatus.BT_INVALID;
		this.m_parent = null;
		this.m_id = -1;
	}

	public void copyto(BehaviorTask target) {
		target.m_status = this.m_status;
	}

	public BehaviorTreeTask getRootTask() {
		BehaviorTask task = this;

		while (task.m_parent != null) {
			task = task.m_parent;
		}

		Debug.Check(task instanceof BehaviorTreeTask);
		BehaviorTreeTask tree = (BehaviorTreeTask) task;

		return tree;
	}

	public String GetClassNameString() {
		if (this.m_node != null) {
			return this.m_node.getClassName();
		}
		String subBT = "SubBT";
		return subBT;
	}

	public int GetId() {
		return this.m_id;
	}

	public int GetNextStateId() {
		return -1;
	}

	public BehaviorTask GetCurrentTask() {
		return null;
	}

	public EBTStatus exec(Agent pAgent) {
		EBTStatus childStatus = EBTStatus.BT_RUNNING;

		return this.exec(pAgent, childStatus);
	}

	public EBTStatus exec(Agent pAgent, EBTStatus childStatus) {
		boolean bEnterResult = false;

		if (this.m_status == EBTStatus.BT_RUNNING) {
			bEnterResult = true;
		} else {
			// reset it to invalid when it was success/failure
			this.m_status = EBTStatus.BT_INVALID;
			bEnterResult = this.onenter_action(pAgent);
		}

		if (bEnterResult) {
			boolean bValid = this.CheckParentUpdatePreconditions(pAgent);

			if (bValid) {
				this.m_status = this.update_current(pAgent, childStatus);
			} else {
				this.m_status = EBTStatus.BT_FAILURE;

				if (this.GetCurrentTask() != null) {
					this.update_current(pAgent, EBTStatus.BT_FAILURE);
				}
			}

			if (this.m_status != EBTStatus.BT_RUNNING) {
				// clear it

				this.onexit_action(pAgent, this.m_status);

				// this node is possibly ticked by its parent or by the topBranch who records it
				// as currrent node
				// so, we can't here reset the topBranch's current node
			} else {
				BranchTask tree = this.GetTopManageBranchTask();

				if (tree != null) {
					tree.SetCurrentTask(this);
				}
			}
		} else {
			this.m_status = EBTStatus.BT_FAILURE;
		}

		return this.m_status;
	}

	private static final int kMaxParentsCount = 512;

	private boolean CheckParentUpdatePreconditions(Agent pAgent) {
		boolean bValid = true;

		if (this.m_bHasManagingParent) {
			boolean bHasManagingParent = false;
			int parentsCount = 0;
			//TODO::改造为threadlocal或者实例变量
			BehaviorTask[] parents = new BehaviorTask[kMaxParentsCount];

			BranchTask parentBranch = this.GetParent();

			parents[parentsCount++] = this;

			// back track the parents until the managing branch
			while (parentBranch != null) {
				Debug.Check(parentsCount < kMaxParentsCount, "weird tree!");

				parents[parentsCount++] = parentBranch;

				if (parentBranch.GetCurrentTask() == this) {
					// Debug.Check(parentBranch->GetNode()->IsManagingChildrenAsSubTrees());
					bHasManagingParent = true;
					break;
				}

				parentBranch = parentBranch.GetParent();
			}

			if (bHasManagingParent) {
				for (int i = parentsCount - 1; i >= 0; --i) {
					BehaviorTask pb = parents[i];

					bValid = pb.CheckPreconditions(pAgent, true);

					if (!bValid) {
						break;
					}
				}
			}
		} else {
			bValid = this.CheckPreconditions(pAgent, true);
		}

		return bValid;
	}

	private BranchTask GetTopManageBranchTask() {
		BranchTask tree = null;
		BehaviorTask task = this.m_parent;

		while (task != null) {
			if (task instanceof BehaviorTreeTask) {
				// to overwrite the child branch
				tree = (BranchTask) task;
				break;
			} else if (task.m_node.IsManagingChildrenAsSubTrees()) {
				// until it is Parallel/SelectorLoop, it's child is used as tree to store
				// current task
				break;
			} else if (task instanceof BranchTask) {
				// this if must be after BehaviorTreeTask and IsManagingChildrenAsSubTrees
				tree = (BranchTask) task;
			} else {
				Debug.Check(false);
			}

			task = task.m_parent;
		}

		return tree;
	}

	private static boolean end_handler(BehaviorTask node, Agent pAgent, Object user_data) {
		if (node.m_status == EBTStatus.BT_RUNNING || node.m_status == EBTStatus.BT_INVALID) {
			EBTStatus status = (EBTStatus) user_data;

			node.onexit_action(pAgent, status);

			node.m_status = status;

			node.SetCurrentTask(null);
		}

		return true;
	}

	private static boolean abort_handler(BehaviorTask node, Agent pAgent, Object user_data) {
		if (node.m_status == EBTStatus.BT_RUNNING) {
			node.onexit_action(pAgent, EBTStatus.BT_FAILURE);

			node.m_status = EBTStatus.BT_FAILURE;

			node.SetCurrentTask(null);
		}

		return true;
	}

	private static boolean reset_handler(BehaviorTask node, Agent pAgent, Object user_data) {
		node.m_status = EBTStatus.BT_INVALID;

		node.onreset(pAgent);
		node.SetCurrentTask(null);

		return true;
	}

	protected static NodeHandler_t end_handler_ = BehaviorTask::end_handler;
	protected static NodeHandler_t abort_handler_ = BehaviorTask::abort_handler;
	protected static NodeHandler_t reset_handler_ = BehaviorTask::reset_handler;

	public void abort(Agent pAgent) {
		this.traverse(true, abort_handler_, pAgent, null);
	}

	/// reset the status to invalid
	public void reset(Agent pAgent) {
		// BEHAVIAC_PROFILE("BehaviorTask.reset");

		this.traverse(true, reset_handler_, pAgent, null);
	}

	public EBTStatus GetStatus() {
		return this.m_status;
	}

	public void SetStatus(EBTStatus s) {
		this.m_status = s;
	}

	public BehaviorNode GetNode() {
		return this.m_node;
	}

	public void SetParent(BranchTask parent) {
		this.m_parent = parent;
	}

	public BranchTask GetParent() {
		return this.m_parent;
	}

	public abstract void traverse(boolean childFirst, NodeHandler_t handler, Agent pAgent, Object user_data);

	public boolean CheckEvents(String eventName, Agent pAgent, Map<Long, IInstantiatedVariable> eventParams) {
		return this.m_node.CheckEvents(eventName, pAgent, eventParams);
	}

	public void onreset(Agent pAgent) {
	}

	/**
	 * return false if the event handling needs to be stopped return true, the event
	 * hanlding will be checked furtherly
	 */

	public boolean onevent(Agent pAgent, String eventName, Map<Long, IInstantiatedVariable> eventParams) {
		if (this.m_status == EBTStatus.BT_RUNNING && this.m_node.HasEvents()) {
			if (!this.CheckEvents(eventName, pAgent, eventParams)) {
				return false;
			}
		}

		return true;
	}

	protected BehaviorTask() {
		m_status = EBTStatus.BT_INVALID;
		m_node = null;
		m_parent = null;
		m_bHasManagingParent = false;
	}

	protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
		EBTStatus s = this.update(pAgent, childStatus);

		return s;
	}

	protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
		return EBTStatus.BT_SUCCESS;
	}

	protected boolean onenter(Agent pAgent) {
		return true;
	}

	protected void onexit(Agent pAgent, EBTStatus status) {
	}

	public static String GetTickInfo(Agent pAgent, BehaviorTask bt, String action) {
		String result = GetTickInfo(pAgent, bt.GetNode(), action);

		return result;
	}

	public static String GetTickInfo(Agent pAgent, BehaviorNode n, String action) {
		return "";
	}

	public static String GetParentTreeName(Agent pAgent, BehaviorNode n) {
		String btName = null;

		if (n instanceof ReferencedBehavior) {
			n = n.getParent();
		}

		boolean bIsTree = false;
		boolean bIsRefTree = false;

		while (n != null) {
			bIsTree = (n instanceof BehaviorTree);
			bIsRefTree = (n instanceof ReferencedBehavior);

			if (bIsTree || bIsRefTree) {
				break;
			}

			n = n.getParent();
		}

		if (bIsTree) {
			BehaviorTree bt = (BehaviorTree) n;
			btName = bt.GetName();
		} else if (bIsRefTree) {
			ReferencedBehavior refTree = (ReferencedBehavior) n;
			btName = refTree.GetReferencedTree(pAgent);
		} else {
			Debug.Check(false);
		}

		return btName;
	}

	protected boolean CheckPreconditions(Agent pAgent, boolean bIsAlive) {
		boolean bResult = true;

		if (this.m_node != null) {
			if (this.m_node.getPreconditionsCount() > 0) {
				bResult = this.m_node.CheckPreconditions(pAgent, bIsAlive);
			}
		}

		return bResult;
	}

	public boolean onenter_action(Agent pAgent) {
		boolean bResult = this.CheckPreconditions(pAgent, false);

		if (bResult) {
			this.m_bHasManagingParent = false;
			this.SetCurrentTask(null);

			bResult = this.onenter(pAgent);

			if (!bResult) {
				return false;
			} else {
			}
		}

		return bResult;
	}

	public void onexit_action(Agent pAgent, EBTStatus status) {
		this.onexit(pAgent, status);

		if (this.m_node != null) {
			Effector.EPhase phase = Effector.EPhase.E_SUCCESS;

			if (status == EBTStatus.BT_FAILURE) {
				phase = Effector.EPhase.E_FAILURE;
			} else {
				Debug.Check(status == EBTStatus.BT_SUCCESS);
			}

			this.m_node.ApplyEffects(pAgent, phase);
		}

	}

	public void SetHasManagingParent(boolean bHasManagingParent) {
		this.m_bHasManagingParent = bHasManagingParent;
	}

	public void SetCurrentTask(BehaviorTask task) {
	}
}
