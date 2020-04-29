package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class DecoratorAlwaysSuccess extends DecoratorNode {

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorAlwaysSuccess)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorAlwaysSuccessTask pTask = new DecoratorAlwaysSuccessTask();

		return pTask;
	}

	private static class DecoratorAlwaysSuccessTask extends DecoratorTask {
		public DecoratorAlwaysSuccessTask() {
		}

		@Override
		protected void addChild(BehaviorTask pBehavior) {
			super.addChild(pBehavior);
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			return EBTStatus.BT_SUCCESS;
		}
	}

}
