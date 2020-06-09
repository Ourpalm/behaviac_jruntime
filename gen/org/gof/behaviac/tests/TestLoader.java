package org.gof.behaviac.tests;

import java.util.ArrayList;

import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorLoader;
import org.gof.behaviac.ClassInfo;
import org.gof.behaviac.EBTStatus;
import org.gof.behaviac.Workspace;
import org.junit.Test;

import loader.BehaviorLoaderImplement;
import testbehaviac.x1.MyAgent2;

public class TestLoader {

	@Test
	public void Test1() {
		try {
			Workspace.Instance.SetFilePath("G:\\workspace\\behaviac_jruntime\\testworkspace\\exported");
			AgentMeta.SetBehaviorLoader(new BehaviorLoaderImplement() {
			});
			var arg = new ArrayList<Short>();
			arg.add(Short.valueOf((short) 100));
			arg.add(Short.valueOf((short) 101));
			arg.add(Short.valueOf((short) 102));

			var count = 1;
			var agent = new MyAgent2();
			agent.btsetcurrent("b1");
			while (agent.btexec() == EBTStatus.BT_RUNNING) {
				System.out.println("running " + System.currentTimeMillis() + " frame=" + agent.GetFrameSinceStartup());
				Thread.sleep(300);
				++count;

				if ((count % 4) == 0) {
					// agent.FireEvent("task1", new Object[] { arg }, new ClassInfo[] { new
					// ClassInfo(true, Short.class) });
//					agent.FireEvent("task2", new Object[] { Integer.valueOf(13131) },
//							new ClassInfo[] { new ClassInfo(Integer.class) });
				}
			}
			
			System.out.println("v15=" + agent._get_v15());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
