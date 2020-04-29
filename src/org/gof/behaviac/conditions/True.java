package org.gof.behaviac.conditions;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class True extends ConditionBase {
	public True() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof True)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		TrueTask pTask = new TrueTask();

		return pTask;
	}

	private static class TrueTask extends ConditionBaseTask {
		public TrueTask() {
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			return EBTStatus.BT_SUCCESS;
		}
	}
}
