package org.gof.behaviac.node;

import org.gof.behaviac.Agent;

public class Precondition extends AttachAction {
	public static enum EPhase {
		E_ENTER, E_UPDATE, E_BOTH,
	}

	public EPhase getPhase() {
		return EPhase.E_ENTER;
	}

	public boolean Evaluate(Agent pAgent) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isAnd() {
		return false;
	}
}
