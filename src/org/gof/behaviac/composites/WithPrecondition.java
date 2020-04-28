package org.gof.behaviac.composites;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;

public class WithPrecondition extends BehaviorNode {
	public WithPrecondition() {
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof WithPrecondition)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		WithPreconditionTask pTask = new WithPreconditionTask();

		return pTask;
	}

	public static class WithPreconditionTask extends Sequence.SequenceTask {
		public WithPreconditionTask() {
		}

		@Override
		protected void addChild(BehaviorTask pBehavior) {
			super.addChild(pBehavior);
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			BehaviorTask pParent = this.GetParent();

			// when as child of SelctorLoop, it is not ticked normally
			Debug.Check(pParent instanceof SelectorLoop.SelectorLoopTask);

			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			BehaviorTask pParent = this.GetParent();

			Debug.Check(pParent instanceof SelectorLoop.SelectorLoopTask);
		}

		public BehaviorTask GetPreconditionNode() {
			Debug.Check(this.m_children.size() == 2);

			return this.m_children.get(0);

		}

		public BehaviorTask GetActionNode() {
			Debug.Check(this.m_children.size() == 2);
			return this.m_children.get(1);

		}

		@Override
		protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
			return this.update(pAgent, childStatus);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			BehaviorTask pParent = this.GetParent();
			Debug.Check(pParent instanceof SelectorLoop.SelectorLoopTask);

			Debug.Check(false);

			return EBTStatus.BT_RUNNING;
		}
	}

}
