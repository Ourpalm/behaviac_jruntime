package org.gof.behaviac.node;

import org.gof.behaviac.Agent;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;

public class ConditionBaseTask extends LeafTask {
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
