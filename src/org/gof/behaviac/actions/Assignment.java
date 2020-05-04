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
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class Assignment extends BehaviorNode {
	protected IInstanceMember m_opl;
	protected IInstanceMember m_opr;
	protected boolean m_bCast = false;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("CastRight")) {
				this.m_bCast = (p.value .equals( "true"));
			} else if (p.name.equals("Opl")) {
				this.m_opl = AgentMeta.ParseProperty(p.value);
			} else if (p.name.equals("Opr")) {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_opr = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_opr = AgentMeta.ParseMethod(p.value);
				}
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Assignment)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		return new AssignmentTask();
	}

	private static class AssignmentTask extends LeafTask {

		@Override
		protected boolean onenter(Agent pAgent) {
			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);
			Debug.Check(this.GetNode() instanceof Assignment);
			Assignment pAssignmentNode = (Assignment) (this.GetNode());

			EBTStatus result = EBTStatus.BT_SUCCESS;

			if (pAssignmentNode.m_opl != null) {
				if (pAssignmentNode.m_bCast) {
					pAssignmentNode.m_opl.SetValueAs(pAgent, pAssignmentNode.m_opr);
				} else {
					pAssignmentNode.m_opl.SetValue(pAgent, pAssignmentNode.m_opr);
				}
			} else {
				result = pAssignmentNode.update_impl(pAgent, childStatus);
			}

			return result;
		}
	}
}