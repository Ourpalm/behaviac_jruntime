package org.gof.behaviac.fsm;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.Workspace;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.CInstanceMember;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class WaitFramesState extends State {
	protected IInstanceMember m_frames;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name.equals("Frames")) {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_frames = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_frames = AgentMeta.ParseMethod(p.value);
				}
			}
		}
	}

	protected int GetFrames(Agent pAgent) {
		if (this.m_frames != null) {
			Debug.Check(this.m_frames instanceof CInstanceMember);
			return (Integer) this.m_frames.GetValueObject(pAgent);
		}

		return 0;
	}

	@Override
	protected BehaviorTask createTask() {
		WaitFramesStateTask pTask = new WaitFramesStateTask();

		return pTask;
	}

	private static class WaitFramesStateTask extends State.StateTask {
		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof WaitFramesStateTask);
			WaitFramesStateTask ttask = (WaitFramesStateTask) target;
			ttask.m_start = this.m_start;
			ttask.m_frames = this.m_frames;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			this.m_nextStateId = -1;

			this.m_start = pAgent.GetFrameSinceStartup();
			this.m_frames = this.GetFrames(pAgent);

			return (this.m_frames >= 0);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			Debug.Check(childStatus == EBTStatus.BT_RUNNING);
			Debug.Check(this.m_node instanceof WaitFramesState, "node is not an WaitFramesState");
			WaitFramesState pStateNode = (WaitFramesState) this.m_node;

			if (pAgent.GetFrameSinceStartup() - this.m_start + 1 >= this.m_frames) {
				var r = pStateNode.Update(pAgent, this.m_nextStateId);
				this.m_nextStateId = r.value2;
				return EBTStatus.BT_SUCCESS;
			}

			return EBTStatus.BT_RUNNING;
		}

		private int GetFrames(Agent pAgent) {
			Debug.Check(this.GetNode() instanceof WaitFramesState);
			WaitFramesState pWaitNode = (WaitFramesState) (this.GetNode());

			return pWaitNode != null ? pWaitNode.GetFrames(pAgent) : 0;
		}

		private long m_start;
		private long m_frames;
	}
}
