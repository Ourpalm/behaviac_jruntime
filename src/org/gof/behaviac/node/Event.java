package org.gof.behaviac.node;

import java.util.List;
import java.util.Map;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.Config;
import org.gof.behaviac.Debug;
import org.gof.behaviac.IInstantiatedVariable;
import org.gof.behaviac.IMethod;
import org.gof.behaviac.TriggerMode;
import org.gof.behaviac.Utils;

public class Event extends ConditionBase {
	protected IMethod m_event;
	protected String m_eventName;
	protected String m_referencedBehaviorPath = null;
	protected TriggerMode m_triggerMode;
	protected boolean m_bTriggeredOnce; // an event can be configured to stop being checked if triggered

	public Event() {
		m_eventName = null;
		m_bTriggeredOnce = false;
		m_triggerMode = TriggerMode.TM_Transfer;
	}

	@Override
    protected void load(int version, String agentType, List<property_t> properties) {
    	// TODO Auto-generated method stub
    	super.load(version, agentType, properties);

        for (int i = 0; i < properties.size(); ++i)
        {
            property_t p = properties.get(i);

            if (p.name == "Task")
            {
                this.m_event = AgentMeta.ParseMethod(p.value, ref this.m_eventName);
            }
            else if (p.name == "ReferenceFilename")
            {
                this.m_referencedBehaviorPath = p.value;

                if (Config.PreloadBehaviors)
                {
                    BehaviorTree behaviorTree = Workspace.Instance.LoadBehaviorTree(this.m_referencedBehaviorPath);
                    Debug.Check(behaviorTree != null);
                }
            }
            else if (p.name == "TriggeredOnce")
            {
                this.m_bTriggeredOnce = (p.value == "true");
            }
            else if (p.name == "TriggerMode")
            {
                if (p.value == "Transfer")
                {
                    this.m_triggerMode = TriggerMode.TM_Transfer;
                }
                else if (p.value == "Return")
                {
                    this.m_triggerMode = TriggerMode.TM_Return;
                }
                else
                {
                    Debug.Check(false, String.format("unrecognised trigger mode %s", p.value));
                }
            }
        }
    }

	public String GetEventName() {
		return this.m_eventName;
	}

	public boolean TriggeredOnce() {
		return this.m_bTriggeredOnce;
	}

	public TriggerMode GetTriggerMode() {
		return this.m_triggerMode;
	}

	public void switchTo(Agent pAgent, Map<Long, IInstantiatedVariable> eventParams) {
		if (!Utils.isNullOrEmpty(this.m_referencedBehaviorPath)) {
			if (pAgent != null) {
				TriggerMode tm = this.GetTriggerMode();

				pAgent.bteventtree(pAgent, this.m_referencedBehaviorPath, tm);

				Debug.Check(pAgent.CurrentTreeTask != null);
				pAgent.CurrentTreeTask.AddVariables(eventParams);

				pAgent.btexec();
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof Event)) {
			return false;
		}

		return super.IsValid(pAgent, pTask);
	}

	@Override
	protected BehaviorTask createTask() {
		Debug.Check(false);
		return null;
	}

}
