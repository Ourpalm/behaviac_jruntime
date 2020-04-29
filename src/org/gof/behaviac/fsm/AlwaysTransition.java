package org.gof.behaviac.fsm;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;

@RegisterableNode
public class AlwaysTransition extends Transition {
	public enum ETransitionPhase {
		ETP_Always, ETP_Success, ETP_Failure, ETP_Exit,
	}

	public AlwaysTransition() {
	}

	// ~AlwaysTransition()
	// {
	// }

	protected ETransitionPhase m_transitionPhase = ETransitionPhase.ETP_Always;

	public ETransitionPhase GetTransitionPhase() {
		return this.m_transitionPhase;
	}

	public void SetTransitionPhase(ETransitionPhase value) {
		this.m_transitionPhase = value;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "TransitionPhase") {
				if (p.value == "ETP_Exit") {
					this.m_transitionPhase = ETransitionPhase.ETP_Exit;
				} else if (p.value == "ETP_Success") {
					this.m_transitionPhase = ETransitionPhase.ETP_Success;
				} else if (p.value == "ETP_Failure") {
					this.m_transitionPhase = ETransitionPhase.ETP_Failure;
				} else if (p.value == "ETP_Always") {
					this.m_transitionPhase = ETransitionPhase.ETP_Always;
				} else {
					Debug.Check(false);
				}
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof AlwaysTransition)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		Debug.Check(false);
		return null;
	}

	@Override
	public boolean Evaluate(Agent pAgent, EBTStatus status) {
		if (this.m_transitionPhase == ETransitionPhase.ETP_Always) {
			return true;
		} else if (status == EBTStatus.BT_SUCCESS && (this.m_transitionPhase == ETransitionPhase.ETP_Success
				|| this.m_transitionPhase == ETransitionPhase.ETP_Exit)) {
			return true;
		} else if (status == EBTStatus.BT_FAILURE && (this.m_transitionPhase == ETransitionPhase.ETP_Failure
				|| this.m_transitionPhase == ETransitionPhase.ETP_Exit)) {
			return true;
		}

		return false;
	}
}
