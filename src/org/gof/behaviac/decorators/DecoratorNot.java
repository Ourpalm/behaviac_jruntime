package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.property_t;

public class DecoratorNot extends DecoratorNode {

	public DecoratorNot() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorNot)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	public boolean Evaluate(Agent pAgent) {
		Debug.Check(this.m_children.size() == 1);
		boolean ret = this.m_children.get(0).Evaluate(pAgent);
		return !ret;
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorNotTask pTask = new DecoratorNotTask();

		return pTask;
	}

	private static class DecoratorNotTask extends DecoratorTask {
		public DecoratorNotTask() {
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			if (status == EBTStatus.BT_FAILURE) {
				return EBTStatus.BT_SUCCESS;
			}

			if (status == EBTStatus.BT_SUCCESS) {
				return EBTStatus.BT_FAILURE;
			}

			return status;
		}
	}

}
