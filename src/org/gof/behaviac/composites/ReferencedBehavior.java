package org.gof.behaviac.composites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.BehaviorTree;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.CInstanceMember;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.IInstanceMember;
import org.gof.behaviac.IInstantiatedVariable;
import org.gof.behaviac.IMethod;
import org.gof.behaviac.SingeChildTask;
import org.gof.behaviac.Task;
import org.gof.behaviac.Utils;
import org.gof.behaviac.Workspace;
import org.gof.behaviac.property_t;
import org.gof.behaviac.fsm.Transition;
import org.gof.behaviac.fsm.State;

public class ReferencedBehavior extends BehaviorNode {
	protected List<Transition> m_transitions;
	protected IInstanceMember m_referencedBehaviorPath;
	protected IMethod m_taskMethod;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "ReferenceBehavior") {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_referencedBehaviorPath = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_referencedBehaviorPath = AgentMeta.ParseMethod(p.value);
				}

				String szTreePath = this.GetReferencedTree(null);

				// conservatively make it true
				boolean bHasEvents = true;

				if (!Utils.isNullOrEmpty(szTreePath)) {
//                    if (Config.PreloadBehaviors)
//                    {
					BehaviorTree behaviorTree = Workspace.Instance.LoadBehaviorTree(szTreePath);
					Debug.Check(behaviorTree != null);

					if (behaviorTree != null) {
						bHasEvents = behaviorTree.HasEvents();
					}
//                    }

					this.m_bHasEvents |= bHasEvents;
				}
			} else if (p.name == "Task") {
				this.m_taskMethod = AgentMeta.ParseMethod(p.value);
			}
		}
	}

	@Override
	public void Attach(BehaviorNode pAttachment, boolean bIsPrecondition, boolean bIsEffector, boolean bIsTransition) {
		if (bIsTransition) {
			Debug.Check(!bIsEffector && !bIsPrecondition);

			if (this.m_transitions == null) {
				this.m_transitions = new ArrayList<Transition>();
			}

			Transition pTransition = (Transition) pAttachment;
			Debug.Check(pTransition != null);
			this.m_transitions.add(pTransition);

			return;
		}

		Debug.Check(bIsTransition == false);
		super.Attach(pAttachment, bIsPrecondition, bIsEffector, bIsTransition);
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof ReferencedBehavior)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		ReferencedBehaviorTask pTask = new ReferencedBehaviorTask();

		return pTask;
	}

	public String GetReferencedTree(Agent pAgent) {
		if (this.m_referencedBehaviorPath != null) {
			Debug.Check(this.m_referencedBehaviorPath instanceof CInstanceMember);
			return (String) this.m_referencedBehaviorPath.GetValueObject(pAgent);
		}

		return "";
	}

	public void SetTaskParams(Agent agent, BehaviorTreeTask treeTask) {
		if (this.m_taskMethod != null) {
			this.m_taskMethod.SetTaskParams(agent, treeTask);
		}
	}

	private Task m_taskNode;

	public Task RootTaskNode(Agent pAgent) {
		if (this.m_taskNode == null) {
			String szTreePath = this.GetReferencedTree(pAgent);
			BehaviorTree bt = Workspace.Instance.LoadBehaviorTree(szTreePath);

			if (bt != null && bt.GetChildrenCount() == 1) {
				BehaviorNode root = bt.GetChild(0);
				this.m_taskNode = (Task) root;
			}
		}

		return this.m_taskNode;
	}

	public class ReferencedBehaviorTask extends SingeChildTask {
		@Override

		protected boolean CheckPreconditions(Agent pAgent, boolean bIsAlive) {
			boolean bOk = super.CheckPreconditions(pAgent, bIsAlive);
			return bOk;
		}

		int m_nextStateId = -1;

		@Override
		public int GetNextStateId() {
			return m_nextStateId;
		}

		BehaviorTreeTask m_subTree = null;

		@Override
		public boolean onevent(Agent pAgent, String eventName, Map<Long, IInstantiatedVariable> eventPrams) {
			if (this.m_status == EBTStatus.BT_RUNNING && this.m_node.HasEvents()) {
				Debug.Check(this.m_subTree != null);

				if (!this.m_subTree.onevent(pAgent, eventName, eventPrams)) {
					return false;
				}
			}

			return true;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			ReferencedBehavior pNode = (ReferencedBehavior) this.GetNode();
			Debug.Check(pNode != null);

			if (pNode != null) {
				this.m_nextStateId = -1;

				String szTreePath = pNode.GetReferencedTree(pAgent);

				// to create the task on demand
				if (this.m_subTree == null || szTreePath != this.m_subTree.GetName()) {
					if (this.m_subTree != null) {
						Workspace.Instance.DestroyBehaviorTreeTask(this.m_subTree, pAgent);
					}

					this.m_subTree = Workspace.Instance.CreateBehaviorTreeTask(szTreePath);
					pNode.SetTaskParams(pAgent, this.m_subTree);
				} else if (this.m_subTree != null) {
					this.m_subTree.reset(pAgent);
				}

				pNode.SetTaskParams(pAgent, this.m_subTree);

				return true;
			}

			return false;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			ReferencedBehavior pNode = (ReferencedBehavior) this.GetNode();
			Debug.Check(pNode != null);

			if (pNode != null) {

				EBTStatus result = this.m_subTree.exec(pAgent);

				var r = State.UpdateTransitions(pAgent, pNode, pNode.m_transitions, this.m_nextStateId, result);
				boolean bTransitioned = r.value1;
				this.m_nextStateId = r.value2;

				if (bTransitioned) {
					if (result == EBTStatus.BT_RUNNING) {
						// subtree not exited, but it will transition to other states
						this.m_subTree.abort(pAgent);
					}

					result = EBTStatus.BT_SUCCESS;
				}

				return result;
			}

			return EBTStatus.BT_INVALID;
		}
	}

}
