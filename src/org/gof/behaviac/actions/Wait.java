package org.gof.behaviac.actions;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.LeafTask;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.CInstanceMember;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class Wait extends BehaviorNode {
	protected IInstanceMember m_time;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name .equals( "Time")) {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_time = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_time = AgentMeta.ParseMethod(p.value);
				}
			}
		}
	}

	protected long GetTime(Agent pAgent) {
		long time = 0;
		if (this.m_time != null) {
			if (this.m_time instanceof CInstanceMember) {
				time = ((Number) this.m_time.GetValueObject(pAgent)).longValue();
			}
		}
		return time;
	}

	@Override
	protected BehaviorTask createTask() {
		WaitTask pTask = new WaitTask();

		return pTask;
	}

	private static class WaitTask extends LeafTask {
		private long m_intStart = 0;
		private long m_intTime = 0;

		@Override
		public void copyto(BehaviorTask target) {
			// TODO Auto-generated method stub
			super.copyto(target);

			Debug.Check(target instanceof WaitTask);
			WaitTask ttask = (WaitTask) target;

			ttask.m_intStart = this.m_intStart;
			ttask.m_intTime = this.m_intTime;
		}

		private long GetTime(Agent pAgent) {
			Wait pWaitNode = (Wait) this.GetNode();

			return pWaitNode != null ? pWaitNode.GetTime(pAgent) : 0;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			this.m_intStart = pAgent.GetCurrentTime();
			this.m_intTime = this.GetTime(pAgent);

			return (this.m_intTime >= 0);
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
		}

		@Override
		protected org.gof.behaviac.EBTStatus update(Agent pAgent, org.gof.behaviac.EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			if (pAgent.GetCurrentTime() - this.m_intStart >= this.m_intTime) {
				return EBTStatus.BT_SUCCESS;
			}

			return EBTStatus.BT_RUNNING;
		}
	}

}
