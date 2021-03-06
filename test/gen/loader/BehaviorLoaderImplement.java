﻿// ---------------------------------------------------------------------
// THIS FILE IS AUTO-GENERATED BY BEHAVIAC DESIGNER, SO PLEASE DON'T MODIFY IT BY YOURSELF!
// ---------------------------------------------------------------------

package loader;
import org.gof.behaviac.BehaviorLoader;
import org.gof.behaviac.*;
import org.gof.behaviac.members.*;
import org.gof.behaviac.utils.StringUtils;


public class BehaviorLoaderImplement extends BehaviorLoader
{
		public class CInstanceConst_org_gof_worldsrv_StructTest extends CInstanceConst
		{
			IInstanceMember _sv1;
			IInstanceMember _sv2;
			IInstanceMember _sv4;

			public CInstanceConst_org_gof_worldsrv_StructTest(String typeName, String valueStr)
			{
				super(new ClassInfo(org.gof.worldsrv.StructTest.class),typeName,valueStr);
				var paramStrs = StringUtils.SplitTokensForStruct(valueStr);
				Debug.Check(paramStrs != null && paramStrs.size() == 3);

				_sv1 = AgentMeta.ParseProperty(paramStrs.get(0),new ClassInfo(Integer.class));
				_sv2 = AgentMeta.ParseProperty(paramStrs.get(1),new ClassInfo(Double.class));
				_sv4 = AgentMeta.ParseProperty(paramStrs.get(2),new ClassInfo(Integer.class));
			}

			@Override
			public void Run(Agent self)
			{
				Debug.Check(_sv1 != null);
				Debug.Check(_sv2 != null);
				Debug.Check(_sv4 != null);

				((org.gof.worldsrv.StructTest)_value).sv1 = (Integer)_sv1.GetValueObject(self);
				((org.gof.worldsrv.StructTest)_value).sv2 = (Double)_sv2.GetValueObject(self);
				((org.gof.worldsrv.StructTest)_value).sv4 = (Integer)_sv4.GetValueObject(self);
			}
		};

		public class CInstanceConst_org_gof_worldsrv_Haha extends CInstanceConst
		{
			IInstanceMember _sv1;
			IInstanceMember _sv2;
			IInstanceMember _xx3;

			public CInstanceConst_org_gof_worldsrv_Haha(String typeName, String valueStr)
			{
				super(new ClassInfo(org.gof.worldsrv.Haha.class),typeName,valueStr);
				var paramStrs = StringUtils.SplitTokensForStruct(valueStr);
				Debug.Check(paramStrs != null && paramStrs.size() == 3);

				_sv1 = AgentMeta.ParseProperty(paramStrs.get(0),new ClassInfo(Integer.class));
				_sv2 = AgentMeta.ParseProperty(paramStrs.get(1),new ClassInfo(Double.class));
				_xx3 = AgentMeta.ParseProperty(paramStrs.get(2),new ClassInfo(Double.class));
			}

			@Override
			public void Run(Agent self)
			{
				Debug.Check(_sv1 != null);
				Debug.Check(_sv2 != null);
				Debug.Check(_xx3 != null);

				((org.gof.worldsrv.Haha)_value).sv1 = (Integer)_sv1.GetValueObject(self);
				((org.gof.worldsrv.Haha)_value).sv2 = (Double)_sv2.GetValueObject(self);
				((org.gof.worldsrv.Haha)_value).xx3 = (Double)_xx3.GetValueObject(self);
			}
		};


