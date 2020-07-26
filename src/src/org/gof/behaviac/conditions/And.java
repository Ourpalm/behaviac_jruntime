package org.gof.behaviac.conditions;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.composites.Sequence;

@RegisterableNode
public class And extends ConditionBase {

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof And)) {
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

			if (!ret) {
				break;
			}
		}

		return ret;
	}

	@Override
	protected BehaviorTask createTask() {
		AndTask pTask = new AndTask();

		return pTask;
	}

	static class AndTask extends Sequence.SequenceTask {
		public AndTask() {
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);
			// Debug.Check(this.m_children.Count == 2);

			for (int i = 0; i < this.m_children.size(); ++i) {
				BehaviorTask pBehavior = this.m_children.get(i);
				EBTStatus s = pBehavior.exec(pAgent);

				// If the child fails, fails
				if (s == EBTStatus.BT_FAILURE) {
					return s;
				}

				Debug.Check(s == EBTStatus.BT_SUCCESS);
			}

			return EBTStatus.BT_SUCCESS;
		}
	}

}
