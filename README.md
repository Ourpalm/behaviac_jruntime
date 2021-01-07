
behavior java运行库


* 编辑器
 https://github.com/Ourpalm/behaviac.git

* 使用中的编辑器编辑Agent元数据以及行为树

* 导出，在生成的Agent框架代码中写入自己的业务逻辑

* 在java中使用

```java
// 初始化 
// BehaviorLoaderImplments由工具生成
org.gof.behaviac.Workspace.Instance.SetFilePath("xml导出目录/");
org.gof.behaviac.AgentMeta.SetBehaviorLoader(new loader.BehaviorLoaderImplments());
org.gof.behaviac.Workspace.Instance.TryInit();
...

// 使用
// TestAgent由根据生成
TestAgent agent = new TestAgent();
agent.btsetcurrent("TestTree1");
agent.btexec();
...

// 热更新(线程安全)
// 自动卸载已经变化的xml对应的行为树，已经创建的不受影响
org.gof.behaviac.Workspace.Instance.UnloadChangedXmls();

```
