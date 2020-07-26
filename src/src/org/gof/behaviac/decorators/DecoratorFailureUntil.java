package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class DecoratorFailureUntil extends DecoratorCount {
	public DecoratorFailureUntil() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorFailureUntil)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorFailureUntilTask pTask = new DecoratorFailureUntilTask();

		return pTask;
	}

	private static class DecoratorFailureUntilTask extends DecoratorCountTask {
		public DecoratorFailureUntilTask() {
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
					return EBTStatus.BT_SUCCESS;
				}

				return EBTStatus.BT_FAILURE;
			}

			if (this.m_n == -1) {
				return EBTStatus.BT_FAILURE;
			}

			Debug.Check(this.m_n == 0);

			return EBTStatus.BT_SUCCESS;
		}
	}
}
