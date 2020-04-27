package org.gof.behaviac.node;

import java.util.List;

import org.gof.behaviac.Agent;

public abstract class DecoratorNode extends BehaviorNode {
	public boolean m_bDecorateWhenChildEnds;

	public DecoratorNode() {
		this.m_bDecorateWhenChildEnds = false;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);
			if (p.name == "DecorateWhenChildEnds") {
				if (p.value == "true") {
					this.m_bDecorateWhenChildEnds = true;
				}
			}
		}
	}

	@Override
	public boolean IsManagingChildrenAsSubTrees() {
		return true;
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorNode)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}

}
