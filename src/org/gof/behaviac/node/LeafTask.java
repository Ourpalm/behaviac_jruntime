package org.gof.behaviac.node;

import org.gof.behaviac.Agent;
import org.gof.behaviac.NodeHandler_t;

public class LeafTask extends BehaviorTask {
	protected LeafTask() {
	}

	@Override
	public void traverse(boolean childFirst, NodeHandler_t handler, Agent pAgent, Object user_data) {
		handler.run(this, pAgent, user_data);
	}

}
