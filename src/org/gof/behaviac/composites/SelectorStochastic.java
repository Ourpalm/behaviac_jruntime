package org.gof.behaviac.composites;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;

@RegisterableNode
public class SelectorStochastic extends CompositeStochastic {

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof SelectorStochastic)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		SelectorStochasticTask pTask = new SelectorStochasticTask();

		return pTask;
	}

	private static class SelectorStochasticTask extends CompositeStochasticTask {
		@Override
		protected void addChild(BehaviorTask pBehavior) {
			super.addChild(pBehavior);
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			super.onenter(pAgent);

			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			super.onexit(pAgent, s);

		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			EBTStatus s = childStatus;
			Debug.Check(this.m_activeChildIndex < this.m_children.size());

			SelectorStochastic node = (SelectorStochastic) this.m_node;

			// Keep going until a child behavior says its running.
			for (;;) {
				if (s == EBTStatus.BT_RUNNING) {
					int childIndex = this.m_set.get(this.m_activeChildIndex);
					BehaviorTask pBehavior = this.m_children.get(childIndex);

					if (node.CheckIfInterrupted(pAgent)) {
						return EBTStatus.BT_FAILURE;
					}

					s = pBehavior.exec(pAgent);
				}

				// If the child succeeds, or keeps running, do the same.
				if (s != EBTStatus.BT_FAILURE) {
					return s;
				}

				// Hit the end of the array, job done!
				++this.m_activeChildIndex;

				if (this.m_activeChildIndex >= this.m_children.size()) {
					return EBTStatus.BT_FAILURE;
				}

				s = EBTStatus.BT_RUNNING;
			}
		}
	}
}
