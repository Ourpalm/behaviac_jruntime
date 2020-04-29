package org.gof.behaviac.tests;

import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorLoader;
import org.gof.behaviac.Workspace;
import org.junit.Test;

public class TestLoader {

	@Test
	public void Test1() {
		try {
			Workspace.Instance.SetFilePath("G:\\workspace\\behaviac_mt\\test\\demo_running\\behaviac\\exported");
			AgentMeta.SetBehaviorLoader(new BehaviorLoader() {
			});
			Workspace.Instance.Load("demo_running");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
