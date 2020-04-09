package com.a9032676.fpstd.data;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.a9032676.fpstd.data.List.*;
import static com.a9032676.fpstd.exception.Undefined.undefined;

public abstract class Maybe<A> {

    private static final class Nothing<A> extends Maybe<A> {

        @Override public <B> B maybe(B b, Function<A, B> ab) { return b; }
        @Override public Boolean isJust() { return false; }
        @Override public Boolean isNothing() { return true; }

        @Override public A fromJust() { throw undefined(); }
        @Override public A fromMaybe(A a) { return a; }
        @Override public List<A> toList() { return empty(); }

        @Override public <R> R match(Supplier<R> f, Function<A, R> g) { return f.get(); }

        @Override public boolean equals(Object o) { return o instanceof Nothing; }
    }

    private static final class Just<A> extends Maybe<A> {

        private final A a;

        private Just(A a) { this.a = a; }

        @Override public <B> B maybe(B b, Function<A, B> ab) { return ab.apply(a); }
        @Override public Boolean isJust() { return true; }
        @Override public Boolean isNothing() { return false; }

        @Override public A fromJust() { return a; }
        @Override public A fromMaybe(A a) { return this.a; }
        @Override public List<A> toList() { return pure(a); }

        @Override public <R> R match(Supplier<R> f, Function<A, R> g) { return g.apply(a); }

        @Override public boolean equals(Object o) { return o instanceof Just && ((Just) o).a.equals(a); }
    }

    public abstract <B> B maybe(B b, Function<A, B> ab);
    public abstract Boolean isJust();
    public abstract Boolean isNothing();

    public abstract A fromJust();
    public abstract A fromMaybe(A a);
    public abstract List<A> toList();

    public abstract <R> R match(Supplier<R> f, Function<A, R> g);

    public static <A> Maybe<A> nothing() { return new Nothing<>(); }
    public static <A> Maybe<A> just(A a) { return new Just<>(a); }
    public static <A> Maybe<A> listToMaybe(List<A> list) { return list.match(Maybe::nothing, a -> as -> just(a)); }
    public static <A> List<A> maybeToList(Maybe<A> maybe) { return maybe.toList(); }
    public static <A> List<A> catMaybes(List<Maybe<A>> list) { return list.match(List::empty, ma -> mas -> ma.match(List::empty, a -> cons(a, catMaybes(mas)))); }
    public static <A, B> List<B> mapMaybe(Function<A, Maybe<B>> f, List<A> list) { return catMaybes(list.map(f)); }
}
