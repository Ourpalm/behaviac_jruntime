package org.gof.behaviac.composites;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.Tuple2;

@RegisterableNode
public class Sequence extends BehaviorNode {
	public Sequence() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Sequence)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

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

	public Tuple2<EBTStatus, Integer> SequenceUpdate(Agent pAgent, EBTStatus childStatus, /* ref */int activeChildIndex,
			List<BehaviorTask> children) {
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

			// If the child fails, or keeps running, do the same.
			if (s != EBTStatus.BT_SUCCESS) {
				return new Tuple2<>(s, activeChildIndex);
			}

			// Hit the end of the array, job done!
			++activeChildIndex;

			if (activeChildIndex >= children.size()) {
				return new Tuple2<>(EBTStatus.BT_SUCCESS, activeChildIndex);
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
		return new SequenceTask();
	}

	public static class SequenceTask extends CompositeTask {
		public SequenceTask() {
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			this.m_activeChildIndex = 0;

			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			super.onexit(pAgent, s);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.m_activeChildIndex < this.m_children.size());

			Sequence node = (Sequence) this.m_node;

			var r = node.SequenceUpdate(pAgent, childStatus, this.m_activeChildIndex, this.m_children);
			m_activeChildIndex = r.value2;
			return r.value1;
		}
	}

}
