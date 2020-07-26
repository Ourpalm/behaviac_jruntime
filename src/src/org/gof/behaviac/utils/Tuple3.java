package org.gof.behaviac.utils;

public class Tuple3<A, B, C> extends Tuple2<A, B> {

	public final C value3;

	public Tuple3(A a, B b, C c) {
		super(a, b);
		value3 = c;
	}

	public String toString() {
		return "(" + value1 + "," + value2 + "," + value3 + ")";
	}
}