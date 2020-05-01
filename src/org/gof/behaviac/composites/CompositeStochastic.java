package org.gof.behaviac.composites;

import java.util.ArrayList;
import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.RandomGenerator;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IMethod;

@RegisterableNode
public class CompositeStochastic extends BehaviorNode {
	protected IMethod m_method;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "RandomGenerator") {
				this.m_method = AgentMeta.ParseMethod(p.value);
			}
		}
	}

	public boolean CheckIfInterrupted(Agent pAgent) {
		boolean bInterrupted = this.EvaluteCustomCondition(pAgent);

		return bInterrupted;
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof CompositeStochastic)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		// TODO Auto-generated method stub
		return null;
	}

	public static class CompositeStochasticTask extends CompositeTask {
		// generate a random float value between 0 and 1.
		public static float GetRandomValue(IMethod method, Agent pAgent) {
			float value = 0;

			if (method != null) {
				value = (float) method.GetValueObject(pAgent);
			} else {
				value = RandomGenerator.Random();
			}

			Debug.Check(value >= 0.0f && value < 1.0f);
			return value;
		}

		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof CompositeStochasticTask);
			CompositeStochasticTask ttask = (CompositeStochasticTask) target;

			ttask.m_set = this.m_set;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			Debug.Check(this.m_children.size() > 0);

			this.random_child(pAgent);

			this.m_activeChildIndex = 0;
			return true;
		}

		private void random_child(Agent pAgent) {
			Debug.Check(this.GetNode() == null || this.GetNode() instanceof CompositeStochastic);
			CompositeStochastic pNode = (CompositeStochastic) (this.GetNode());

			int n = this.m_children.size();

			if (this.m_set.size() != n) {
				this.m_set.clear();

				for (int i = 0; i < n; ++i) {
					this.m_set.add(i);
				}
			}

			for (int i = 0; i < n; ++i) {
				int index1 = (int) (n * GetRandomValue(pNode != null ? pNode.m_method : null, pAgent));
				Debug.Check(index1 < n);

				int index2 = (int) (n * GetRandomValue(pNode != null ? pNode.m_method : null, pAgent));
				Debug.Check(index2 < n);

				// swap
				if (index1 != index2) {
					int old = this.m_set.get(index1);
					this.m_set.set(index1, this.m_set.get(index2));
					this.m_set.set(index2, old);
				}
			}
		}

		protected List<Integer> m_set = new ArrayList<Integer>();
	}
}
