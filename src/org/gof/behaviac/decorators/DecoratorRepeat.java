package org.gof.behaviac.decorators;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.EBTStatus;

public class DecoratorRepeat extends DecoratorCount {
	public DecoratorRepeat() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorRepeat)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorRepeatTask pTask = new DecoratorRepeatTask();

		return pTask;
	}

	private static class DecoratorRepeatTask extends DecoratorCountTask {
		public DecoratorRepeatTask() {
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			Debug.Check(false, "unsurpported");

			return EBTStatus.BT_INVALID;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.m_node instanceof DecoratorNode);
			DecoratorNode node = (DecoratorNode) this.m_node;

			Debug.Check(this.m_n >= 0);
			Debug.Check(this.m_root != null);

			EBTStatus status = EBTStatus.BT_INVALID;

			for (int i = 0; i < this.m_n; ++i) {
				status = this.m_root.exec(pAgent, childStatus);

				if (node.m_bDecorateWhenChildEnds) {
					while (status == EBTStatus.BT_RUNNING) {
						status = super.update(pAgent, childStatus);
					}
				}

				if (status == EBTStatus.BT_FAILURE) {
					return EBTStatus.BT_FAILURE;
				}
			}

			return EBTStatus.BT_SUCCESS;
		}
	}
}
