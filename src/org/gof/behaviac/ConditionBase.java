package org.gof.behaviac;

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
}
