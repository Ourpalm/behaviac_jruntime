package org.gof.behaviac.composites;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;

public class IfElse extends BehaviorNode {
	public IfElse() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof IfElse)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		IfElseTask pTask = new IfElseTask();

		return pTask;
	}

	private static class IfElseTask extends CompositeTask {
		public IfElseTask() {
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			// reset it as it will be checked for the condition execution at the first time
			this.m_activeChildIndex = CompositeTask.InvalidChildIndex;

			if (this.m_children.size() == 3) {
				return true;
			}

			Debug.Check(false, "IfElseTask has to have three children: condition, if, else");

			return false;
		}

		@Override
		protected org.gof.behaviac.EBTStatus update(Agent pAgent, org.gof.behaviac.EBTStatus childStatus) {
			Debug.Check(childStatus != EBTStatus.BT_INVALID);
			Debug.Check(this.m_children.size() == 3);

			EBTStatus conditionResult = EBTStatus.BT_INVALID;

			if (childStatus == EBTStatus.BT_SUCCESS || childStatus == EBTStatus.BT_FAILURE) {
				// if the condition returned running then ended with childStatus
				conditionResult = childStatus;
			}

			if (this.m_activeChildIndex == CompositeTask.InvalidChildIndex) {
				BehaviorTask pCondition = this.m_children.get(0);

				if (conditionResult == EBTStatus.BT_INVALID) {
					// condition has not been checked
					conditionResult = pCondition.exec(pAgent);
				}

				if (conditionResult == EBTStatus.BT_SUCCESS) {
					// if
					this.m_activeChildIndex = 1;
				} else if (conditionResult == EBTStatus.BT_FAILURE) {
					// else
					this.m_activeChildIndex = 2;
				}
			} else {
				return childStatus;
			}

			if (this.m_activeChildIndex != CompositeTask.InvalidChildIndex) {
				BehaviorTask pBehavior = this.m_children.get(this.m_activeChildIndex);
				EBTStatus s = pBehavior.exec(pAgent);

				return s;
			}

			return EBTStatus.BT_RUNNING;
		}
	}

}
