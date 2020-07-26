package org.gof.behaviac.actions;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.LeafTask;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IInstanceMember;
import org.gof.behaviac.utils.StringUtils;

/**
 * @author caobihuan/ctemple@21cn.com
 * 结束动作
 */
@RegisterableNode
public class End extends BehaviorNode {
	IInstanceMember m_endStatus = null;
	boolean m_endOutside = false;

	EBTStatus getStatus(Agent self) {
		if (m_endStatus != null)
			return (EBTStatus) m_endStatus.GetValueObject(self);
		return EBTStatus.BT_SUCCESS;
	}

	boolean getEndOutside() {
		return this.m_endOutside;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("EndStatus")) {
				if (StringUtils.IsValidString(p.value)) {
					var index = p.value.indexOf('(');
					if (index < 0) {
						this.m_endStatus = AgentMeta.ParseProperty(p.value);
					} else {
						this.m_endStatus = AgentMeta.ParseMethod(p.value);
					}
				}
			} else if (p.name.equals("EndOutside")) {
				m_endOutside = p.value.equals("true");

			}
		}
	}

	@Override
	protected BehaviorTask createTask() {
		return new EndTask();
	}

	static class EndTask extends LeafTask {

		EBTStatus GetStatus(Agent self) {
			var node = (End) this.GetNode();
			var status = node != null ? node.getStatus(self) : EBTStatus.BT_SUCCESS;
			Debug.Check(status == EBTStatus.BT_SUCCESS || status == EBTStatus.BT_FAILURE);
			return status;
		}

		boolean GetEndOutside() {
			var node = (End) this.GetNode();
			return node != null ? node.getEndOutside() : false;
		}

		@Override
		protected boolean onenter(Agent self) {
			return true;
		}

		@Override
		protected EBTStatus update(Agent self, EBTStatus childStatus) {
			BehaviorTreeTask rootTask = null;
			if (!this.GetEndOutside()) {
				rootTask = this.getRootTask();
			} else if (self != null) {
				rootTask = self.GetCurrentTreeTask();
			}

			if (rootTask != null) {
				rootTask.setEndStatus(this.GetStatus(self));
			}
			return EBTStatus.BT_RUNNING;
		}
	}

}
