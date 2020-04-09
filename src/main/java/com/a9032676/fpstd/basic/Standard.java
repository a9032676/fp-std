package com.a9032676.fpstd.basic;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Standard {

    public static <T> T identity(T t) { return t; }
    public static <T> Function<T, T> identity() { return t -> t; }

    public static <A, B> A _const_(A a, B b) { return a; }
    public static <A, B> Function<B, A> _const_(A a) { return b -> a; }
    public static <A, B> Function<A, Function<B, A>> _const_() { return a -> b -> a; }

    public static <T, U, R> Function<T, Function<U, R>> curry(BiFunction<T, U, R> biFunc) { return t -> u -> biFunc.apply(t, u); }
    public static <T, U, R> BiFunction<T, U, R> uncurry(Function<T, Function<U, R>> func) { return (t, u) -> func.apply(t).apply(u); }

    public static Boolean not(Boolean b) { return !b; }
    public static Function<Boolean, Boolean> not() { return Standard::not; }

    public static <A, B, C> C compose(Function<B, C> f, Function<A, B> g, A a) { return f.apply(g.apply(a)); }
    public static <A, B, C> Function<A, C> compose(Function<B, C> f, Function<A, B> g) { return a -> compose(f, g, a); }
    public static <A, B, C> Function<Function<A, B>, Function<A, C>> compose(Function<B, C> f) { return g -> a -> compose(f, g, a); }
    public static <A, B, C> Function<Function<B, C>, Function<Function<A, B>, Function<A, C>>> compose() { return f -> g -> a -> compose(f, g, a); }

    public static <A, B, C> C flip(Function<A, Function<B, C>> f, B b, A a) { return f.apply(a).apply(b); }
    public static <A, B, C> Function<A, C> flip(Function<A, Function<B, C>> f, B b) { return a -> flip(f, b, a); }
    public static <A, B, C> Function<B, Function<A, C>> flip(Function<A, Function<B, C>> f) { return b -> a -> flip(f, b, a); }
    public static <A, B, C> Function<Function<A, Function<B, C>>, Function<B, Function<A, C>>> flip() { return f -> b -> a -> flip(f, b, a); }
}
