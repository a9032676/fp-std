package com.a9032676.fpstd.data;

import java.util.function.Function;

public abstract class Tuple {

    public static final class Unit<A> extends Tuple {

        private A a;

        private Unit(A a) {
            this.a = a;
        }

    }

    public static final class Tuple2<A, B> extends Tuple {

        private A a;
        private B b;

        private Tuple2(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override public boolean equals(Object o) { return o instanceof Tuple2 && ((Tuple2) o).a.equals(a) && ((Tuple2) o).b.equals(b); }

        @Override public String toString() { return "(" + a + ", " + b + ")"; }
    }

    public static <A> Unit<A> unit(A a) { return new Unit<>(a); }
    public static <A, B> Tuple2<A, B> tuple2(A a, B b) { return new Tuple2<>(a, b); }

    public static <A, B> A fst(Tuple2<A, B> t2) { return t2.a; }
    public static <A, B> B snd(Tuple2<A, B> t2) { return t2.b; }

    public static <A, B, C> Function<A, Function<B, C>> curry(Function<Tuple2<A, B>, C> f) { return a -> b -> f.apply(new Tuple2<>(a, b)); }
    public static <A, B, C> Function<Tuple2<A, B>, C> uncurry(Function<A, Function<B, C>> f) { return t -> f.apply(t.a).apply(t.b); }
    public static <A, B> Tuple2<A, B> swap(Tuple2<B, A> t) { return new Tuple2<>(t.b, t.a); }
}
