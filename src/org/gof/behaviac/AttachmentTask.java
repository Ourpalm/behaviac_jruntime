package org.gof.behaviac;

public class AttachmentTask extends BehaviorTask {
	protected AttachmentTask() {
	}

	@Override
	public void traverse(boolean childFirst, NodeHandler_t handler, Agent pAgent, Object user_data) {
		handler.run(this, pAgent, user_data);
	}

}
