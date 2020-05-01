package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.Workspace;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class DecoratorTime extends DecoratorNode {
	protected IInstanceMember m_time;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
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
		DecoratorTimeTask pTask = new DecoratorTimeTask();

		return pTask;
	}

	private static class DecoratorTimeTask extends DecoratorTask {
		private long m_intStart = 0;
		private long m_intTime = 0;

		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof DecoratorTimeTask);
			DecoratorTimeTask ttask = (DecoratorTimeTask) target;

			ttask.m_intStart = this.m_intStart;
			ttask.m_intTime = this.m_intTime;
		}

		private long GetIntTime(Agent pAgent) {
			Debug.Check(this.GetNode() instanceof DecoratorTime);
			DecoratorTime pNode = (DecoratorTime) (this.GetNode());

			return pNode != null ? pNode.GetTime(pAgent) : 0;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			super.onenter(pAgent);

			this.m_intStart = Workspace.Instance.GetFrameSinceStartup();
			this.m_intTime = this.GetIntTime(pAgent);

			return (this.m_intTime >= 0);

		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			if (Workspace.Instance.GetFrameSinceStartup() - this.m_intStart >= this.m_intTime) {
				return EBTStatus.BT_SUCCESS;
			}
			return EBTStatus.BT_RUNNING;
		}
	}
}
