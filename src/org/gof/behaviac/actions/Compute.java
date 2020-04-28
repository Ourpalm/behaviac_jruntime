package org.gof.behaviac.actions;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.EOperatorType;
import org.gof.behaviac.IInstanceMember;
import org.gof.behaviac.LeafTask;
import org.gof.behaviac.OperationUtils;
import org.gof.behaviac.property_t;

public class Compute extends BehaviorNode {
	protected IInstanceMember m_opl;
	protected IInstanceMember m_opr1;
	protected IInstanceMember m_opr2;
	protected EOperatorType m_operator = EOperatorType.E_INVALID;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Opl") {
				this.m_opl = AgentMeta.ParseProperty(p.value);
			} else if (p.name == "Operator") {
				Debug.Check(p.value == "Add" || p.value == "Sub" || p.value == "Mul" || p.value == "Div");

				this.m_operator = OperationUtils.ParseOperatorType(p.value);
			} else if (p.name == "Opr1") {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_opr1 = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_opr1 = AgentMeta.ParseMethod(p.value);
				}
			} else if (p.name == "Opr2") {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_opr2 = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_opr2 = AgentMeta.ParseMethod(p.value);
				}
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Compute)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		return new ComputeTask();
	}

	private static class ComputeTask extends LeafTask {
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

			EBTStatus result = EBTStatus.BT_SUCCESS;

			Debug.Check(this.GetNode() instanceof Compute);
			Compute pComputeNode = (Compute) (this.GetNode());

			if (pComputeNode.m_opl != null) {
				pComputeNode.m_opl.Compute(pAgent, pComputeNode.m_opr1, pComputeNode.m_opr2, pComputeNode.m_operator);
			} else {
				result = pComputeNode.update_impl(pAgent, childStatus);
			}

			return result;
		}
	}
}
