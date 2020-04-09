package com.a9032676.fpstd.data;

import java.util.function.Function;
import static com.a9032676.fpstd.data.Tuple.*;

public abstract class Either<A, B> {

    public static final class Left<A, B> extends Either<A, B> {

        private A a;

        private Left(A a) { this.a = a; }

        @Override public <C> C either(Function<A, C> f, Function<B, C> g) { return f.apply(a); }

        @Override public Boolean isLeft() { return true; }
        @Override public Boolean isRight() { return false; }

        @Override public A fromLeft(A a) { return this.a; }
        @Override public B fromRight(B b) { return b; }

        @Override public boolean equals(Object o) { return o instanceof Left && ((Left) o).a.equals(a); }
    }

    public static final class Right<A, B> extends Either<A, B> {

        private B b;

        private Right(B b) { this.b = b; }

        @Override public <C> C either(Function<A, C> f, Function<B, C> g) { return g.apply(b); }

        @Override public Boolean isLeft() { return false; }
        @Override public Boolean isRight() { return true; }

        @Override public A fromLeft(A a) { return a; }
        @Override public B fromRight(B b) { return this.b; }

        @Override public boolean equals(Object o) { return o instanceof Right && ((Right) o).b.equals(b); }
    }

    public abstract <C> C either(Function<A, C> f, Function<B, C> g);

    public abstract Boolean isLeft();
    public abstract Boolean isRight();

    public abstract A fromLeft(A a);
    public abstract B fromRight(B b);

    public static <A, B> Either<A, B> left(A a) { return new Left<>(a); }
    public static <A, B> Either<A, B> right(B b) { return new Right<>(b); }
    public static <A, B> List<A> lefts(List<Either<A, B>> list) { return list.filter(Either::isLeft).map(e -> ((Left<A, B>) e).a); }
    public static <A, B> List<B> rights(List<Either<A, B>> list) { return list.filter(Either::isRight).map(e -> ((Right<A, B>) e).b); }
    public static <A, B> Tuple2<List<A>, List<B>> partitionEithers(List<Either<A, B>> list) { return tuple2(lefts(list), rights(list)); }
}
