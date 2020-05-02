﻿// ---------------------------------------------------------------------
// THIS FILE IS AUTO-GENERATED BY BEHAVIAC DESIGNER, SO PLEASE DON'T MODIFY IT BY YOURSELF!
// ---------------------------------------------------------------------

package loader;
import org.gof.behaviac.BehaviorLoader;
import org.gof.behaviac.*;


public class BehaviorLoaderImplement extends BehaviorLoader
{

	@Override
	public boolean Load() {{
		AgentMeta.SetTotalSignature(3208881645);

		{
			AgentMeta meta;

			// behaviac.Agent
			meta = new AgentMeta(24743406);
			AgentMeta.RegisterMeta(2436498804, meta);
			meta.RegisterStaticMethod(behaviac.Agent.CreateStaticMethod_LOGMESSAGE());
			meta.RegisterStaticMethod(behaviac.Agent.CreateStaticMethod_VECTORADD());
			meta.RegisterStaticMethod(behaviac.Agent.CreateStaticMethod_VECTORCLEAR());
			meta.RegisterStaticMethod(behaviac.Agent.CreateStaticMethod_VECTORCONTAINS());
			meta.RegisterStaticMethod(behaviac.Agent.CreateStaticMethod_VECTORLENGTH());
			meta.RegisterStaticMethod(behaviac.Agent.CreateStaticMethod_VECTORREMOVE());
		}
		{
			AgentMeta meta;

			// org.gof.worldsrv.MyAgent
			meta = new AgentMeta(804018510);
			AgentMeta.RegisterMeta(467449450, meta);
			meta.RegisterStaticProperty(org.gof.worldsrv.MyAgent.CreateStaticProperty_S1());
			meta.RegisterStaticProperty(org.gof.worldsrv.MyAgent.CreateStaticProperty_S2());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V1());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V11());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V11());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V12());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V12());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V13());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V14());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V15());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V16());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V2());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V3());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V4());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V5());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V6());
			meta.RegisterMemberProperty(org.gof.worldsrv.MyAgent.CreateMemberProperty_V7());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_LOGMESSAGE());
			meta.RegisterMemberMethod(org.gof.worldsrv.MyAgent.CreateMemberMethod_M1());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_STATICMETHOD1());
			meta.RegisterMethod(4058224632, new CAgentMethodVoid<ArrayList<Short>>(delegate(Agent self, ArrayList<Short> v1) { }) /* task1 */);
			meta.RegisterMemberMethod(org.gof.worldsrv.MyAgent.CreateMemberMethod_TESTM2());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORADD());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORCLEAR());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORCONTAINS());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORLENGTH());
			meta.RegisterStaticMethod(org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORREMOVE());
		}

		AgentMeta.Register("behaviac.Agent",behaviac.Agent.class);
		AgentMeta.Register("org.gof.worldsrv.MyAgent",org.gof.worldsrv.MyAgent.class);
		AgentMeta.Register("org.gof.worldsrv.EnumTest", org.gof.worldsrv.EnumTest.class);
		AgentMeta.Register("org.gof.worldsrv.StructTest", org.gof.worldsrv.StructTest.class);
		AgentMeta.Register("org.gof.worldsrv.Haha", org.gof.worldsrv.Haha.class);
		return true;
	}

	@Override
	public boolean UnLoad()
	{
		AgentMeta.UnRegister("behaviac.Agent");
		AgentMeta.UnRegister("org.gof.worldsrv.MyAgent");
		AgentMeta.UnRegister("org.gof.worldsrv.EnumTest");
		AgentMeta.UnRegister("org.gof.worldsrv.StructTest");
		AgentMeta.UnRegister("org.gof.worldsrv.Haha");
		return true;
	}
}
