package org.gof.behaviac;

@FunctionalInterface
public interface NodeHandler_t {
	boolean run(BehaviorTask task, Agent agent, Object user_data);
}
