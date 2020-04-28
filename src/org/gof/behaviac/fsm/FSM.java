package org.gof.behaviac.fsm;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.property_t;
import org.gof.behaviac.EBTStatus;

public class FSM extends BehaviorNode {
	private int m_initialid = -1;

	public int GetInitialId() {
		return this.m_initialid;
	}

	public void SetInitialId(int value) {
		this.m_initialid = value;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "initialid") {
				this.m_initialid = Integer.parseInt(p.value);
			}
		}
	}

	@Override
	protected BehaviorTask createTask() {
		return new FSMTask();
	}

	public static class FSMTask extends CompositeTask {
		// used for FSM
		private int m_currentNodeId = -1;

		@Override
		protected boolean onenter(Agent pAgent) {
			Debug.Check(this.m_node != null);
			FSM fsm = (FSM) this.m_node;

			this.m_activeChildIndex = 0;

			this.m_currentNodeId = fsm.GetInitialId();

			return true;
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			this.m_currentNodeId = -1;

			super.onexit(pAgent, s);
		}

		private EBTStatus UpdateFSM(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.m_node != null);
			Debug.Check(this.m_currentNodeId != -1);

			EBTStatus status = childStatus;
			boolean bLoop = true;

			while (bLoop) {
				int nextStateId = -1;
				BehaviorTask currentState = this.GetChildById(this.m_currentNodeId);

				if (currentState != null) {
					currentState.exec(pAgent);

					if (currentState instanceof State.StateTask) {
						State.StateTask pStateTask = (State.StateTask) currentState;

						if (pStateTask != null && pStateTask.IsEndState()) {
							return EBTStatus.BT_SUCCESS;
						}
					}

					nextStateId = currentState.GetNextStateId();
				}

				if (nextStateId < 0) {
					// if not transitioned, don't go on next state, to exit
					bLoop = false;
				} else { // if transitioned, go on next state
					this.m_currentNodeId = nextStateId;
				}
			}

			return status;
		}

		@Override
		protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
			EBTStatus status = this.update(pAgent, childStatus);

			return status;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(this.m_activeChildIndex < this.m_children.size());
			Debug.Check(this.m_node instanceof FSM);

			EBTStatus s = this.UpdateFSM(pAgent, childStatus);

			return s;
		}
	}
}