	@Override
	public boolean Load() {
		AgentMeta.SetTotalSignature(1920889879L);

		{
			AgentMeta meta;

			// behaviac.Agent
			meta = new AgentMeta(1044849142L);
			AgentMeta.RegisterMeta(750752878L, meta);//org.gof.behaviac.Agent
			meta.RegisterStaticMethod(1396400587L,org.gof.behaviac.Agent.CreateStaticMethod_LOGMESSAGE());
			meta.RegisterStaticMethod(197161586L,org.gof.behaviac.Agent.CreateStaticMethod_VECTORADD());
			meta.RegisterStaticMethod(1810246298L,org.gof.behaviac.Agent.CreateStaticMethod_VECTORCLEAR());
			meta.RegisterStaticMethod(2025304058L,org.gof.behaviac.Agent.CreateStaticMethod_VECTORCONTAINS());
			meta.RegisterStaticMethod(348439993L,org.gof.behaviac.Agent.CreateStaticMethod_VECTORLENGTH());
			meta.RegisterStaticMethod(2042517019L,org.gof.behaviac.Agent.CreateStaticMethod_VECTORREMOVE());
		}
		{
			AgentMeta meta;

			// org.gof.worldsrv.MyAgent
			meta = new AgentMeta(1404767707L);
			AgentMeta.RegisterMeta(646043850L, meta);//org.gof.worldsrv.MyAgent
			meta.RegisterStaticProperty(15114L,org.gof.worldsrv.MyAgent.CreateStaticProperty_S1());
			meta.RegisterStaticProperty(15115L,org.gof.worldsrv.MyAgent.CreateStaticProperty_S2());
			meta.RegisterMemberProperty(15507L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V1());
			meta.RegisterMemberProperty(2031466L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V11());
			meta.RegisterMemberProperty(502261672L,org.gof.worldsrv.MyAgent.CreateMemberArrayProperty_V11());
			meta.RegisterMemberProperty(2031467L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V12());
			meta.RegisterMemberProperty(502278833L,org.gof.worldsrv.MyAgent.CreateMemberArrayProperty_V12());
			meta.RegisterMemberProperty(2031468L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V13());
			meta.RegisterMemberProperty(2031469L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V14());
			meta.RegisterMemberProperty(2031470L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V15());
			meta.RegisterMemberProperty(2031471L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V16());
			meta.RegisterMemberProperty(15508L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V2());
			meta.RegisterMemberProperty(15509L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V3());
			meta.RegisterMemberProperty(15510L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V4());
			meta.RegisterMemberProperty(15511L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V5());
			meta.RegisterMemberProperty(15512L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V6());
			meta.RegisterMemberProperty(15513L,org.gof.worldsrv.MyAgent.CreateMemberProperty_V7());
			meta.RegisterStaticMethod(1396400587L,org.gof.worldsrv.MyAgent.CreateStaticMethod_LOGMESSAGE());
			meta.RegisterMemberMethod(14328L,org.gof.worldsrv.MyAgent.CreateMemberMethod_M1());
			meta.RegisterStaticMethod(1738753698L,org.gof.worldsrv.MyAgent.CreateStaticMethod_STATICMETHOD1());
			meta.RegisterMemberMethod(1952612959L,org.gof.worldsrv.MyAgent.CreateMemberMethod_TESTM2());
			meta.RegisterStaticMethod(197161586L,org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORADD());
			meta.RegisterStaticMethod(1810246298L,org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORCLEAR());
			meta.RegisterStaticMethod(2025304058L,org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORCONTAINS());
			meta.RegisterStaticMethod(348439993L,org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORLENGTH());
			meta.RegisterStaticMethod(2042517019L,org.gof.worldsrv.MyAgent.CreateStaticMethod_VECTORREMOVE());
		}
		{
			AgentMeta meta;

			// testbehaviac.x1.MyAgent2
			meta = new AgentMeta(121652052L);
			AgentMeta.RegisterMeta(1464643067L, meta);//testbehaviac.x1.MyAgent2
			meta.RegisterStaticProperty(1683652181L,testbehaviac.x1.MyAgent2.CreateStaticProperty_HAHA_Z2());
			meta.RegisterStaticProperty(910089963L,testbehaviac.x1.MyAgent2.CreateStaticArrayProperty_HAHA_Z2());
			meta.RegisterStaticProperty(15114L,testbehaviac.x1.MyAgent2.CreateStaticProperty_S1());
			meta.RegisterStaticProperty(15115L,testbehaviac.x1.MyAgent2.CreateStaticProperty_S2());
			meta.RegisterMemberProperty(1593209488L,testbehaviac.x1.MyAgent2.CreateMemberProperty_TESTHAHA());
			meta.RegisterMemberProperty(15507L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V1());
			meta.RegisterMemberProperty(2031466L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V11());
			meta.RegisterMemberProperty(502261672L,testbehaviac.x1.MyAgent2.CreateMemberArrayProperty_V11());
			meta.RegisterMemberProperty(2031467L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V12());
			meta.RegisterMemberProperty(502278833L,testbehaviac.x1.MyAgent2.CreateMemberArrayProperty_V12());
			meta.RegisterMemberProperty(2031468L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V13());
			meta.RegisterMemberProperty(2031469L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V14());
			meta.RegisterMemberProperty(2031470L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V15());
			meta.RegisterMemberProperty(2031471L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V16());
			meta.RegisterMemberProperty(15508L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V2());
			meta.RegisterMemberProperty(15509L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V3());
			meta.RegisterMemberProperty(15510L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V4());
			meta.RegisterMemberProperty(15511L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V5());
			meta.RegisterMemberProperty(15512L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V6());
			meta.RegisterMemberProperty(15513L,testbehaviac.x1.MyAgent2.CreateMemberProperty_V7());
			meta.RegisterMemberProperty(1247657123L,testbehaviac.x1.MyAgent2.CreateMemberProperty_VSTRUCT());
			meta.RegisterMemberProperty(271846009L,testbehaviac.x1.MyAgent2.CreateMemberProperty_XXX1());
			meta.RegisterStaticMethod(1396400587L,testbehaviac.x1.MyAgent2.CreateStaticMethod_LOGMESSAGE());
			meta.RegisterMemberMethod(14328L,testbehaviac.x1.MyAgent2.CreateMemberMethod_M1());
			meta.RegisterMemberMethod(206889707L,testbehaviac.x1.MyAgent2.CreateMemberMethod_METHODHAHA());
			meta.RegisterMemberMethod(1332747891L,testbehaviac.x1.MyAgent2.CreateMemberMethod_METHODHAHA2());
			meta.RegisterStaticMethod(1738753698L,testbehaviac.x1.MyAgent2.CreateStaticMethod_STATICMETHOD1());
			meta.RegisterMemberMethod(22304877L,testbehaviac.x1.MyAgent2.CreateMemberMethod_TASK2());
			meta.RegisterMemberMethod(1952612959L,testbehaviac.x1.MyAgent2.CreateMemberMethod_TESTM2());
			meta.RegisterStaticMethod(197161586L,testbehaviac.x1.MyAgent2.CreateStaticMethod_VECTORADD());
			meta.RegisterStaticMethod(1810246298L,testbehaviac.x1.MyAgent2.CreateStaticMethod_VECTORCLEAR());
			meta.RegisterStaticMethod(2025304058L,testbehaviac.x1.MyAgent2.CreateStaticMethod_VECTORCONTAINS());
			meta.RegisterStaticMethod(348439993L,testbehaviac.x1.MyAgent2.CreateStaticMethod_VECTORLENGTH());
			meta.RegisterStaticMethod(2042517019L,testbehaviac.x1.MyAgent2.CreateStaticMethod_VECTORREMOVE());
			meta.RegisterMemberMethod(276376776L,testbehaviac.x1.MyAgent2.CreateMemberMethod_ZZZ2());
		}
		{
			AgentMeta meta;

			// org.gof.worldsrv.SandEventBeacon
			meta = new AgentMeta(224246714L);
			AgentMeta.RegisterMeta(809683623L, meta);//org.gof.worldsrv.SandEventBeacon
			meta.RegisterMemberProperty(307498538L,org.gof.worldsrv.SandEventBeacon.CreateMemberProperty_GROUP1());
			meta.RegisterMemberProperty(615099496L,org.gof.worldsrv.SandEventBeacon.CreateMemberArrayProperty_GROUP1());
			meta.RegisterMemberProperty(266954404L,org.gof.worldsrv.SandEventBeacon.CreateMemberProperty_VARS());
			meta.RegisterMemberMethod(245805620L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_CREATETRIGGER());
			meta.RegisterMemberMethod(1786529168L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_HASHUMANS());
			meta.RegisterStaticMethod(1396400587L,org.gof.worldsrv.SandEventBeacon.CreateStaticMethod_LOGMESSAGE());
			meta.RegisterMemberMethod(1197111957L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_SENDAWARD());
			meta.RegisterMemberMethod(161333911L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_SETHUMANTARGETS());
			meta.RegisterMemberMethod(2029564917L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_SETVAR());
			meta.RegisterMemberMethod(212928523L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_SPAWNMONSTER());
			meta.RegisterMemberMethod(2123832787L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_SPAWNMONSTER2());
			meta.RegisterStaticMethod(197161586L,org.gof.worldsrv.SandEventBeacon.CreateStaticMethod_VECTORADD());
			meta.RegisterStaticMethod(1810246298L,org.gof.worldsrv.SandEventBeacon.CreateStaticMethod_VECTORCLEAR());
			meta.RegisterStaticMethod(2025304058L,org.gof.worldsrv.SandEventBeacon.CreateStaticMethod_VECTORCONTAINS());
			meta.RegisterStaticMethod(348439993L,org.gof.worldsrv.SandEventBeacon.CreateStaticMethod_VECTORLENGTH());
			meta.RegisterStaticMethod(2042517019L,org.gof.worldsrv.SandEventBeacon.CreateStaticMethod_VECTORREMOVE());
			meta.RegisterMemberMethod(2139864962L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_WAITMONSTERKILLED());
			meta.RegisterMemberMethod(1149435832L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_WAITMONSTERKILLED2());
			meta.RegisterMemberMethod(518602921L,org.gof.worldsrv.SandEventBeacon.CreateMemberMethod_WAITSIGNAL());
		}

		AgentMeta.Register("org.gof.behaviac.Agent",org.gof.behaviac.Agent.class);
		AgentMeta.Register("org.gof.worldsrv.MyAgent",org.gof.worldsrv.MyAgent.class);
		AgentMeta.Register("testbehaviac.x1.MyAgent2",testbehaviac.x1.MyAgent2.class);
		AgentMeta.Register("org.gof.worldsrv.SandEventBeacon",org.gof.worldsrv.SandEventBeacon.class);
		AgentMeta.Register("org.gof.worldsrv.EnumTest", org.gof.worldsrv.EnumTest.class);
		AgentMeta.Register("org.gof.worldsrv.StructTest", org.gof.worldsrv.StructTest.class);
		AgentMeta.Register("org.gof.worldsrv.Haha", org.gof.worldsrv.Haha.class);
		return true;
	}

	@Override
	public boolean UnLoad()
	{
		AgentMeta.UnRegister("org.gof.behaviac.Agent");
		AgentMeta.UnRegister("org.gof.worldsrv.MyAgent");
		AgentMeta.UnRegister("testbehaviac.x1.MyAgent2");
		AgentMeta.UnRegister("org.gof.worldsrv.SandEventBeacon");
		AgentMeta.UnRegister("org.gof.worldsrv.EnumTest");
		AgentMeta.UnRegister("org.gof.worldsrv.StructTest");
		AgentMeta.UnRegister("org.gof.worldsrv.Haha");
		return true;
	}
}
