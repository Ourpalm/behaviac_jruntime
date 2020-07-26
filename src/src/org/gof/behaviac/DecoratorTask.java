package org.gof.behaviac;

public abstract class DecoratorTask extends SingeChildTask {
	protected DecoratorTask() {
	}

	@Override
	protected EBTStatus update_current(Agent pAgent, EBTStatus childStatus) {
		// TODO Auto-generated method stub
		return super.update_current(pAgent, childStatus);
	}

	@Override
	protected EBTStatus update(Agent pAgent, EBTStatus childStatus) {
		Debug.Check(this.m_node instanceof DecoratorNode);
		DecoratorNode node = (DecoratorNode) this.m_node;

		EBTStatus status = EBTStatus.BT_INVALID;

		if (childStatus != EBTStatus.BT_RUNNING) {
			status = childStatus;

			if (!node.m_bDecorateWhenChildEnds || status != EBTStatus.BT_RUNNING) {
				EBTStatus result = this.decorate(status);

				if (result != EBTStatus.BT_RUNNING) {
					return result;
				}

				return EBTStatus.BT_RUNNING;
			}
		}

		status = super.update(pAgent, childStatus);

		if (!node.m_bDecorateWhenChildEnds || status != EBTStatus.BT_RUNNING) {
			EBTStatus result = this.decorate(status);

			return result;
		}

		return EBTStatus.BT_RUNNING;
	}

	protected abstract EBTStatus decorate(EBTStatus status);
}
