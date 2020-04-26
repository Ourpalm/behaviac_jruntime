package org.gof.behaviac.node;

import org.gof.behaviac.Agent;

public class Effector extends AttachAction {
	public static enum EPhase {
		E_SUCCESS,
		E_FAILURE,
		E_BOTH,
	}
	
	public EPhase getPhase() {
		return EPhase.E_SUCCESS;
	}

	public void Evaluate(Agent pAgent) {
		// TODO Auto-generated method stub
		
	}
}
