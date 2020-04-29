package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;

public class DecoratorAlwaysRunning extends DecoratorNode {

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorAlwaysRunning)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorAlwaysRunningTask pTask = new DecoratorAlwaysRunningTask();

		return pTask;
	}

	private static class DecoratorAlwaysRunningTask extends DecoratorTask {
		public DecoratorAlwaysRunningTask() {
		}

		@Override
		protected void addChild(BehaviorTask pBehavior) {
			super.addChild(pBehavior);
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			return EBTStatus.BT_RUNNING;
		}
	}

}
