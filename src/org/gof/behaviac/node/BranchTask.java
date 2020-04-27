package org.gof.behaviac.node;

import java.util.Map;

import org.gof.behaviac.Agent;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.IInstantiatedVariable;
import org.gof.behaviac.NodeHandler_t;

public abstract class BranchTask extends BehaviorTask {
	private BehaviorTask m_currentTask;

	protected BranchTask() {
	}

	@Override
	public void Clear() {
		super.Clear();
		this.m_currentTask = null;
	}

	@Override
	protected boolean onenter(Agent pAgent) {
		return true;
	}

	@Override
	protected void onexit(Agent pAgent, EBTStatus status) {
	}

	@Override
	public void onreset(Agent pAgent) {
	}

	private boolean oneventCurrentNode(Agent pAgent, String eventName, Map<Long, IInstantiatedVariable> eventParams) {
		Debug.Check(this.m_currentTask != null);

		if (this.m_currentTask != null) {
			EBTStatus s = this.m_currentTask.GetStatus();

			Debug.Check(s == EBTStatus.BT_RUNNING && this.m_node.HasEvents());

			boolean bGoOn = this.m_currentTask.onevent(pAgent, eventName, eventParams);

			// give the handling back to parents
			if (bGoOn && this.m_currentTask != null) {
				BranchTask parentBranch = this.m_currentTask.GetParent();

				// back track the parents until the branch
				while (parentBranch != null && parentBranch != this) {
					Debug.Check(parentBranch.GetStatus() == EBTStatus.BT_RUNNING);

					bGoOn = parentBranch.onevent(pAgent, eventName, eventParams);

					if (!bGoOn) {
						return false;
					}

					parentBranch = parentBranch.GetParent();
				}
			}

			return bGoOn;
		}

		return false;
	}

	@Override
	public boolean onevent(Agent pAgent, String eventName, Map<Long, IInstantiatedVariable> eventParams) {
		if (this.m_node.HasEvents()) {
			boolean bGoOn = true;

			if (this.m_currentTask != null) {
				bGoOn = this.oneventCurrentNode(pAgent, eventName, eventParams);
			}

			if (bGoOn) {
				bGoOn = super.onevent(pAgent, eventName, eventParams);
			}

			return bGoOn;
		}

		return true;
	}

	private EBTStatus execCurrentTask(Agent pAgent, EBTStatus childStatus) {
		Debug.Check(this.m_currentTask != null && this.m_currentTask.GetStatus() == EBTStatus.BT_RUNNING);

		// this.m_currentTask could be cleared in ::tick, to remember it
		EBTStatus status = this.m_currentTask.exec(pAgent, childStatus);

		// give the handling back to parents
		if (status != EBTStatus.BT_RUNNING) {
			Debug.Check(status == EBTStatus.BT_SUCCESS || status == EBTStatus.BT_FAILURE);
			Debug.Check(this.m_currentTask.m_status == status);

			BranchTask parentBranch = this.m_currentTask.GetParent();

			this.m_currentTask = null;

			// back track the parents until the branch
			while (parentBranch != null) {
				if (parentBranch == this) {
					status = parentBranch.update(pAgent, status);
				} else {
					status = parentBranch.exec(pAgent, status);
				}

				if (status == EBTStatus.BT_RUNNING) {
					return EBTStatus.BT_RUNNING;
				}

				Debug.Check(parentBranch == this || parentBranch.m_status == status);

				if (parentBranch == this) {
					break;
				}

				parentBranch = parentBranch.GetParent();
			}
		}

		return status;
	}

	@Override
	protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
		EBTStatus status = EBTStatus.BT_INVALID;

		if (this.m_currentTask != null) {
			status = this.execCurrentTask(pAgent, childStatus);
			Debug.Check(
					status == EBTStatus.BT_RUNNING || (status != EBTStatus.BT_RUNNING && this.m_currentTask == null));
		} else {
			status = this.update(pAgent, childStatus);
		}

		return status;
	}

	protected EBTStatus resume_branch(Agent pAgent, EBTStatus status) {
		Debug.Check(this.m_currentTask != null);
		Debug.Check(status == EBTStatus.BT_SUCCESS || status == EBTStatus.BT_FAILURE);

		BranchTask parent = null;

		if (this.m_currentTask.GetNode().IsManagingChildrenAsSubTrees()) {
			parent = (BranchTask) this.m_currentTask;
		} else {
			parent = this.m_currentTask.GetParent();
		}

		// clear it as it ends and the next exec might need to set it
		this.m_currentTask = null;

		if (parent != null) {
			EBTStatus s = parent.exec(pAgent, status);

			return s;
		}

		return EBTStatus.BT_INVALID;
	}

	protected abstract void addChild(BehaviorTask pBehavior);

	@Override
	public BehaviorTask GetCurrentTask() {
		return this.m_currentTask;
	}

	@Override
	public void SetCurrentTask(BehaviorTask task) {
		if (task != null) {
			// if the leaf node is running, then the leaf's parent node is also as running,
			// the leaf is set as the tree's current task instead of its parent
			if (this.m_currentTask == null) {
				Debug.Check(this.m_currentTask != this);
				this.m_currentTask = task;
				task.SetHasManagingParent(true);
			}
		} else {
			if (this.m_status != EBTStatus.BT_RUNNING) {
				this.m_currentTask = task;
			}
		}
	}

}
