package org.gof.behaviac.htn;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorNode;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.CompositeTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.IMethod;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.composites.Sequence;

@RegisterableNode
public class Task extends BehaviorNode {
	public static final String LOCAL_TASK_PARAM_PRE = "_$local_task_param_$_";
	protected IMethod m_task;

	protected boolean m_bHTN;

	public boolean IsHTN() {
		return this.m_bHTN;
	}

	public int FindMethodIndex(Method method) {
		for (int i = 0; i < this.GetChildrenCount(); ++i) {
			BehaviorNode child = this.GetChild(i);

			if (child == method) {
				return i;
			}
		}

		return -1;
	}

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Prototype") {
				this.m_task = AgentMeta.ParseMethod(p.value);
			} else if (p.name == "IsHTN") {
				this.m_bHTN = (p.value == "true");
			}
		}
	}

	@Override
	protected BehaviorTask createTask() {
		TaskTask pTask = new TaskTask();

		return pTask;
	}

	static class TaskTask extends Sequence.SequenceTask {

		@Override
		public void Init(BehaviorNode node) {
			Debug.Check(node instanceof Task, "node is not an Method");
			Task pTaskNode = (Task) (node);

			if (pTaskNode.IsHTN()) {
				this.m_bIgnoreChildren = true;
			}

			super.Init(node);
		}

		@Override
		protected void addChild(BehaviorTask pBehavior) {
			super.addChild(pBehavior);
		}

		@Override
		protected boolean onenter(Agent pAgent) {
			// reset the action child as it will be checked in the update
			this.m_activeChildIndex = CompositeTask.InvalidChildIndex;
			Debug.Check(this.m_activeChildIndex == CompositeTask.InvalidChildIndex);

			return super.onenter(pAgent);
		}

		@Override
		protected void onexit(Agent pAgent, EBTStatus s) {
			super.onexit(pAgent, s);
		}

		@Override
		protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
			EBTStatus status = childStatus;

			if (childStatus == EBTStatus.BT_RUNNING) {
				Debug.Check(this.GetNode() instanceof Task, "node is not an Method");
				Task pTaskNode = (Task) (this.GetNode());

				if (pTaskNode.IsHTN()) {
				} else {
					Debug.Check(this.m_children.size() == 1);
					BehaviorTask c = this.m_children.get(0);
					status = c.exec(pAgent);
				}
			} else {
				Debug.Check(true);
			}

			return status;
		}
	}
}
