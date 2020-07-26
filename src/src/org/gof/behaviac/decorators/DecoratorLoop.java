package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;

@RegisterableNode
public class DecoratorLoop extends DecoratorCount {
	protected boolean m_bDoneWithinFrame;

	public DecoratorLoop() {
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("DoneWithinFrame")) {
				if (p.value.equals("true")) {
					this.m_bDoneWithinFrame = true;
				}
			}
		}
	}

	public int Count(Agent pAgent) {
		return super.GetCount(pAgent);
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorLoop)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorLoopTask pTask = new DecoratorLoopTask();

		return pTask;
	}

	private static class DecoratorLoopTask extends DecoratorCountTask {
		public DecoratorLoopTask() {
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			if (this.m_n > 0) {
				this.m_n--;
				if (this.m_n == 0) {
					return EBTStatus.BT_SUCCESS;
				}
				return EBTStatus.BT_RUNNING;
			}
			if (this.m_n == -1) {
				return EBTStatus.BT_RUNNING;
			}
			Debug.Check(this.m_n == 0);
			return EBTStatus.BT_SUCCESS;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.m_node instanceof DecoratorLoop);
			DecoratorLoop node = (DecoratorLoop) this.m_node;

			if (node.m_bDoneWithinFrame) {
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

			return super.update(pAgent, childStatus);
		}

	}
}
