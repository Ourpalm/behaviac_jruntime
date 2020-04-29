package org.gof.behaviac.conditions;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.LeafTask;

public abstract class ConditionBase extends BehaviorNode {
	public ConditionBase() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof ConditionBase)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}
	
	public static class ConditionBaseTask extends LeafTask {
		public ConditionBaseTask() {
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus status) {
			// TODO Auto-generated method stub
			super.onexit(pAgent, status);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			return EBTStatus.BT_SUCCESS;
		}
	}

}
