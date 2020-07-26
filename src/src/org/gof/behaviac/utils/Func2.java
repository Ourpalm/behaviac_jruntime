package org.gof.behaviac.utils;

@FunctionalInterface
public interface Func2<R, P1, P2> {
	R run(P1 p1, P2 p2);
}
