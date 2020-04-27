package org.gof.behaviac;

import java.util.List;

public class Effector extends AttachAction {
	public static enum EPhase {
		E_SUCCESS, E_FAILURE, E_BOTH,
	}

	public static class EffectorConfig extends ActionConfig {
		public EPhase m_phase = EPhase.E_SUCCESS;

		@Override
		public boolean load(List<property_t> properties) {
			boolean loaded = super.load(properties);

			try {
				for (int i = 0; i < properties.size(); ++i) {
					property_t p = properties.get(i);

					if (p.name == "Phase") {
						if (p.value == "Success") {
							this.m_phase = EPhase.E_SUCCESS;
						} else if (p.value == "Failure") {
							this.m_phase = EPhase.E_FAILURE;
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

	public Effector() {
		m_ActionConfig = new EffectorConfig();
	}

	public EPhase GetPhase() {
		return ((EffectorConfig) m_ActionConfig).m_phase;
	}

	public void SetPhase(EPhase p) {
		((EffectorConfig) m_ActionConfig).m_phase = p;
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Effector)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}
}
