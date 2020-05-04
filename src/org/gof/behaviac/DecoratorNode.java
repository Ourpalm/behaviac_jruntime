package org.gof.behaviac;

import java.util.List;

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
			if (p.name.equals("DecorateWhenChildEnds")) {
				if (p.value.equals("true")) {
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
