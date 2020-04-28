package org.gof.behaviac.actions;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.IMethod;
import org.gof.behaviac.IValue;
import org.gof.behaviac.LeafTask;
import org.gof.behaviac.TValue;
import org.gof.behaviac.Utils;
import org.gof.behaviac.property_t;

public class Action extends BehaviorNode {
	protected IMethod m_method;
	protected IMethod m_resultFunctor;
	protected EBTStatus m_resultOption = EBTStatus.BT_INVALID;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Method") {
				if (!Utils.isNullOrEmpty(p.value)) {
					this.m_method = AgentMeta.ParseMethod(p.value);
				}
			} else if (p.name == "ResultOption") {
				if (p.value == "BT_INVALID") {
					m_resultOption = EBTStatus.BT_INVALID;
				} else if (p.value == "BT_FAILURE") {
					m_resultOption = EBTStatus.BT_FAILURE;
				} else if (p.value == "BT_RUNNING") {
					m_resultOption = EBTStatus.BT_RUNNING;
				} else {
					m_resultOption = EBTStatus.BT_SUCCESS;
				}
			} else if (p.name == "ResultFunctor") {
				if (!Utils.isNullOrEmpty(p.value)) {
					this.m_resultFunctor = AgentMeta.ParseMethod(p.value);
					;
				}
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Action)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}

	public EBTStatus Execute(Agent pAgent, EBTStatus childStatus) {
		EBTStatus result = EBTStatus.BT_SUCCESS;

		if (this.m_method != null) {
			if (this.m_resultOption != EBTStatus.BT_INVALID) {
				this.m_method.Run(pAgent);

				result = this.m_resultOption;
			} else {
				if (this.m_resultFunctor != null) {
					IValue returnValue = this.m_resultFunctor.GetIValue(pAgent, this.m_method);

					result = ((TValue) returnValue).getT();
				} else {
					IValue returnValue = this.m_method.GetIValue(pAgent);

					Debug.Check(returnValue instanceof TValue, "method's return type is not EBTStatus");

					result = ((TValue) returnValue).getT();
				}
			}
		} else {
			result = this.update_impl(pAgent, childStatus);
		}

		return result;
	}

	@Override
	protected BehaviorTask createTask() {
		ActionTask pTask = new ActionTask();

		return pTask;
	}

	private static class ActionTask extends LeafTask {
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

			Debug.Check(this.GetNode() instanceof Action, "node is not an Action");
			Action pActionNode = (Action) (this.GetNode());

			EBTStatus result = pActionNode.Execute(pAgent, childStatus);

			return result;
		}
	}
}
