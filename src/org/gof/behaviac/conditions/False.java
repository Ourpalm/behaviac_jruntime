package org.gof.behaviac.conditions;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class False extends ConditionBase {
	public False() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof False)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		FalseTask pTask = new FalseTask();

		return pTask;
	}

	private static class FalseTask extends ConditionBaseTask {
		public FalseTask() {
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			return EBTStatus.BT_FAILURE;
		}
	}

}
