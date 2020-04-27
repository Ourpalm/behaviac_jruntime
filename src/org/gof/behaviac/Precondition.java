package org.gof.behaviac;

import java.util.List;

public class Precondition extends AttachAction {
	public static enum EPhase {
		E_ENTER, E_UPDATE, E_BOTH,
	}

	public static class PreconditionConfig extends ActionConfig {
		public EPhase m_phase = EPhase.E_ENTER;
		public boolean m_bAnd = false;

		@Override
		public boolean load(List<property_t> properties) {
			boolean loaded = super.load(properties);

			try {
				for (int i = 0; i < properties.size(); ++i) {
					property_t p = properties.get(i);

					if (p.name == "BinaryOperator") {
						if (p.value == "Or") {
							this.m_bAnd = false;
						} else if (p.value == "And") {
							this.m_bAnd = true;
						} else {
							Debug.Check(false);
						}
					} else if (p.name == "Phase") {
						if (p.value == "Enter") {
							this.m_phase = EPhase.E_ENTER;
						} else if (p.value == "Update") {
							this.m_phase = EPhase.E_UPDATE;
						} else if (p.value == "Both") {
							this.m_phase = EPhase.E_BOTH;
						} else {
							Debug.Check(false);
						}

						break;
					}
				}
			} catch (Exception ex) {
				Debug.Check(false, ex.getMessage());
				loaded = false;
			}

			return loaded;
		}
	}

	public Precondition() {
		m_ActionConfig = new PreconditionConfig();
	}

	public EPhase GetPhase() {
		return ((PreconditionConfig) (this.m_ActionConfig)).m_phase;
	}

	public void SetPhase(EPhase phase) {
		((PreconditionConfig) (this.m_ActionConfig)).m_phase = phase;
	}

	public boolean IsAnd() {
		return ((PreconditionConfig) (this.m_ActionConfig)).m_bAnd;
	}

	public void SetIsAnd(boolean value) {
		((PreconditionConfig) (this.m_ActionConfig)).m_bAnd = value;
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Precondition)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}
}
