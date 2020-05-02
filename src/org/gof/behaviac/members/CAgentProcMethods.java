package org.gof.behaviac.members;

import org.gof.behaviac.Agent;
import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorTreeTask;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.Debug;
import org.gof.behaviac.utils.Proc1;
import org.gof.behaviac.utils.Proc2;
import org.gof.behaviac.utils.Proc3;
import org.gof.behaviac.utils.Proc4;
import org.gof.behaviac.utils.Proc5;
import org.gof.behaviac.utils.Proc6;
import org.gof.behaviac.utils.Proc7;
import org.gof.behaviac.utils.Proc8;
import org.gof.behaviac.utils.Utils;

public class CAgentProcMethods {
	public class V0 extends CAgentMethodVoidBase {

		Proc1<Agent> _fp;

		public V0(Proc1<Agent> f) {
			_fp = f;
		}

		public V0(V0 rhs) {
			super(rhs);
			_fp = rhs._fp;
		}

		@Override
		public IMethod Clone() {
			return new V0(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 0);
			_instance = instance;
		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V1 extends CAgentMethodVoidBase {
		Proc2<Agent, Object> _fp;
		IInstanceMember _p1;
		ClassInfo _c1;

		public V1(Proc2<Agent, Object> f, ClassInfo c1) {
			_fp = f;
			_c1 = c1;
		}

		public V1(V1 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
		}

		@Override
		public IMethod Clone() {
			return new V1(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 1);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V2 extends CAgentMethodVoidBase {
		Proc3<Agent, Object, Object> _fp;
		IInstanceMember _p1;
		IInstanceMember _p2;
		ClassInfo _c1;
		ClassInfo _c2;

		public V2(Proc3<Agent, Object, Object> f, ClassInfo c1, ClassInfo c2) {
			_fp = f;
			_c1 = c1;
			_c2 = c2;
		}

		public V2(V2 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
			_c2 = rhs._c2;
		}

		@Override
		public IMethod Clone() {
			return new V2(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 2);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
			_p2 = AgentMeta.ParseProperty(paramStrs[1], _c2);
		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1, _p2);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V3 extends CAgentMethodVoidBase {
		Proc4<Agent, Object, Object, Object> _fp;
		IInstanceMember _p1;
		IInstanceMember _p2;
		IInstanceMember _p3;
		ClassInfo _c1;
		ClassInfo _c2;
		ClassInfo _c3;

		public V3(Proc4<Agent, Object, Object, Object> f, ClassInfo c1, ClassInfo c2, ClassInfo c3) {
			_fp = f;
			_c1 = c1;
			_c2 = c2;
			_c3 = c3;
		}

		public V3(V3 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
			_c2 = rhs._c2;
			_c3 = rhs._c3;
		}

		@Override
		public IMethod Clone() {
			return new V3(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 3);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
			_p2 = AgentMeta.ParseProperty(paramStrs[1], _c2);
			_p3 = AgentMeta.ParseProperty(paramStrs[2], _c3);
		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1, _p2, _p3);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V4 extends CAgentMethodVoidBase {
		Proc5<Agent, Object, Object, Object, Object> _fp;
		IInstanceMember _p1;
		IInstanceMember _p2;
		IInstanceMember _p3;
		IInstanceMember _p4;
		ClassInfo _c1;
		ClassInfo _c2;
		ClassInfo _c3;
		ClassInfo _c4;

		public V4(Proc5<Agent, Object, Object, Object, Object> f, ClassInfo c1, ClassInfo c2, ClassInfo c3,
				ClassInfo c4) {
			_fp = f;
			_c1 = c1;
			_c2 = c2;
			_c3 = c3;
			_c4 = c4;
		}

		public V4(V4 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
			_c2 = rhs._c2;
			_c3 = rhs._c3;
			_c4 = rhs._c4;
		}

		@Override
		public IMethod Clone() {
			return new V4(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 4);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
			_p2 = AgentMeta.ParseProperty(paramStrs[1], _c2);
			_p3 = AgentMeta.ParseProperty(paramStrs[2], _c3);
			_p4 = AgentMeta.ParseProperty(paramStrs[3], _c4);
		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1, _p2, _p3, _p4);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V5 extends CAgentMethodVoidBase {
		Proc6<Agent, Object, Object, Object, Object, Object> _fp;
		IInstanceMember _p1;
		IInstanceMember _p2;
		IInstanceMember _p3;
		IInstanceMember _p4;
		IInstanceMember _p5;
		ClassInfo _c1;
		ClassInfo _c2;
		ClassInfo _c3;
		ClassInfo _c4;
		ClassInfo _c5;

		public V5(Proc6<Agent, Object, Object, Object, Object, Object> f, ClassInfo c1, ClassInfo c2, ClassInfo c3,
				ClassInfo c4, ClassInfo c5) {
			_fp = f;
			_c1 = c1;
			_c2 = c2;
			_c3 = c3;
			_c4 = c4;
			_c5 = c5;
		}

		public V5(V5 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
			_c2 = rhs._c2;
			_c3 = rhs._c3;
			_c4 = rhs._c4;
			_c5 = rhs._c5;
		}

		@Override
		public IMethod Clone() {
			return new V5(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 5);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
			_p2 = AgentMeta.ParseProperty(paramStrs[1], _c2);
			_p3 = AgentMeta.ParseProperty(paramStrs[2], _c3);
			_p4 = AgentMeta.ParseProperty(paramStrs[3], _c4);
			_p5 = AgentMeta.ParseProperty(paramStrs[4], _c5);

		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1, _p2, _p3, _p4, _p5);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V6 extends CAgentMethodVoidBase {
		Proc7<Agent, Object, Object, Object, Object, Object, Object> _fp;
		IInstanceMember _p1;
		IInstanceMember _p2;
		IInstanceMember _p3;
		IInstanceMember _p4;
		IInstanceMember _p5;
		IInstanceMember _p6;
		ClassInfo _c1;
		ClassInfo _c2;
		ClassInfo _c3;
		ClassInfo _c4;
		ClassInfo _c5;
		ClassInfo _c6;

		public V6(Proc7<Agent, Object, Object, Object, Object, Object, Object> f, ClassInfo c1, ClassInfo c2,
				ClassInfo c3, ClassInfo c4, ClassInfo c5, ClassInfo c6) {
			_fp = f;
			_c1 = c1;
			_c2 = c2;
			_c3 = c3;
			_c4 = c4;
			_c5 = c5;
			_c6 = c6;
		}

		public V6(V6 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
			_c2 = rhs._c2;
			_c3 = rhs._c3;
			_c4 = rhs._c4;
			_c5 = rhs._c5;
			_c6 = rhs._c6;
		}

		@Override
		public IMethod Clone() {
			return new V6(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 6);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
			_p2 = AgentMeta.ParseProperty(paramStrs[1], _c2);
			_p3 = AgentMeta.ParseProperty(paramStrs[2], _c3);
			_p4 = AgentMeta.ParseProperty(paramStrs[3], _c4);
			_p5 = AgentMeta.ParseProperty(paramStrs[4], _c5);
			_p6 = AgentMeta.ParseProperty(paramStrs[5], _c6);

		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1, _p2, _p3, _p4, _p5, _p6);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}

	public class V7 extends CAgentMethodVoidBase {
		Proc8<Agent, Object, Object, Object, Object, Object, Object, Object> _fp;
		IInstanceMember _p1;
		IInstanceMember _p2;
		IInstanceMember _p3;
		IInstanceMember _p4;
		IInstanceMember _p5;
		IInstanceMember _p6;
		IInstanceMember _p7;
		ClassInfo _c1;
		ClassInfo _c2;
		ClassInfo _c3;
		ClassInfo _c4;
		ClassInfo _c5;
		ClassInfo _c6;
		ClassInfo _c7;

		public V7(Proc8<Agent, Object, Object, Object, Object, Object, Object, Object> f, ClassInfo c1, ClassInfo c2,
				ClassInfo c3, ClassInfo c4, ClassInfo c5, ClassInfo c6, ClassInfo c7) {
			_fp = f;
			_c1 = c1;
			_c2 = c2;
			_c3 = c3;
			_c4 = c4;
			_c5 = c5;
			_c6 = c6;
			_c7 = c7;
		}

		public V7(V7 rhs) {
			super(rhs);
			_fp = rhs._fp;
			_c1 = rhs._c1;
			_c2 = rhs._c2;
			_c3 = rhs._c3;
			_c4 = rhs._c4;
			_c5 = rhs._c5;
			_c6 = rhs._c6;
			_c7 = rhs._c7;
		}

		@Override
		public IMethod Clone() {
			return new V7(this);
		}

		@Override
		public void Load(String instance, String[] paramStrs) {
			Debug.Check(paramStrs.length == 7);

			_instance = instance;
			_p1 = AgentMeta.ParseProperty(paramStrs[0], _c1);
			_p2 = AgentMeta.ParseProperty(paramStrs[1], _c2);
			_p3 = AgentMeta.ParseProperty(paramStrs[2], _c3);
			_p4 = AgentMeta.ParseProperty(paramStrs[3], _c4);
			_p5 = AgentMeta.ParseProperty(paramStrs[4], _c5);
			_p6 = AgentMeta.ParseProperty(paramStrs[5], _c6);
			_p7 = AgentMeta.ParseProperty(paramStrs[6], _c7);

		}

		@Override
		public void Run(Agent self) {
			Agent agent = Utils.GetParentAgent(self, _instance);
			_fp.run(agent, _p1, _p2, _p3, _p4, _p5, _p6, _p7);
		}

		@Override
		public void SetTaskParams(Agent self, BehaviorTreeTask treeTask) {
			super.SetTaskParams(self, treeTask);
		}
	}
}