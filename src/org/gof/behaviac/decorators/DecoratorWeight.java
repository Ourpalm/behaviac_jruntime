package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.CInstanceMember;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class DecoratorWeight extends DecoratorNode {
	protected IInstanceMember m_weight;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name .equals("Weight")) {
				this.m_weight = AgentMeta.ParseProperty(p.value);
			}
		}
	}

	protected int GetWeight(Agent pAgent) {
		if (this.m_weight != null) {
			Debug.Check(this.m_weight instanceof CInstanceMember);
			return (Integer) this.m_weight.GetValueObject(pAgent);
		}

		return 0;
	}

	@Override
	public boolean IsManagingChildrenAsSubTrees() {
		return false;
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorWeightTask pTask = new DecoratorWeightTask();

		return pTask;
	}

	public static class DecoratorWeightTask extends DecoratorTask {

		public int GetWeight(Agent pAgent) {
			Debug.Check(this.GetNode() instanceof DecoratorWeight);
			DecoratorWeight pNode = (DecoratorWeight) (this.GetNode());

			return pNode != null ? pNode.GetWeight(pAgent) : 0;
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			return status;
		}
	}
}
