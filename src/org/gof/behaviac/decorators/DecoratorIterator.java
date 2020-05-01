package org.gof.behaviac.decorators;

import java.util.List;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTask;
import org.gof.behaviac.Debug;
import org.gof.behaviac.DecoratorNode;
import org.gof.behaviac.RegisterableNode;
import org.gof.behaviac.property_t;
import org.gof.behaviac.members.IInstanceMember;
import org.gof.behaviac.utils.Tuple2;

@RegisterableNode
public class DecoratorIterator extends DecoratorNode {
	protected IInstanceMember m_opl;
	protected IInstanceMember m_opr;

	@Override
	protected void load(int version, String agentType, List<property_t> properties) {
		// TODO Auto-generated method stub
		super.load(version, agentType, properties);

		for (int i = 0; i < properties.size(); ++i) {
			property_t p = properties.get(i);

			if (p.name == "Opl") {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_opl = AgentMeta.ParseProperty(p.value);
				} else {
					Debug.Check(false);
				}
			} else if (p.name == "Opr") {
				int pParenthesis = p.value.indexOf('(');

				if (pParenthesis == -1) {
					this.m_opr = AgentMeta.ParseProperty(p.value);
				} else {
					this.m_opr = AgentMeta.ParseMethod(p.value);
				}
			}
		}
	}

	@Override
	public boolean IsValid(Agent pAgent, BehaviorTask pTask) {
		if (!(pTask.GetNode() instanceof DecoratorIterator)) {
			return false;
		}
		return super.IsValid(pAgent, pTask);
	}

	public Tuple2<Boolean, Integer> IterateIt(Agent pAgent, int index, /* ref */ int count) {
		if (this.m_opl != null && this.m_opr != null) {
			count = this.m_opr.GetCount(pAgent);

			if (index >= 0 && index < count) {
				this.m_opl.SetValue(pAgent, this.m_opr, index);

				return new Tuple2<>(true, count);
			}
		} else {
			Debug.Check(false);
		}

		return new Tuple2<>(false, count);
	}

	@Override
	protected BehaviorTask createTask() {
		Debug.Check(false);
        return null;
	}

}
