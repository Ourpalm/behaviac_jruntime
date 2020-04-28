package org.gof.behaviac.fsm;

import java.util.ArrayList;
import java.util.List;

import org.gof.behaviac.Effector;
import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.IMethod;
import org.gof.behaviac.LeafTask;
import org.gof.behaviac.Tuple2;
import org.gof.behaviac.property_t;

public class State extends BehaviorNode {
	protected boolean m_bIsEndState;
	protected IMethod m_method;
	protected List<Transition> m_transitions;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Method") {
				this.m_method = AgentMeta.ParseMethod(p.value);
			} else if (p.name == "IsEndState") {
				this.m_bIsEndState = (p.value == "true");
			}
		}
	}

	@Override
	public void Attach(BehaviorNode pAttachment, boolean bIsPrecondition, boolean bIsEffector, boolean bIsTransition) {
		if (bIsTransition) {
			Debug.Check(!bIsEffector && !bIsPrecondition);

			if (this.m_transitions == null) {
				this.m_transitions = new ArrayList<Transition>();
			}

			Transition pTransition = (Transition) pAttachment;
			Debug.Check(pTransition != null);
			this.m_transitions.add(pTransition);

			return;
		}

		Debug.Check(bIsTransition == false);
		super.Attach(pAttachment, bIsPrecondition, bIsEffector, bIsTransition);
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof State)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	public boolean IsEndState() {
		return this.m_bIsEndState;
	}

	public EBTStatus Execute(Agent pAgent) {
		EBTStatus result = EBTStatus.BT_RUNNING;

		if (this.m_method != null) {
			this.m_method.Run(pAgent);
		} else {
			result = this.update_impl(pAgent, EBTStatus.BT_RUNNING);
		}

		return result;
	}

	@Override
	protected BehaviorTask createTask() {
		StateTask pTask = new StateTask();

		return pTask;
	}

	public Tuple2<EBTStatus, Integer> Update(Agent pAgent, /* out */int nextStateId) {
		nextStateId = -1;

		// when no method is specified(m_method == null),
		// 'update_impl' is used to return the configured result status for both
		// xml/bson and c#
		EBTStatus result = this.Execute(pAgent);

		if (this.m_bIsEndState) {
			result = EBTStatus.BT_SUCCESS;
		} else {
			var r = UpdateTransitions(pAgent, this, this.m_transitions, nextStateId, result);
			nextStateId = r.value2;

			if (r.value1.booleanValue()) {
				result = EBTStatus.BT_SUCCESS;
			}
		}

		return new Tuple2<>(result, nextStateId);
	}

	public static Tuple2<Boolean, Integer> UpdateTransitions(Agent pAgent, BehaviorNode node,
			List<Transition> transitions, /* ref */ int nextStateId, EBTStatus result) {
		boolean bTransitioned = false;

		if (transitions != null) {
			for (int i = 0; i < transitions.size(); ++i) {
				Transition transition = transitions.get(i);

				if (transition.Evaluate(pAgent, result)) {
					nextStateId = transition.GetTargetStateId();
					Debug.Check(nextStateId != -1);

					// transition actions
					transition.ApplyEffects(pAgent, Effector.EPhase.E_BOTH);
					bTransitioned = true;

					break;
				}
			}
		}

		return new Tuple2<>(bTransitioned, nextStateId);
	}

	public static class StateTask extends LeafTask {
		protected int m_nextStateId = -1;

		@Override
		public int GetNextStateId() {
			return m_nextStateId;
		}

		public boolean IsEndState() {
			Debug.Check(this.GetNode() instanceof State, "node is not an State");
			State pStateNode = (State) (this.GetNode());

			return pStateNode.IsEndState();
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			this.m_nextStateId = -1;
			return true;
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);

			Debug.Check(this.GetNode() instanceof State, "node is not an State");
			State pStateNode = (State) (this.GetNode());

			var r = pStateNode.Update(pAgent, this.m_nextStateId);

			EBTStatus result = r.value1;
			this.m_nextStateId = r.value2;

			return result;
		}
	}
}
