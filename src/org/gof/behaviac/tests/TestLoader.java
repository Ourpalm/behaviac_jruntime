package org.gof.behaviac.tests;

import org.gof.behaviac.Workspace;
import org.junit.Test;

public class TestLoader {
	
	@Test
	public void Test1() {
		Workspace.Instance.SetFilePath("G:\\workspace\\behaviac_mt\\test\\demo_running\\behaviac\\exported");
		Workspace.Instance.Load("demo_running");
	}
}
