package org.gof.behaviac;

public class SingeChildTask extends BranchTask {
	protected BehaviorTask m_root;

	protected SingeChildTask() {
		m_root = null;
	}

	@Override
	public void Init(BehaviorNode node) {
		// TODO Auto-generated method stub
		super.Init(node);
		Debug.Check(node.GetChildrenCount() <= 1);

		if (node.GetChildrenCount() == 1) {
			BehaviorNode childNode = node.GetChild(0);
			Debug.Check(childNode != null);

			if (childNode != null) {
				BehaviorTask childTask = childNode.CreateAndInitTask();

				this.addChild(childTask);
			}
		} else {
			Debug.Check(true);
		}
	}

	@Override
	public void copyto(BehaviorTask target) {
		// TODO Auto-generated method stub
		super.copyto(target);

		Debug.Check(target instanceof SingeChildTask);
		SingeChildTask ttask = (SingeChildTask) target;

		if (this.m_root != null && ttask != null) {
			// referencebehavior/query, etc.
			if (ttask.m_root == null) {
				BehaviorNode pNode = this.m_root.GetNode();
				Debug.Check(pNode instanceof BehaviorTree);
				if (pNode != null) {
					ttask.m_root = pNode.CreateAndInitTask();
				}

				// Debug.Check(ttask.m_root is BehaviorTreeTask);
				// BehaviorTreeTask btt = ttask.m_root as BehaviorTreeTask;
				// btt.ModifyId(ttask);
			}

			Debug.Check(ttask.m_root != null);
			if (ttask.m_root != null) {
				this.m_root.copyto(ttask.m_root);
			}
		}
	}

	@Override
	protected void addChild(BehaviorTask pBehavior) {
		pBehavior.SetParent(this);

		this.m_root = pBehavior;
	}

	@Override
	protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
		if (this.m_root != null) {
			EBTStatus s = this.m_root.exec(pAgent, childStatus);
			return s;
		}

		return EBTStatus.BT_FAILURE;
	}

	@Override
	public void traverse(boolean childFirst, NodeHandler_t handler, Agent pAgent, Object user_data) {
		if (childFirst) {
			if (this.m_root != null) {
				this.m_root.traverse(childFirst, handler, pAgent, user_data);
			}

			handler.run(this, pAgent, user_data);
		} else {
			if (handler.run(this, pAgent, user_data)) {
				if (this.m_root != null) {
					this.m_root.traverse(childFirst, handler, pAgent, user_data);
				}
			}
		}
	}

}
