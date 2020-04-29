package org.gof.behaviac.fsm;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class WaitTransition extends Transition {
	public WaitTransition() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof WaitTransition)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		Debug.Check(false);
		return null;
	}

	@Override
	public boolean Evaluate(Agent pAgent, EBTStatus status) {
		return true;
	}
}
