package org.gof.behaviac;

public class LeafTask extends BehaviorTask {
	protected LeafTask() {
	}

	@Override
	public void traverse(boolean childFirst, NodeHandler_t handler, Agent pAgent, Object user_data) {
		handler.run(this, pAgent, user_data);
	}

}
