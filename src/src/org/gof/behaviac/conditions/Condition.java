package org.gof.behaviac.conditions;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.EOperatorType;
import org.gof.behaviac.OperationUtils;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class Condition extends ConditionBase {
	protected IInstanceMember m_opl;
	protected IInstanceMember m_opr;
	protected EOperatorType m_operator = EOperatorType.E_EQUAL;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("Opl")) {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_opl = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_opl = AgentMeta.ParseMethod(p.value);
				}
			} else if (p.name.equals("Operator")) {
				this.m_operator = OperationUtils.ParseOperatorType(p.value);
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
		if (!(pTask.GetNode() instanceof Condition)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	public boolean Evaluate(Agent pAgent) {
		if (this.m_opl != null && this.m_opr != null) {
			return this.m_opl.Compare(pAgent, this.m_opr, this.m_operator);
		} else {
			EBTStatus childStatus = EBTStatus.BT_INVALID;
			EBTStatus result = this.update_impl(pAgent, childStatus);
			return result == EBTStatus.BT_SUCCESS;
		}
	}

	@Override
	protected BehaviorTask createTask() {
		ConditionTask pTask = new ConditionTask();

		return pTask;
	}

	private static class ConditionTask extends ConditionBaseTask {
		@Override
		protected boolean onenter(Agent pAgent) {
			return true;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			Debug.Check(this.GetNode() instanceof Condition);
			Condition pConditionNode = (Condition) (this.GetNode());

			boolean ret = pConditionNode.Evaluate(pAgent);

			return ret ? EBTStatus.BT_SUCCESS : EBTStatus.BT_FAILURE;
		}
	}

}
