package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.property_t;

public class DecoratorLog extends DecoratorNode {
	protected String m_message;

	public DecoratorLog() {
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Log") {
				this.m_message = p.value;
			}
		}
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorLogTask pTask = new DecoratorLogTask();

		return pTask;
	}

	private static class DecoratorLogTask extends DecoratorTask {
		public DecoratorLogTask() {
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			Debug.Check(this.GetNode() instanceof DecoratorLog);
			DecoratorLog pDecoratorLogNode = (DecoratorLog) (this.GetNode());
			Debug.Log(String.format("DecoratorLogTask:%s\n", pDecoratorLogNode.m_message));

			return status;
		}
	}

}
