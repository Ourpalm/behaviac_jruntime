package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.property_t;

public class DecoratorLoopUntil extends DecoratorCount {
	protected boolean m_until;

	public DecoratorLoopUntil() {
		m_until = true;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Until") {
				if (p.value == "true") {
					this.m_until = true;
				} else if (p.value == "false") {
					this.m_until = false;
				}
			}
		}
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorLoopUntilTask pTask = new DecoratorLoopUntilTask();

		return pTask;
	}

	private static class DecoratorLoopUntilTask extends DecoratorCountTask {
		public DecoratorLoopUntilTask() {
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			if (this.m_n > 0) {
				this.m_n--;
			}

			if (this.m_n == 0) {
				return EBTStatus.BT_SUCCESS;
			}

			Debug.Check(this.GetNode() instanceof DecoratorLoopUntil);
			DecoratorLoopUntil pDecoratorLoopUntil = (DecoratorLoopUntil) (this.GetNode());

			if (pDecoratorLoopUntil.m_until) {
				if (status == EBTStatus.BT_SUCCESS) {
					return EBTStatus.BT_SUCCESS;
				}
			} else {
				if (status == EBTStatus.BT_FAILURE) {
					return EBTStatus.BT_FAILURE;
				}
			}

			return EBTStatus.BT_RUNNING;
		}
	}
}
