package org.gof.behaviac.fsm;

import java.util.ArrayList;
import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.Effector;
import org.gof.behaviac.Precondition;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;

@RegisterableNode
public class StartCondition extends Precondition {
	protected List<Effector.EffectorConfig> m_effectors = new ArrayList<Effector.EffectorConfig>();
	protected int m_targetId = -1;

	public int GetTargetStateId() {
		return m_targetId;
	}

	public void SetTargetStateId(int value) {
		m_targetId = value;
	}

	@Override
	public void ApplyEffects(Agent pAgent, org.gof.behaviac.Effector.EPhase phase) {
		for (int i = 0; i < this.m_effectors.size(); ++i) {
			Effector.EffectorConfig effector = this.m_effectors.get(i);
			effector.Execute(pAgent);
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof StartCondition)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		if (this.m_loadAttachment) {
			Effector.EffectorConfig effectorConfig = new Effector.EffectorConfig();

			if (effectorConfig.load(properties)) {
				this.m_effectors.add(effectorConfig);
			}

			return;
		}

		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("TargetFSMNodeId")) {
				this.m_targetId = Integer.parseInt(p.value);
			} else {
				// Debug.Check(0, "unrecognised property %s", p.name);
			}
		}
	}

	@Override
	protected BehaviorTask createTask() {
		Debug.Check(false);
		return null;
	}
}
