
behavior java运行库


1.使用https://github.com/Ourpalm/behaviac.git中的编辑器编辑Agent元数据以及行为树
2.导出，在生成的Agent框架代码中写入自己的业务逻辑
3.在java中加载

```java
// 初始化 
// BehaviorLoaderImplments由工具生成
org.gof.behaviac.Workspace.Instance.SetFilePath("xml行为树文件/");
org.gof.behaviac.AgentMeta.SetBehaviorLoader(new loader.BehaviorLoaderImplments());
org.gof.behaviac.Workspace.Instance.TryInit();
...

// 使用
TestAgent agent = new TestAgent();
agent.btsetcurrent("TestTree1");
agent.btexec();
...

// 热更新
// 自动卸载已经变化的xml对应的行为树，已经创建的不受影响
org.gof.behaviac.Workspace.Instance.UnloadChangedXmls();

```
