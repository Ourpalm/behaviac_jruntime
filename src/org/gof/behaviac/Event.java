package org.gof.behaviac;

import java.util.List;
import java.util.Map;

import org.gof.behaviac.conditions.ConditionBase;
import org.gof.behaviac.members.IInstantiatedVariable;
import org.gof.behaviac.members.IMethod;
import org.gof.behaviac.utils.Utils;

@RegisterableNode
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

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Task") {
				var r = AgentMeta.ParseMethod(p.value, m_eventName);
				this.m_event = r.value1;
				this.m_eventName = r.value2;
			} else if (p.name == "ReferenceFilename") {
				this.m_referencedBehaviorPath = p.value;

				if (Config.PreloadBehaviors) {
					BehaviorTree behaviorTree = Workspace.Instance.LoadBehaviorTree(this.m_referencedBehaviorPath);
					Debug.Check(behaviorTree != null);
				}
			} else if (p.name == "TriggeredOnce") {
				this.m_bTriggeredOnce = (p.value == "true");
			} else if (p.name == "TriggerMode") {
				if (p.value == "Transfer") {
					this.m_triggerMode = TriggerMode.TM_Transfer;
				} else if (p.value == "Return") {
					this.m_triggerMode = TriggerMode.TM_Return;
				} else {
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
		if (!Utils.IsNullOrEmpty(this.m_referencedBehaviorPath)) {
			if (pAgent != null) {
				TriggerMode tm = this.GetTriggerMode();

				pAgent.bteventtree(pAgent, this.m_referencedBehaviorPath, tm);

				Debug.Check(pAgent.GetCurrentTreeTask() != null);
				pAgent.GetCurrentTreeTask().AddVariables(eventParams);

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
