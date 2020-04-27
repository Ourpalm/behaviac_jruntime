package org.gof.behaviac;

import org.gof.behaviac.node.BehaviorTask;

@FunctionalInterface
public interface NodeHandler_t {
	boolean run(BehaviorTask task, Agent agent, Object user_data);
}
