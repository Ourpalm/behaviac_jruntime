package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class DecoratorCountLimit extends DecoratorCount {
	public DecoratorCountLimit() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorCountLimit)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorCountLimitTask pTask = new DecoratorCountLimitTask();

		return pTask;
	}

	public boolean CheckIfReInit(Agent pAgent) {
		boolean bTriggered = this.EvaluteCustomCondition(pAgent);

		return bTriggered;
	}

	private static class DecoratorCountLimitTask extends DecoratorCountTask {
		private boolean m_bInited;

		public DecoratorCountLimitTask() {
		}

		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof DecoratorCountLimitTask);
			DecoratorCountLimitTask ttask = (DecoratorCountLimitTask) target;

			ttask.m_bInited = this.m_bInited;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			DecoratorCountLimit node = (DecoratorCountLimit) this.m_node;

			if (node.CheckIfReInit(pAgent)) {
				this.m_bInited = false;
			}

			if (!this.m_bInited) {
				this.m_bInited = true;

				int count = this.GetCount(pAgent);

				this.m_n = count;
			}

			// if this.m_n is -1, it is endless
			if (this.m_n > 0) {
				this.m_n--;
				return true;
			} else if (this.m_n == 0) {
				return false;
			} else if (this.m_n == -1) {
				return true;
			}

			Debug.Check(false);

			return false;
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			Debug.Check(this.m_n >= 0 || this.m_n == -1);
			return status;
		}
	}
}
