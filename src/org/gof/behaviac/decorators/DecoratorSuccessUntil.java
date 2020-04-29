package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;

public class DecoratorSuccessUntil extends DecoratorCount {
	public DecoratorSuccessUntil() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorSuccessUntil)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorSuccessUntilTask pTask = new DecoratorSuccessUntilTask();

		return pTask;
	}

	private static class DecoratorSuccessUntilTask extends DecoratorCountTask {
		public DecoratorSuccessUntilTask() {
		}

		@Override
		public void onreset(Agent pAgent) {
			this.m_n = 0;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			// base.onenter(pAgent);

			if (this.m_n == 0) {
				int count = this.GetCount(pAgent);

				if (count == 0) {
					return false;
				}

				this.m_n = count;
			} else {
				Debug.Check(true);
			}

			return true;
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			if (this.m_n > 0) {
				this.m_n--;

				if (this.m_n == 0) {
					return EBTStatus.BT_FAILURE;
				}

				return EBTStatus.BT_SUCCESS;
			}

			if (this.m_n == -1) {
				return EBTStatus.BT_SUCCESS;
			}

			Debug.Check(this.m_n == 0);

			return EBTStatus.BT_FAILURE;
		}
	}
}
