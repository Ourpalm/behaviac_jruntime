﻿// ---------------------------------------------------------------------
// THIS FILE IS AUTO-GENERATED BY BEHAVIAC DESIGNER, SO PLEASE DON'T MODIFY IT BY YOURSELF!
// Export file: exported/behaviac_generated/behaviors/generated_behaviors.cs
// ---------------------------------------------------------------------

using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;

namespace behaviac
{
	// Source file: b1

	[behaviac.GeneratedTypeMetaInfo()]
	class Event_bt_b1_attach8 : behaviac.Event
	{
		public Event_bt_b1_attach8()
		{
			this.Initialize("Self.testbehaviac::x1::MyAgent2::task2(0)", "b2", TriggerMode.TM_Return, true);
		}
		public void Initialize(string eventName, string referencedBehavior, TriggerMode mode, bool once)
		{
			this.m_event = AgentMeta.ParseMethod(eventName, ref this.m_eventName);
			this.m_referencedBehaviorPath = referencedBehavior;
			this.m_triggerMode = mode;
			this.m_bTriggeredOnce = once;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class DecoratorLoop_bt_b1_node4 : behaviac.DecoratorLoop
	{
		public DecoratorLoop_bt_b1_node4()
		{
			m_bDecorateWhenChildEnds = true;
			m_bDoneWithinFrame = false;
		}
		protected override int GetCount(Agent pAgent)
		{
			return 5;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Assignment_bt_b1_node6 : behaviac.Assignment
	{
		public Assignment_bt_b1_node6()
		{
			opr = new org.gof.worldsrv.StructTest();
			opr.sv1 = 234;
			opr.sv2 = 24234.4453125;
			opr.sv4 = 121;
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			EBTStatus result = EBTStatus.BT_SUCCESS;
			pAgent.SetVariable<org.gof.worldsrv.StructTest>("vstruct", 1247657123u, opr);
			return result;
		}
		org.gof.worldsrv.StructTest opr;
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class SelectorProbability_bt_b1_node10 : behaviac.SelectorProbability
	{
		public SelectorProbability_bt_b1_node10()
		{
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class DecoratorWeight_bt_b1_node13 : behaviac.DecoratorWeight
	{
		public DecoratorWeight_bt_b1_node13()
		{
			m_bDecorateWhenChildEnds = false;
		}
		protected override int GetWeight(Agent pAgent)
		{
			return 1;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Assignment_bt_b1_node14 : behaviac.Assignment
	{
		public Assignment_bt_b1_node14()
		{
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			EBTStatus result = EBTStatus.BT_SUCCESS;
			string opr = "zzzzzzzzzzzzzzzzzzzz";
			pAgent.SetVariable<string>("v16", 2031471u, opr);
			return result;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b1_node23 : behaviac.Action
	{
		public Action_bt_b1_node23()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			behaviac.Agent.LogMessage((string)AgentMetaVisitor.GetProperty(pAgent, "v16"));
			return EBTStatus.BT_SUCCESS;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class DecoratorWeight_bt_b1_node11 : behaviac.DecoratorWeight
	{
		public DecoratorWeight_bt_b1_node11()
		{
			m_bDecorateWhenChildEnds = false;
		}
		protected override int GetWeight(Agent pAgent)
		{
			return 1;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Assignment_bt_b1_node12 : behaviac.Assignment
	{
		public Assignment_bt_b1_node12()
		{
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			EBTStatus result = EBTStatus.BT_SUCCESS;
			string opr = "xxxxxxxxxxxxx";
			pAgent.SetVariable<string>("v16", 2031471u, opr);
			return result;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b1_node21 : behaviac.Action
	{
		public Action_bt_b1_node21()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			behaviac.Agent.LogMessage((string)AgentMetaVisitor.GetProperty(pAgent, "v16"));
			return EBTStatus.BT_SUCCESS;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Condition_bt_b1_node16 : behaviac.Condition
	{
		public Condition_bt_b1_node16()
		{
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			int opl = (int)AgentMetaVisitor.GetProperty(pAgent, "v3");
			int opr = 0;
			bool op = opl <= opr;
			return op ? EBTStatus.BT_SUCCESS : EBTStatus.BT_FAILURE;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Assignment_bt_b1_node5 : behaviac.Assignment
	{
		public Assignment_bt_b1_node5()
		{
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			EBTStatus result = EBTStatus.BT_SUCCESS;
			string opr = "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh";
			pAgent.SetVariable<string>("v16", 2031471u, opr);
			return result;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b1_node1 : behaviac.Action
	{
		public Action_bt_b1_node1()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			behaviac.Agent.LogMessage((string)AgentMetaVisitor.GetProperty(pAgent, "v16"));
			return EBTStatus.BT_SUCCESS;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class End_bt_b1_node17 : behaviac.End
	{
		public End_bt_b1_node17()
		{
		}
		protected override EBTStatus GetStatus(Agent pAgent)
		{
			return behaviac.EBTStatus.BT_SUCCESS;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b1_node7 : behaviac.Action
	{
		public Action_bt_b1_node7()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
			method_p0 = new List<org.gof.worldsrv.Haha>();
			method_p0.Capacity = 1;
			org.gof.worldsrv.Haha method_p0_item0;
			method_p0_item0 = new org.gof.worldsrv.Haha();
			method_p0_item0.sv1 = 2147483647;
			method_p0_item0.sv2 = 4234;
			method_p0_item0.xx3 = 111;
			method_p0.Add(method_p0_item0);
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			((testbehaviac.x1.MyAgent2)pAgent).methodHaha2(method_p0);
			return EBTStatus.BT_SUCCESS;
		}
		List<org.gof.worldsrv.Haha> method_p0;
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b1_node2 : behaviac.Action
	{
		public Action_bt_b1_node2()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
			method_p0 = new org.gof.worldsrv.StructTest();
			method_p0.sv1 = 345;
			method_p0.sv2 = 345345;
			method_p0.sv4 = 4545;
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			((testbehaviac.x1.MyAgent2)pAgent).methodHaha(method_p0);
			return EBTStatus.BT_SUCCESS;
		}
		org.gof.worldsrv.StructTest method_p0;
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Wait_bt_b1_node3 : behaviac.Wait
	{
		public Wait_bt_b1_node3()
		{
		}
		protected override double GetTime(Agent pAgent)
		{
			return 1000f;
		}
	}

	public static class bt_b1
	{
		public static bool build_behavior_tree(BehaviorTree bt)
		{
			bt.SetClassNameString("BehaviorTree");
			bt.SetId(-1);
			bt.SetName("b1");
			bt.IsFSM = false;
#if !BEHAVIAC_RELEASE
			bt.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
			// children
			{
				Sequence node0 = new Sequence();
				node0.SetClassNameString("Sequence");
				node0.SetId(0);
#if !BEHAVIAC_RELEASE
				node0.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
				// attachments
				{
					Event_bt_b1_attach8 attach8 = new Event_bt_b1_attach8();
					attach8.SetClassNameString("Event");
					attach8.SetId(8);
#if !BEHAVIAC_RELEASE
					attach8.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
					node0.Attach(attach8, false, false, false);
					node0.SetHasEvents(node0.HasEvents() | (attach8 is Event));
				}
				bt.AddChild(node0);
				{
					DecoratorLoop_bt_b1_node4 node4 = new DecoratorLoop_bt_b1_node4();
					node4.SetClassNameString("DecoratorLoop");
					node4.SetId(4);
#if !BEHAVIAC_RELEASE
					node4.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
					node0.AddChild(node4);
					{
						Sequence node9 = new Sequence();
						node9.SetClassNameString("Sequence");
						node9.SetId(9);
#if !BEHAVIAC_RELEASE
						node9.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
						node4.AddChild(node9);
						{
							Assignment_bt_b1_node6 node6 = new Assignment_bt_b1_node6();
							node6.SetClassNameString("Assignment");
							node6.SetId(6);
#if !BEHAVIAC_RELEASE
							node6.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
							node9.AddChild(node6);
							node9.SetHasEvents(node9.HasEvents() | node6.HasEvents());
						}
						{
							SelectorProbability_bt_b1_node10 node10 = new SelectorProbability_bt_b1_node10();
							node10.SetClassNameString("SelectorProbability");
							node10.SetId(10);
#if !BEHAVIAC_RELEASE
							node10.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
							node9.AddChild(node10);
							{
								DecoratorWeight_bt_b1_node13 node13 = new DecoratorWeight_bt_b1_node13();
								node13.SetClassNameString("DecoratorWeight");
								node13.SetId(13);
#if !BEHAVIAC_RELEASE
								node13.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
								node10.AddChild(node13);
								{
									Sequence node18 = new Sequence();
									node18.SetClassNameString("Sequence");
									node18.SetId(18);
#if !BEHAVIAC_RELEASE
									node18.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
									node13.AddChild(node18);
									{
										Assignment_bt_b1_node14 node14 = new Assignment_bt_b1_node14();
										node14.SetClassNameString("Assignment");
										node14.SetId(14);
#if !BEHAVIAC_RELEASE
										node14.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
										node18.AddChild(node14);
										node18.SetHasEvents(node18.HasEvents() | node14.HasEvents());
									}
									{
										Action_bt_b1_node23 node23 = new Action_bt_b1_node23();
										node23.SetClassNameString("Action");
										node23.SetId(23);
#if !BEHAVIAC_RELEASE
										node23.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
										node18.AddChild(node23);
										node18.SetHasEvents(node18.HasEvents() | node23.HasEvents());
									}
									node13.SetHasEvents(node13.HasEvents() | node18.HasEvents());
								}
								node10.SetHasEvents(node10.HasEvents() | node13.HasEvents());
							}
							{
								DecoratorWeight_bt_b1_node11 node11 = new DecoratorWeight_bt_b1_node11();
								node11.SetClassNameString("DecoratorWeight");
								node11.SetId(11);
#if !BEHAVIAC_RELEASE
								node11.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
								node10.AddChild(node11);
								{
									Sequence node20 = new Sequence();
									node20.SetClassNameString("Sequence");
									node20.SetId(20);
#if !BEHAVIAC_RELEASE
									node20.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
									node11.AddChild(node20);
									{
										Assignment_bt_b1_node12 node12 = new Assignment_bt_b1_node12();
										node12.SetClassNameString("Assignment");
										node12.SetId(12);
#if !BEHAVIAC_RELEASE
										node12.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
										node20.AddChild(node12);
										node20.SetHasEvents(node20.HasEvents() | node12.HasEvents());
									}
									{
										Action_bt_b1_node21 node21 = new Action_bt_b1_node21();
										node21.SetClassNameString("Action");
										node21.SetId(21);
#if !BEHAVIAC_RELEASE
										node21.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
										node20.AddChild(node21);
										node20.SetHasEvents(node20.HasEvents() | node21.HasEvents());
									}
									node11.SetHasEvents(node11.HasEvents() | node20.HasEvents());
								}
								node10.SetHasEvents(node10.HasEvents() | node11.HasEvents());
							}
							node9.SetHasEvents(node9.HasEvents() | node10.HasEvents());
						}
						{
							IfElse node15 = new IfElse();
							node15.SetClassNameString("IfElse");
							node15.SetId(15);
#if !BEHAVIAC_RELEASE
							node15.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
							node9.AddChild(node15);
							{
								Condition_bt_b1_node16 node16 = new Condition_bt_b1_node16();
								node16.SetClassNameString("Condition");
								node16.SetId(16);
#if !BEHAVIAC_RELEASE
								node16.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
								node15.AddChild(node16);
								node15.SetHasEvents(node15.HasEvents() | node16.HasEvents());
							}
							{
								Sequence node22 = new Sequence();
								node22.SetClassNameString("Sequence");
								node22.SetId(22);
#if !BEHAVIAC_RELEASE
								node22.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
								node15.AddChild(node22);
								{
									Assignment_bt_b1_node5 node5 = new Assignment_bt_b1_node5();
									node5.SetClassNameString("Assignment");
									node5.SetId(5);
#if !BEHAVIAC_RELEASE
									node5.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
									node22.AddChild(node5);
									node22.SetHasEvents(node22.HasEvents() | node5.HasEvents());
								}
								{
									Action_bt_b1_node1 node1 = new Action_bt_b1_node1();
									node1.SetClassNameString("Action");
									node1.SetId(1);
#if !BEHAVIAC_RELEASE
									node1.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
									node22.AddChild(node1);
									node22.SetHasEvents(node22.HasEvents() | node1.HasEvents());
								}
								node15.SetHasEvents(node15.HasEvents() | node22.HasEvents());
							}
							{
								End_bt_b1_node17 node17 = new End_bt_b1_node17();
								node17.SetClassNameString("End");
								node17.SetId(17);
#if !BEHAVIAC_RELEASE
								node17.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
								node15.AddChild(node17);
								node15.SetHasEvents(node15.HasEvents() | node17.HasEvents());
							}
							node9.SetHasEvents(node9.HasEvents() | node15.HasEvents());
						}
						{
							Action_bt_b1_node7 node7 = new Action_bt_b1_node7();
							node7.SetClassNameString("Action");
							node7.SetId(7);
#if !BEHAVIAC_RELEASE
							node7.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
							node9.AddChild(node7);
							node9.SetHasEvents(node9.HasEvents() | node7.HasEvents());
						}
						{
							Action_bt_b1_node2 node2 = new Action_bt_b1_node2();
							node2.SetClassNameString("Action");
							node2.SetId(2);
#if !BEHAVIAC_RELEASE
							node2.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
							node9.AddChild(node2);
							node9.SetHasEvents(node9.HasEvents() | node2.HasEvents());
						}
						{
							Wait_bt_b1_node3 node3 = new Wait_bt_b1_node3();
							node3.SetClassNameString("Wait");
							node3.SetId(3);
#if !BEHAVIAC_RELEASE
							node3.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
							node9.AddChild(node3);
							node9.SetHasEvents(node9.HasEvents() | node3.HasEvents());
						}
						node4.SetHasEvents(node4.HasEvents() | node9.HasEvents());
					}
					node0.SetHasEvents(node0.HasEvents() | node4.HasEvents());
				}
				bt.SetHasEvents(bt.HasEvents() | node0.HasEvents());
			}
			return true;
		}
	}

	// Source file: b2

	[behaviac.GeneratedTypeMetaInfo()]
	class Task_bt_b2_node0 : behaviac.Task
	{
		public Task_bt_b2_node0()
		{
			this.m_task = AgentMeta.ParseMethod("Self.testbehaviac::x1::MyAgent2::task2(0)");
			Debug.Check(this.m_task != null);
			this.m_bHTN = false;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Assignment_bt_b2_node4 : behaviac.Assignment
	{
		public Assignment_bt_b2_node4()
		{
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			EBTStatus result = EBTStatus.BT_SUCCESS;
			Debug.Check(behaviac.Utils.MakeVariableId("_$local_task_param_$_0") == 37332066u);
			int opr = pAgent.GetVariable<int>(37332066u);
			pAgent.SetVariable<int>("v13", 2031468u, opr);
			return result;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b2_node3 : behaviac.Action
	{
		public Action_bt_b2_node3()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			behaviac.Agent.LogMessage((string)AgentMetaVisitor.GetProperty(pAgent, "v16"));
			return EBTStatus.BT_SUCCESS;
		}
	}

	[behaviac.GeneratedTypeMetaInfo()]
	class Action_bt_b2_node2 : behaviac.Action
	{
		public Action_bt_b2_node2()
		{
			this.m_resultOption = EBTStatus.BT_SUCCESS;
			method_p0 = "触发成功!!";
		}
		protected override EBTStatus update_impl(behaviac.Agent pAgent, behaviac.EBTStatus childStatus)
		{
			behaviac.Agent.LogMessage(method_p0);
			return EBTStatus.BT_SUCCESS;
		}
		string method_p0;
	}

	public static class bt_b2
	{
		public static bool build_behavior_tree(BehaviorTree bt)
		{
			bt.SetClassNameString("BehaviorTree");
			bt.SetId(-1);
			bt.SetName("b2");
			bt.IsFSM = false;
#if !BEHAVIAC_RELEASE
			bt.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
			// locals
			bt.AddLocal("testbehaviac::x1::MyAgent2", "int", "_$local_task_param_$_0", "0");
			// children
			{
				Task_bt_b2_node0 node0 = new Task_bt_b2_node0();
				node0.SetClassNameString("Task");
				node0.SetId(0);
#if !BEHAVIAC_RELEASE
				node0.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
				bt.AddChild(node0);
				{
					Sequence node1 = new Sequence();
					node1.SetClassNameString("Sequence");
					node1.SetId(1);
#if !BEHAVIAC_RELEASE
					node1.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
					node0.AddChild(node1);
					{
						Assignment_bt_b2_node4 node4 = new Assignment_bt_b2_node4();
						node4.SetClassNameString("Assignment");
						node4.SetId(4);
#if !BEHAVIAC_RELEASE
						node4.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
						node1.AddChild(node4);
						node1.SetHasEvents(node1.HasEvents() | node4.HasEvents());
					}
					{
						Action_bt_b2_node3 node3 = new Action_bt_b2_node3();
						node3.SetClassNameString("Action");
						node3.SetId(3);
#if !BEHAVIAC_RELEASE
						node3.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
						node1.AddChild(node3);
						node1.SetHasEvents(node1.HasEvents() | node3.HasEvents());
					}
					{
						Action_bt_b2_node2 node2 = new Action_bt_b2_node2();
						node2.SetClassNameString("Action");
						node2.SetId(2);
#if !BEHAVIAC_RELEASE
						node2.SetAgentType("testbehaviac.x1.MyAgent2");
#endif
						node1.AddChild(node2);
						node1.SetHasEvents(node1.HasEvents() | node2.HasEvents());
					}
					node0.SetHasEvents(node0.HasEvents() | node1.HasEvents());
				}
				bt.SetHasEvents(bt.HasEvents() | node0.HasEvents());
			}
			return true;
		}
	}

}
