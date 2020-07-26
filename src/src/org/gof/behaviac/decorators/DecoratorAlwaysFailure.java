package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class DecoratorAlwaysFailure extends DecoratorNode {
	public DecoratorAlwaysFailure() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorAlwaysFailure)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorAlwaysFailureTask pTask = new DecoratorAlwaysFailureTask();

		return pTask;
	}

	private static class DecoratorAlwaysFailureTask extends DecoratorTask {
		public DecoratorAlwaysFailureTask() {
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			return EBTStatus.BT_FAILURE;
		}
	}

}
