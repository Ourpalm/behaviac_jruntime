package org.gof.behaviac.node;

import org.gof.behaviac.Agent;

public class ConditionBase extends BehaviorNode {
	@Override
	public  boolean isValid(Agent pAgent, BehaviorTask pTask)
    {
        if (!(pTaskgetNode() instanceof ConditionBase))
        {
            return false;
        }

        return base.IsValid(pAgent, pTask);
    }
}
