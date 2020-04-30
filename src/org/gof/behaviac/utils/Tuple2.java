package org.gof.behaviac.utils;

public class Tuple2<A, B> {
	public final A value1;
	public final B value2;

	public Tuple2(A a, B b) {
		value1 = a;
		value2 = b;
	}

	public String toString() {
		return "(" + value1 + ", " + value2 + ")";
	}

}