﻿// -------------------------------------------------------------------------------
// THIS FILE IS ORIGINALLY GENERATED BY THE DESIGNER.
// YOU ARE ONLY ALLOWED TO MODIFY CODE BETWEEN '///<<< BEGIN' AND '///<<< END'.
// PLEASE MODIFY AND REGENERETE IT IN THE DESIGNER FOR CLASS/MEMBERS/METHODS, ETC.
// -------------------------------------------------------------------------------

using System;
using System.Collections;
using System.Collections.Generic;

///<<< BEGIN WRITING YOUR CODE FILE_INIT

///<<< END WRITING YOUR CODE

namespace org.gof.worldsrv
{
///<<< BEGIN WRITING YOUR CODE NAMESPACE_INIT

///<<< END WRITING YOUR CODE

	public class MyAgent : behaviac.Agent
///<<< BEGIN WRITING YOUR CODE MyAgent
///<<< END WRITING YOUR CODE
	{
		public static ushort s1 = 0;

		private static long s2 = 0;
		public static void _set_s2(long value)
		{
			s2 = value;
		}
		public static long _get_s2()
		{
			return s2;
		}

		private behaviac.EBTStatus v1 = behaviac.EBTStatus.BT_INVALID;
		public void _set_v1(behaviac.EBTStatus value)
		{
			v1 = value;
		}
		public behaviac.EBTStatus _get_v1()
		{
			return v1;
		}

		private List<behaviac.EBTStatus> v11 = new List<behaviac.EBTStatus>(0) {};
		public void _set_v11(List<behaviac.EBTStatus> value)
		{
			v11 = value;
		}
		public List<behaviac.EBTStatus> _get_v11()
		{
			return v11;
		}

		private List<bool> v12 = new List<bool>(0) {};
		public void _set_v12(List<bool> value)
		{
			v12 = value;
		}
		public List<bool> _get_v12()
		{
			return v12;
		}

		private int v13 = 0;
		public void _set_v13(int value)
		{
			v13 = value;
		}
		public int _get_v13()
		{
			return v13;
		}

		private float v14 = 0f;
		public void _set_v14(float value)
		{
			v14 = value;
		}
		public float _get_v14()
		{
			return v14;
		}

		private double v15 = 0;
		public void _set_v15(double value)
		{
			v15 = value;
		}
		public double _get_v15()
		{
			return v15;
		}

		private string v16 = "";
		public void _set_v16(string value)
		{
			v16 = value;
		}
		public string _get_v16()
		{
			return v16;
		}

		private bool v2 = false;
		public void _set_v2(bool value)
		{
			v2 = value;
		}
		public bool _get_v2()
		{
			return v2;
		}

		private int v3 = 0;
		public void _set_v3(int value)
		{
			v3 = value;
		}
		public int _get_v3()
		{
			return v3;
		}

		private uint v4 = 0;
		public void _set_v4(uint value)
		{
			v4 = value;
		}
		public uint _get_v4()
		{
			return v4;
		}

		private float v5 = 0f;
		public void _set_v5(float value)
		{
			v5 = value;
		}
		public float _get_v5()
		{
			return v5;
		}

		private double v6 = 0;
		public void _set_v6(double value)
		{
			v6 = value;
		}
		public double _get_v6()
		{
			return v6;
		}

		private string v7 = "";
		public void _set_v7(string value)
		{
			v7 = value;
		}
		public string _get_v7()
		{
			return v7;
		}

		public behaviac.EBTStatus m1(bool v1, uint v2, float v3, string v4, long v5, List<bool> v11, List<int> v12, double v13)
		{
///<<< BEGIN WRITING YOUR CODE m1
			return behaviac.EBTStatus.BT_INVALID;
///<<< END WRITING YOUR CODE
		}

		public static void StaticMethod1(bool z1, double z2)
		{
///<<< BEGIN WRITING YOUR CODE StaticMethod1
///<<< END WRITING YOUR CODE
		}

		public void testM2(List<float> z1, uint z2)
		{
///<<< BEGIN WRITING YOUR CODE testM2
///<<< END WRITING YOUR CODE
		}

///<<< BEGIN WRITING YOUR CODE CLASS_PART

///<<< END WRITING YOUR CODE

	}

///<<< BEGIN WRITING YOUR CODE NAMESPACE_UNINIT

///<<< END WRITING YOUR CODE
}

///<<< BEGIN WRITING YOUR CODE FILE_UNINIT

///<<< END WRITING YOUR CODE

