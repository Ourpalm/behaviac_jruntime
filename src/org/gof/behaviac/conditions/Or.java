package org.gof.behaviac.conditions;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.composites.Selector;

@RegisterableNode
public class Or extends ConditionBase {
	public Or() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Or)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	public boolean Evaluate(Agent pAgent) {
		boolean ret = true;

		for (int i = 0; i < this.m_children.size(); ++i) {
			BehaviorNode c = this.m_children.get(i);
			ret = c.Evaluate(pAgent);

			if (ret) {
				break;
			}
		}

		return ret;
	}

	@Override
	protected BehaviorTask createTask() {
		OrTask pTask = new OrTask();

		return pTask;
	}

	private static class OrTask extends Selector.SelectorTask {
		public OrTask() {
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			// Debug.Check(this.m_children.Count == 2);
			for (int i = 0; i < this.m_children.size(); ++i) {
				BehaviorTask pBehavior = this.m_children.get(i);
				EBTStatus s = pBehavior.exec(pAgent);

				// If the child succeeds, succeeds
				if (s == EBTStatus.BT_SUCCESS) {
					return s;
				}

				Debug.Check(s == EBTStatus.BT_FAILURE);
			}

			return EBTStatus.BT_FAILURE;
		}
	}
}
