package org.gof.behaviac.node;

import java.util.ArrayList;
import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.Debug;
import org.gof.behaviac.NodeHandler_t;

public class CompositeTask extends BranchTask {
	protected int m_activeChildIndex = InvalidChildIndex;
	protected static final int InvalidChildIndex = -1;
	protected boolean m_bIgnoreChildren = false;
	protected List<BehaviorTask> m_children = new ArrayList<BehaviorTask>();

	protected CompositeTask() {
		m_activeChildIndex = InvalidChildIndex;
	}

	@Override
	public void Init(BehaviorNode node) {
		super.Init(node);

		if (!this.m_bIgnoreChildren) {
			Debug.Check(node.GetChildrenCount() > 0);

			int childrenCount = node.GetChildrenCount();

			for (int i = 0; i < childrenCount; i++) {
				BehaviorNode childNode = node.GetChild(i);
				BehaviorTask childTask = childNode.CreateAndInitTask();

				this.addChild(childTask);
			}
		}
	}

	@Override
	protected void addChild(BehaviorTask pBehavior) {
		pBehavior.SetParent(this);

		this.m_children.add(pBehavior);
	}

	@Override
	public void copyto(BehaviorTask target) {
		super.copyto(target);

		Debug.Check(target instanceof CompositeTask);
		CompositeTask ttask = (CompositeTask) target;

		ttask.m_activeChildIndex = this.m_activeChildIndex;

		Debug.Check(this.m_children.size() > 0);
		Debug.Check(this.m_children.size() == ttask.m_children.size());

		int count = this.m_children.size();

		for (int i = 0; i < count; ++i) {
			BehaviorTask childTask = this.m_children.get(i);
			BehaviorTask childTTask = ttask.m_children.get(i);

			childTask.copyto(childTTask);
		}
	}

	protected BehaviorTask GetChildById(int nodeId) {
		if (this.m_children != null && this.m_children.size() > 0) {
			for (int i = 0; i < this.m_children.size(); ++i) {
				BehaviorTask c = this.m_children.get(i);

				if (c.GetId() == nodeId) {
					return c;
				}
			}
		}

		return null;
	}

	@Override
	public void traverse(boolean childFirst, NodeHandler_t handler, Agent pAgent, Object user_data) {
		if (childFirst) {
			for (int i = 0; i < this.m_children.size(); ++i) {
				BehaviorTask task = this.m_children.get(i);
				task.traverse(childFirst, handler, pAgent, user_data);
			}

			handler.run(this, pAgent, user_data);
		} else {
			if (handler.run(this, pAgent, user_data)) {
				for (int i = 0; i < this.m_children.size(); ++i) {
					BehaviorTask task = this.m_children.get(i);
					task.traverse(childFirst, handler, pAgent, user_data);
				}
			}
		}
	}

}
