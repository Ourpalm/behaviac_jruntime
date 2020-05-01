package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.DecoratorTask;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.Workspace;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IInstanceMember;

@RegisterableNode
public class DecoratorFrames extends DecoratorNode {
	protected IInstanceMember m_frames;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);
		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Frames") {
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
			return ((Number) this.m_frames.GetValueObject(pAgent)).intValue();
		}

		return 0;
	}

	@Override
	protected BehaviorTask createTask() {
		DecoratorFramesTask pTask = new DecoratorFramesTask();

		return pTask;
	}

	private static class DecoratorFramesTask extends DecoratorTask {
		@Override
		public void copyto(BehaviorTask target) {
			super.copyto(target);

			Debug.Check(target instanceof DecoratorFramesTask);
			DecoratorFramesTask ttask = (DecoratorFramesTask) target;

			ttask.m_start = this.m_start;
			ttask.m_frames = this.m_frames;
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			super.onenter(pAgent);

			this.m_start = Workspace.Instance.GetFrameSinceStartup();
			this.m_frames = this.GetFrames(pAgent);

			return (this.m_frames >= 0);
		}

		@Override
		protected EBTStatus decorate(EBTStatus status) {
			if (Workspace.Instance.GetFrameSinceStartup() - this.m_start + 1 >= this.m_frames) {
				return EBTStatus.BT_SUCCESS;
			}

			return EBTStatus.BT_RUNNING;
		}

		private int GetFrames(Agent pAgent) {
			Debug.Check(this.GetNode() instanceof DecoratorFrames);
			DecoratorFrames pNode = (DecoratorFrames) (this.GetNode());

			return pNode != null ? pNode.GetFrames(pAgent) : 0;
		}

		private long m_start = 0;
		private long m_frames = 0;
	}

}
