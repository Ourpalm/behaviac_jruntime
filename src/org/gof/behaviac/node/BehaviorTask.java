package org.gof.behaviac.node;

import org.gof.behaviac.Debug;
import org.gof.behaviac.EBTStatus;

public abstract class BehaviorTask {
	public static void DestroyTask(BehaviorTask task) {
	}

	public void Init(BehaviorNode node) {
		Debug.check(node != null);

		this.m_node = node;
		this.m_id = this.m_node.getId();
	}

	public void Clear() {
		this.m_status = EBTStatus.BT_INVALID;
		this.m_parent = null;
		this.m_id = -1;
	}

	public void copyto(BehaviorTask target) {
		target.m_status = this.m_status;
	}

	public BehaviorTreeTask getRootTask()    {
             BehaviorTask task = this;

             while (task.m_parent != null)
             {
                 task = task.m_parent;
             }

             Debug.Check(task is BehaviorTreeTask);
             BehaviorTreeTask tree = (BehaviorTreeTask)task;

             return tree;
     }

	public string GetClassNameString() {
		if (this.m_node != null) {
			return this.m_node.GetClassNameString();
		}

		string subBT = "SubBT";
		return subBT;
	}

	public int GetId() {
		return this.m_id;
	}

	public int GetNextStateId() {
		return -1;
	}

	public BehaviorTask

			GetCurrentTask() {
		return null;
	}
}
