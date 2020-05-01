package org.gof.behaviac;

import java.util.List;

import org.gof.behaviac.members.IInstanceMember;
import org.gof.behaviac.members.IMethod;
import org.gof.behaviac.utils.StringUtils;

public class AttachAction extends BehaviorNode {

	public static class ActionConfig {
		protected IInstanceMember m_opl;
		protected IInstanceMember m_opr1;
		protected IInstanceMember m_opr2;
		public EOperatorType m_operator = EOperatorType.E_INVALID;

		protected ActionConfig() {
		}

		public boolean load(List<property_t> properties) {
			for (int i = 0; i < properties.size(); ++i) {
				property_t p = properties.get(i);

				if (p.name == "Opl") {
					if (StringUtils.IsValidString(p.value)) {
						int pParenthesis = p.value.indexOf('(');

						if (pParenthesis == -1) {
							this.m_opl = AgentMeta.ParseProperty(p.value);
						} else {
							this.m_opl = AgentMeta.ParseMethod(p.value);
						}
					}
				} else if (p.name == "Opr1") {
					if (StringUtils.IsValidString(p.value)) {
						int pParenthesis = p.value.indexOf('(');

						if (pParenthesis == -1) {
							this.m_opr1 = AgentMeta.ParseProperty(p.value);
						} else {
							this.m_opr1 = AgentMeta.ParseMethod(p.value);
						}
					}
				} else if (p.name == "Operator") {					
					this.m_operator = OperationUtils.ParseOperatorType(p.value);
				} else if (p.name == "Opr2") {
					if (StringUtils.IsValidString(p.value)) {
						int pParenthesis = p.value.indexOf('(');

						if (pParenthesis == -1) {
							this.m_opr2 = AgentMeta.ParseProperty(p.value);
						} else {
							this.m_opr2 = AgentMeta.ParseMethod(p.value);
						}
					}
				}
			}

			return this.m_opl != null;
		}

		public boolean Execute(Agent pAgent) {
			boolean bValid = false;

			// action
			if (this.m_operator == EOperatorType.E_INVALID) {
				if (this.m_opl != null) {
					Debug.Check(this.m_opl instanceof IMethod);
					IMethod method = (IMethod) this.m_opl;

					if (method != null) {
						method.Run(pAgent);

						bValid = true;
					}
				}
			}

			// assign
			else if (this.m_operator == EOperatorType.E_ASSIGN) {
				if (this.m_opl != null) {
					this.m_opl.SetValue(pAgent, this.m_opr2);

					bValid = true;
				}
			}

			// compute
			else if (this.m_operator.ordinal() >= EOperatorType.E_ADD.ordinal()
					&& this.m_operator.ordinal() <= EOperatorType.E_DIV.ordinal()) {
				if (this.m_opl != null) {
					this.m_opl.Compute(pAgent, this.m_opr1, this.m_opr2, m_operator);

					bValid = true;
				}
			}

			// compare
			else if (this.m_operator.ordinal() >= EOperatorType.E_EQUAL.ordinal()
					&& this.m_operator.ordinal() <= EOperatorType.E_LESSEQUAL.ordinal()) {
				if (this.m_opl != null) {
					bValid = this.m_opl.Compare(pAgent, this.m_opr2, m_operator);
				}
			}

			return bValid;
		}

	}

	protected ActionConfig m_ActionConfig;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		this.m_ActionConfig.load(properties);
	}

	@Override
	public boolean Evaluate(Agent pAgent) {
		boolean bValid = this.m_ActionConfig.Execute(pAgent);

		if (!bValid) {
			EBTStatus childStatus = EBTStatus.BT_INVALID;
			bValid = (EBTStatus.BT_SUCCESS == this.update_impl(pAgent, childStatus));
		}

		return bValid;
	}

	public boolean Evaluate(Agent pAgent, EBTStatus status) {
		boolean bValid = this.m_ActionConfig.Execute(pAgent);

		if (!bValid) {
			EBTStatus childStatus = EBTStatus.BT_INVALID;
			bValid = (EBTStatus.BT_SUCCESS == this.update_impl(pAgent, childStatus));
		}

		return bValid;
	}

	@Override
	protected BehaviorTask createTask() {
		Debug.Check(false);
		return null;
	}

}
