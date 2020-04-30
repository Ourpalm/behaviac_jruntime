package org.gof.behaviac.tests;

import org.gof.behaviac.AgentMeta;
import org.gof.behaviac.BehaviorLoader;
import org.gof.behaviac.Workspace;
import org.junit.Test;

public class TestLoader {

	@Test
	public void Test1() {
		try {
			AgentMeta.SetTotalSignature(2970163408L);
			Workspace.Instance.SetFilePath("I:\\workspace\\behaviac_mt\\integration\\demo_running\\exported");
			AgentMeta.SetBehaviorLoader(new BehaviorLoader() {
			});
			Workspace.Instance.Load("demo_running");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
