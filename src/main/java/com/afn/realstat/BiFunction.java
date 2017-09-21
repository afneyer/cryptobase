package com.afn.realstat;

@FunctionalInterface
interface BiFunction<A, B, R> {
	public R apply(A a, B b);
}