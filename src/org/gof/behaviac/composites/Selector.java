package org.gof.behaviac.composites;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.Tuple2;

public class Selector extends BehaviorNode {
	public Selector() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Selector)) {
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

	public Tuple2<EBTStatus, Integer> SelectorUpdate(Agent pAgent, EBTStatus childStatus,
			/* ref */ int activeChildIndex, List<BehaviorTask> children) {
		EBTStatus s = childStatus;

		for (;;) {
			Debug.Check(activeChildIndex < children.size());

			if (s == EBTStatus.BT_RUNNING) {
				BehaviorTask pBehavior = children.get(activeChildIndex);

				if (this.CheckIfInterrupted(pAgent)) {
					return new Tuple2<>(EBTStatus.BT_FAILURE, activeChildIndex);
				}

				s = pBehavior.exec(pAgent);
			}

			// If the child succeeds, or keeps running, do the same.
			if (s != EBTStatus.BT_FAILURE) {
				return new Tuple2<>(s, activeChildIndex);
			}

			// Hit the end of the array, job done!
			++activeChildIndex;

			if (activeChildIndex >= children.size()) {
				return new Tuple2<>(EBTStatus.BT_FAILURE, activeChildIndex);
			}

			s = EBTStatus.BT_RUNNING;
		}
	}

	public boolean CheckIfInterrupted(Agent pAgent) {
		boolean bInterrupted = this.EvaluteCustomCondition(pAgent);

		return bInterrupted;
	}

	@Override
	protected BehaviorTask createTask() {
		SelectorTask pTask = new SelectorTask();

		return pTask;
	}

	public static class SelectorTask extends CompositeTask {
		public SelectorTask() {
		}

		// ~SelectorTask()
		// {
		// }

		@Override
		protected boolean onenter(Agent pAgent) {
			Debug.Check(this.m_children.size() > 0);
			this.m_activeChildIndex = 0;
			return true;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.m_activeChildIndex < this.m_children.size());
			Selector node = (Selector) this.m_node;

			var r = node.SelectorUpdate(pAgent, childStatus, this.m_activeChildIndex, this.m_children);
			m_activeChildIndex = r.value2;
			return r.value1;
		}
	}

}
