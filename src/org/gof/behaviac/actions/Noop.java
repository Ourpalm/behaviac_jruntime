package org.gof.behaviac.actions;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.LeafTask;
import org.gof.behaviac.property_t;

public class Noop extends BehaviorNode {
	public Noop() {
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Noop)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		NoopTask pTask = new NoopTask();

		return pTask;
	}

	private static class NoopTask extends LeafTask {

		@Override
		protected boolean onenter(Agent pAgent) {
			return true;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			return EBTStatus.BT_SUCCESS;
		}
	}
}
