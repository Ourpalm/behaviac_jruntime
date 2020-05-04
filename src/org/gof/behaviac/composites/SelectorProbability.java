package org.gof.behaviac.composites;

import java.util.ArrayList;
import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.decorators.DecoratorWeight;
import org.gof.behaviac.members.IMethod;

@RegisterableNode
public class SelectorProbability extends BehaviorNode {
	protected IMethod m_method;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("RandomGenerator")) {
				this.m_method = AgentMeta.ParseMethod(p.value);
			}
		}
	}

	@Override
	public void addChild(BehaviorNode pBehavior) {
		Debug.Check(pBehavior instanceof DecoratorWeight);
		DecoratorWeight pDW = (DecoratorWeight) (pBehavior);

		if (pDW != null) {
			super.addChild(pBehavior);
		} else {
			Debug.Check(false, "only DecoratorWeightTask can be children");
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof SelectorProbability)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		SelectorProbabilityTask pTask = new SelectorProbabilityTask();

		return pTask;
	}

	private static class SelectorProbabilityTask extends CompositeTask {
		@Override
		protected boolean onenter(Agent pAgent) {
			Debug.Check(this.m_children.size() > 0);

			// if the following assert failed, just comment it out
			// Debug.Check(this.m_activeChildIndex == CompositeTask.InvalidChildIndex);

			// to reset it anyway in case onexit is not called for some reason
			this.m_activeChildIndex = CompositeTask.InvalidChildIndex;

			// SelectorProbability pSelectorProbabilityNode = this.GetNode() is
			// SelectorProbability;

			this.m_weightingMap.clear();
			this.m_totalSum = 0;

			for (int i = 0; i < this.m_children.size(); ++i) {
				BehaviorTask task = this.m_children.get(i);

				Debug.Check(task instanceof DecoratorWeight.DecoratorWeightTask);
				DecoratorWeight.DecoratorWeightTask pWT = (DecoratorWeight.DecoratorWeightTask) task;

				int weight = pWT.GetWeight(pAgent);
				this.m_weightingMap.add(weight);
				this.m_totalSum += weight;
			}

			Debug.Check(this.m_weightingMap.size() == this.m_children.size());

			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			this.m_activeChildIndex = CompositeTask.InvalidChildIndex;
			super.onexit(pAgent, s);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.GetNode() instanceof SelectorProbability);
			SelectorProbability pSelectorProbabilityNode = (SelectorProbability) (this.GetNode());

			if (childStatus != EBTStatus.BT_RUNNING) {
				return childStatus;
			}

			// check if we've already chosen a node to run
			if (this.m_activeChildIndex != CompositeTask.InvalidChildIndex) {
				BehaviorTask pNode = this.m_children.get(this.m_activeChildIndex);

				EBTStatus status = pNode.exec(pAgent);

				return status;
			}

			Debug.Check(this.m_weightingMap.size() == this.m_children.size());

			// generate a number between 0 and the sum of the weights
			float chosen = this.m_totalSum * CompositeStochastic.CompositeStochasticTask
					.GetRandomValue(pSelectorProbabilityNode.m_method, pAgent);

			float sum = 0;

			for (int i = 0; i < this.m_children.size(); ++i) {
				int w = this.m_weightingMap.get(i);

				sum += w;

				if (w > 0 && sum >= chosen) // execute this node
				{
					BehaviorTask pChild = this.m_children.get(i);

					EBTStatus status = pChild.exec(pAgent);

					if (status == EBTStatus.BT_RUNNING) {
						this.m_activeChildIndex = i;
					} else {
						this.m_activeChildIndex = CompositeTask.InvalidChildIndex;
					}

					return status;
				}
			}

			return EBTStatus.BT_FAILURE;
		}

		private List<Integer> m_weightingMap = new ArrayList<>();
		private int m_totalSum;
	}

}
