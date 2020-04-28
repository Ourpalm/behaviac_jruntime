package org.gof.behaviac.actions;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.SingeChildTask;

public class WaitforSignal extends BehaviorNode {
	public WaitforSignal() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof WaitforSignal)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	public boolean CheckIfSignaled(Agent pAgent) {
		boolean ret = this.EvaluteCustomCondition(pAgent);
		return ret;
	}

	@Override
	protected BehaviorTask createTask() {
		WaitforSignalTask pTask = new WaitforSignalTask();

		return pTask;
	}

	static class WaitforSignalTask extends SingeChildTask {
		public WaitforSignalTask() {
			m_bTriggered = false;
		}

		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof WaitforSignalTask);
			WaitforSignalTask ttask = (WaitforSignalTask) target;

			ttask.m_bTriggered = this.m_bTriggered;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			this.m_bTriggered = false;

			return true;
		}

		@Override
		protected org.gof.behaviac.EBTStatus update(Agent pAgent, org.gof.behaviac.EBTStatus childStatus) {
			if (childStatus != EBTStatus.BT_RUNNING) {
				return childStatus;
			}

			if (!this.m_bTriggered) {
				WaitforSignal node = (WaitforSignal) this.m_node;
				this.m_bTriggered = node.CheckIfSignaled(pAgent);
			}

			if (this.m_bTriggered) {
				if (this.m_root == null) {
					return EBTStatus.BT_SUCCESS;
				}

				EBTStatus status = super.update(pAgent, childStatus);

				return status;
			}

			return EBTStatus.BT_RUNNING;
		}

		private boolean m_bTriggered;
	}

}
