package org.gof.behaviac.composites;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.composites.WithPrecondition.WithPreconditionTask;

@RegisterableNode
public class SelectorLoop extends BehaviorNode {
	protected boolean m_bResetChildren = false;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("ResetChildren")) {
				this.m_bResetChildren = (p.value.equals("true"));
				break;
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof SelectorLoop)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	public boolean IsManagingChildrenAsSubTrees() {
		return true;
	}

	@Override
	protected BehaviorTask createTask() {
		SelectorLoopTask pTask = new SelectorLoopTask();

		return pTask;
	}

	public static class SelectorLoopTask extends CompositeTask {
		@Override
		protected void addChild(BehaviorTask pBehavior) {
			super.addChild(pBehavior);

			Debug.Check(pBehavior instanceof WithPreconditionTask);
		}

		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof SelectorLoopTask);
			SelectorLoopTask ttask = (SelectorLoopTask) target;

			ttask.m_activeChildIndex = this.m_activeChildIndex;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			// reset the action child as it will be checked in the update
			this.m_activeChildIndex = CompositeTask.InvalidChildIndex;
			Debug.Check(this.m_activeChildIndex == CompositeTask.InvalidChildIndex);

			return super.onenter(pAgent);
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			super.onexit(pAgent, s);
		}

		// no current task, as it needs to update every child for every update
		@Override
		protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
			EBTStatus s = this.update(pAgent, childStatus);

			return s;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			int idx = -1;

			if (childStatus != EBTStatus.BT_RUNNING) {
				Debug.Check(this.m_activeChildIndex != CompositeTask.InvalidChildIndex);

				if (childStatus == EBTStatus.BT_SUCCESS) {
					return EBTStatus.BT_SUCCESS;
				} else if (childStatus == EBTStatus.BT_FAILURE) {
					// the next for starts from (idx + 1), so that it starts from next one after
					// this failed one
					idx = this.m_activeChildIndex;
				} else {
					Debug.Check(false);
				}
			}

			// checking the preconditions and take the first action tree
			int index = (int) -1;

			for (int i = (idx + 1); i < this.m_children.size(); ++i) {
				Debug.Check(this.m_children.get(i) instanceof WithPreconditionTask);
				WithPreconditionTask pSubTree = (WithPreconditionTask) this.m_children.get(i);

				BehaviorTask pre = pSubTree.GetPreconditionNode();

				EBTStatus status = pre.exec(pAgent);

				if (status == EBTStatus.BT_SUCCESS) {
					index = i;
					break;
				}
			}

			// clean up the current ticking action tree
			if (index != (int) -1) {
				if (this.m_activeChildIndex != CompositeTask.InvalidChildIndex) {
					boolean abortChild = (this.m_activeChildIndex != index);
					if (!abortChild) {
						SelectorLoop pSelectorLoop = (SelectorLoop) (this.GetNode());
						Debug.Check(pSelectorLoop != null);

						if (pSelectorLoop != null) {
							abortChild = pSelectorLoop.m_bResetChildren;
						}
					}

					if (abortChild) {
						WithPreconditionTask pCurrentSubTree = (WithPreconditionTask) this.m_children
								.get(this.m_activeChildIndex);
						// BehaviorTask action = pCurrentSubTree.ActionNode;
						pCurrentSubTree.abort(pAgent);

						// don't set it here
						// this.m_activeChildIndex = index;
					}
				}

				for (int i = index; i < this.m_children.size(); ++i) {
					WithPreconditionTask pSubTree = (WithPreconditionTask) this.m_children.get(i);

					if (i > index) {
						BehaviorTask pre = pSubTree.GetPreconditionNode();
						EBTStatus status = pre.exec(pAgent);

						// to search for the first one whose precondition is success
						if (status != EBTStatus.BT_SUCCESS) {
							continue;
						}
					}

					BehaviorTask action = pSubTree.GetActionNode();
					EBTStatus s = action.exec(pAgent);

					if (s == EBTStatus.BT_RUNNING) {
						this.m_activeChildIndex = i;
						pSubTree.m_status = EBTStatus.BT_RUNNING;
					} else {
						pSubTree.m_status = s;

						if (s == EBTStatus.BT_FAILURE) {
							// THE ACTION failed, to try the next one
							continue;
						}
					}

					Debug.Check(s == EBTStatus.BT_RUNNING || s == EBTStatus.BT_SUCCESS);

					return s;
				}
			}

			return EBTStatus.BT_FAILURE;
		}
	}
}
