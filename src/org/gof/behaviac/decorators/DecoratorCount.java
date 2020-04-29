package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.IInstanceMember;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;

@RegisterableNode
public class DecoratorCount extends DecoratorNode {
	protected IInstanceMember m_count;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Count") {
				this.m_count = AgentMeta.ParseProperty(p.value);
			}
		}
	}

	protected int GetCount(Agent pAgent) {
		if (this.m_count != null) {
			int count = ((Number) this.m_count.GetValueObject(pAgent)).intValue();

			return count;
		}

		return 0;
	}

	@Override
	protected BehaviorTask createTask() {
		// TODO Auto-generated method stub
		return null;
	}

	protected static abstract class DecoratorCountTask extends DecoratorTask {
		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof DecoratorCountTask);
			DecoratorCountTask ttask = (DecoratorCountTask) target;

			ttask.m_n = this.m_n;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			super.onenter(pAgent);
			int count = this.GetCount(pAgent);
			if (count == 0) {
				return false;
			}
			this.m_n = count;
			return true;
		}

		public int GetCount(Agent pAgent) {
			Debug.Check(this.GetNode() instanceof DecoratorCount);
			DecoratorCount pDecoratorCountNode = (DecoratorCount) (this.GetNode());

			return pDecoratorCountNode != null ? pDecoratorCountNode.GetCount(pAgent) : 0;
		}

		protected int m_n;
	}

}
