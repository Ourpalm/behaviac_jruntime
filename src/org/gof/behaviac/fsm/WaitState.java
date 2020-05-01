package org.gof.behaviac.fsm;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class WaitState extends State {
	protected IInstanceMember m_time;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Time") {
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
			time = ((Number) this.m_time.GetValueObject(pAgent)).longValue();
		}

		return time;
	}

	@Override
	protected BehaviorTask createTask() {
		WaitStateTask pTask = new WaitStateTask();

		return pTask;
	}

	private static class WaitStateTask extends State.StateTask {
		private long m_intStart = 0;
		private long m_intTime = 0;

		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof WaitStateTask);
			WaitStateTask ttask = (WaitStateTask) target;

			ttask.m_intStart = this.m_intStart;
			ttask.m_intTime = this.m_intTime;
		}

		private long GetIntTime(Agent pAgent) {
			WaitState pWaitNode = (WaitState) this.GetNode();

			return pWaitNode != null ? pWaitNode.GetTime(pAgent) : 0;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			this.m_nextStateId = -1;

			this.m_intStart = pAgent.GetCurrentTime();
			this.m_intTime = this.GetIntTime(pAgent);

			return (this.m_intTime >= 0);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);
			Debug.Check(this.m_node instanceof WaitState, "node is not an WaitState");
			WaitState pStateNode = (WaitState) this.m_node;

			if (pAgent.GetCurrentTime() - this.m_intStart >= this.m_intTime) {
				var r = pStateNode.Update(pAgent, this.m_nextStateId);
				this.m_nextStateId = r.value2;
				return EBTStatus.BT_SUCCESS;
			}

			return EBTStatus.BT_RUNNING;
		}
	}
}
