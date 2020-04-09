package com.a9032676.fpstd.data;

import com.a9032676.fpstd.basic.Standard;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.a9032676.fpstd.data.Maybe.*;
import static com.a9032676.fpstd.data.Tuple.*;
import static com.a9032676.fpstd.exception.Undefined.undefined;

public abstract class List<A> implements Iterator<A>, Cloneable, Serializable {

    public static final class Nil<A> extends List<A> {

        @Override public List<A> append(List<A> list) { return list; }
        @Override public A head() { throw undefined(); }
        @Override public A last() { throw undefined(); }
        @Override public List<A> tail() { throw undefined(); }
        @Override public List<A> init() { throw undefined(); }
        @Override public Maybe<Tuple2<A, List<A>>> uncons() { return nothing(); }
        @Override public Boolean isNil() { return true; }
        @Override public Boolean isCons() { return false; }
        @Override public int length() { return 0; }

        @Override public <B> List<B> map(Function<A, B> f) { return empty(); }
        @Override public List<A> reverse() { return this; }
        @Override public List<A> intersperse(A a) { return this; }
        @Override public List<List<A>> subsequences() { return empty(); }
        @Override public List<List<A>> nonEmptySubsequences() { return empty(); }
        @Override public List<List<A>> permutations() { return empty(); }

        @Override public <B> B foldl(BiFunction<B, A, B> f, B b) { return b; }
        @Override public <B> B foldr(BiFunction<A, B, B> f, B b) { return b; }
        @Override public Boolean any(Function<A, Boolean> p) { return false; }
        @Override public Boolean all(Function<A, Boolean> p) { return true; }

        @Override public List<A> take(int i) { return this; }
        @Override public List<A> drop(int i) { return this; }
        @Override public Tuple2<List<A>, List<A>> splitAt(int n) { return tuple2(this, this); }
        @Override public List<A> takeWhile(Function<A, Boolean> p) { return this; }
        @Override public List<A> dropWhile(Function<A, Boolean> p) { return this; }
        @Override public Maybe<List<A>> stripPrefix(List<A> prefix) { return prefix.match(
                () -> just(empty()),
                a -> as -> nothing()
        ); }
        @Override public List<List<A>> inits() { return empty(); }
        @Override public List<List<A>> tails() { return empty(); }

        private Boolean emptyMatchPredicate(List<A> list) { return list.match(() -> true, h -> t -> false); }
        @Override public Boolean isPrefixOf(List<A> prefix) { return emptyMatchPredicate(prefix); }
        @Override public Boolean isSuffixOf(List<A> suffix) { return emptyMatchPredicate(suffix); }
        @Override public Boolean isInfixOf(List<A> infix) { return emptyMatchPredicate(infix); }
        @Override public Boolean isSubsequenceOf(List<A> sequence) { return emptyMatchPredicate(sequence); }

        @Override public Boolean elem(A a) { return false; }
        @Override public Boolean notElem(A a) { return true; }
        @Override public Maybe<A> find(Function<A, Boolean> p) { return nothing(); }
        @Override public List<A> filter(Function<A, Boolean> p) { return this; }
        @Override public Tuple2<List<A>, List<A>> partition(Function<A, Boolean> p) { return tuple2(this, this); }

        @Override public A elemOf(int n) { throw new NoSuchElementException(); }
        @Override public Maybe<Integer> elemIndex(A a) { return nothing(); }
        @Override public List<Integer> elemIndices(A a) { return empty(); }
        @Override public Maybe<Integer> findIndex(Function<A, Boolean> p) { return nothing(); }
        @Override public List<Integer> findIndices(Function<A, Boolean> p) { return empty(); }

        @Override public <B> List<Tuple2<A, B>> zip(List<B> list) { return empty(); }
        @Override public List<A> nub() { return this; }
        @Override public List<A> delete(A a) { return this; }

        @Override public Stream<A> stream() { return Stream.empty(); }
        @Override public Object[] toJArray() { return new Object[]{}; }
        @Override public java.util.List<A> toJList() { return Collections.emptyList(); }
        @Override public java.util.Set<A> toJSet() { return Collections.emptySet(); }
        @Override public <T, R extends Collection<A>> R toCollection(Collector<A, T, R> collector) { return Stream.<A>empty().collect(collector); }

        @Override public <R> R match(Supplier<R> f, Function<A, Function<List<A>, R>> g) { return f.get(); }
        @Override public <R> R match2(Supplier<R> f, Function<List<A>, R> g) { return f.get(); }

        @Override public boolean hasNext() { return false; }
        @Override public A next() { throw new NoSuchElementException(); }

        @Override public boolean equals(Object o) { return o instanceof Nil; }
    }

    public static final class Cons<A> extends List<A> {

        private final A head;
        private final List<A> tail;

        private Cons(A head, List<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override public List<A> append(List<A> list) { return cons(head, tail.append(list)); }
        @Override public A head() { return head; }
        @Override public A last() { return match(this::head, h -> List::last); }
        @Override public List<A> tail() { return tail; }
        @Override public List<A> init() { return match(this::tail, h -> t -> cons(h, t.init())); }
        @Override public Maybe<Tuple2<A, List<A>>> uncons() { return just(tuple2(head, tail)); }
        @Override public Boolean isNil() { return false; }
        @Override public Boolean isCons() { return true; }
        @Override public int length() { return tail.length() + 1; }

        @Override public <B> List<B> map(Function<A, B> f) { return cons(f.apply(head), tail.map(f)); }
        @Override public List<A> reverse() { return tail.reverse().append(pure(head)); }
        @Override public List<A> intersperse(A a) { return cons(head, cons(a, tail.intersperse(a))); }
        @Override public List<List<A>> subsequences() { return cons(empty(), nonEmptySubsequences()); }
        @Override public List<List<A>> nonEmptySubsequences() { return match(
                List::empty,
                h -> t -> List.cons(pure(h), foldr((ys, r) -> cons(ys, cons(cons(h, ys), r)), empty(), t.nonEmptySubsequences()))
        ); }
        @Override public List<List<A>> permutations() { return null; }

        @Override public <B> B foldl(BiFunction<B, A, B> f, B b) { return tail.foldl(f, f.apply(b, head)); }
        @Override public <B> B foldr(BiFunction<A, B, B> f, B b) { return Standard.curry(f).apply(head).apply(tail.foldr(f, b)); }
        @Override public Boolean any(Function<A, Boolean> p) { return p.apply(head) ? true : tail.any(p); }
        @Override public Boolean all(Function<A, Boolean> p) { return p.apply(head) ? tail.all(p) : false; }

        @Override public List<A> take(int i) { return i == 0 ? empty() : cons(head, tail.take(i - 1)); }
        @Override public List<A> drop(int i) { return i == 0 ? this : List.<A>empty().append(tail.drop(i - 1)); }
        @Override public Tuple2<List<A>, List<A>> splitAt(int n) { return tuple2(take(n), drop(n)); }
        @Override public List<A> takeWhile(Function<A, Boolean> p) { return p.apply(head) ? cons(head, tail.takeWhile(p)) : tail.takeWhile(p); }
        @Override public List<A> dropWhile(Function<A, Boolean> p) { return p.apply(head) ? tail.dropWhile(p) : cons(head, tail.dropWhile(p)); }
        @Override public Maybe<List<A>> stripPrefix(List<A> prefix) { return prefix.match(
                () -> just(this),
                h -> t -> head.equals(h) ? tail.stripPrefix(t) : nothing()
        ); }
        @Override public List<List<A>> inits() { return cons(empty(), match(List::empty, h -> t -> pure(pure(h)).append(_inits(pure(h), t)))); }
        private List<List<A>> _inits(List<A> a, List<A> as) { return as.match(List::empty, h2 -> t2 -> cons(a.append(pure(h2)), _inits(a.append(pure(h2)), t2))); }
        @Override public List<List<A>> tails() { return match2(List::empty, l -> _tails(l).append(pure(empty()))); }
        private List<List<A>> _tails(List<A> l) { return l.tail().match2(List::empty, l2 -> pure(l).append(_tails(l2))); }

        @Override public Boolean isPrefixOf(List<A> prefix) { return prefix.match(() -> true, h -> t -> head.equals(h) && tail.isPrefixOf(t)); }
        @Override public Boolean isSuffixOf(List<A> suffix) { return suffix.match(
                () -> true,
                h -> t -> drop(length() - suffix.length()).match2(
                        () -> false,
                        l -> l.length() == suffix.length() && l.isPrefixOf(suffix)
                )
        ); }
        @Override public Boolean isInfixOf(List<A> infix) { return tails().any(l -> l.isPrefixOf(infix)); }
        @Override public Boolean isSubsequenceOf(List<A> seq) { return subsequences().elem(seq); }

        @Override public Boolean elem(A a) { return head.equals(a) ? true : tail.elem(a); }
        @Override public Boolean notElem(A a) { return head.equals(a) ? false : tail.elem(a); }
        @Override public Maybe<A> find(Function<A, Boolean> p) { return p.apply(head) ? just(head) : tail.find(p); }
        @Override public List<A> filter(Function<A, Boolean> p) { return p.apply(head) ? this : List.<A>empty().append(tail.filter(p)); }
        @Override public Tuple2<List<A>, List<A>> partition(Function<A, Boolean> p) { return tuple2(filter(p), filter(p.andThen(Standard::not))); }

        @Override public A elemOf(int n) { return n == 0 ? head : tail.elemOf(n - 1); }
        @Override public Maybe<Integer> elemIndex(A a) { return findIndex(a::equals); }
        @Override public List<Integer> elemIndices(A a) { return findIndices(a::equals); }
        @Override public Maybe<Integer> findIndex(Function<A, Boolean> p) { return listToMaybe(findIndices(p)); }
        @Override public List<Integer> findIndices(Function<A, Boolean> p) {
            return zip(Stream.iterate(0, i -> i + 1).limit(length()).collect(toList())).filter(t -> p.apply(fst(t))).map(Tuple2::snd); }

        @Override public <B> List<Tuple2<A, B>> zip(List<B> list) { return cons(tuple2(head, list.head()), tail.zip(list.tail())); }
        @Override public List<A> nub() { return cons(head, List.<A, List<A>>foldl((l, a) -> head.equals(a) ? l : l.append(pure(a)), empty(), tail).nub()); }
        @Override public List<A> delete(A a) { return head.equals(a) ? tail : cons(head, tail.delete(a)); }

        @Override public Stream<A> stream() { return Stream.concat(Stream.of(head), match(
                Stream::empty,
                h -> List::stream
        )); }
        @Override public Object[] toJArray() { return Stream.concat(Stream.of(head), Arrays.stream(tail.toJArray())).toArray(); }
        @Override public java.util.List<A> toJList() { return Stream.concat(Stream.of(head), tail.toJList().stream()).collect(Collectors.toList()); }
        @Override public java.util.Set<A> toJSet() { return Stream.concat(Stream.of(head), tail.toJSet().stream()).collect(Collectors.toSet()); }
        @Override public <T, R extends Collection<A>> R toCollection(Collector<A, T, R> collector) { return Stream.<A>empty().collect(collector); }

        @Override public <R> R match(Supplier<R> f, Function<A, Function<List<A>, R>> g) { return g.apply(head).apply(tail); }
        @Override public <R> R match2(Supplier<R> f, Function<List<A>, R> g) { return g.apply(this); }

        @Override public boolean hasNext() { return true; }
        @Override public A next() { return head; }

        @Override public boolean equals(Object o) { return o instanceof Cons && ((Cons) o).head.equals(head) && ((Cons) o).tail.equals(tail); }
    }

    public abstract List<A> append(List<A> list);
    public abstract A head();
    public abstract A last();
    public abstract List<A> tail();
    public abstract List<A> init();
    public abstract Maybe<Tuple2<A, List<A>>> uncons();
    public abstract Boolean isNil();
    public abstract Boolean isCons();
    public abstract int length();

    public abstract <B> List<B> map(Function<A, B> f);
    public abstract List<A> reverse();
    public abstract List<A> intersperse(A a);
    public static <A> List<A> intercalate(List<A> sep, List<List<A>> list) { return concat(list.intersperse(sep)); }
    public static <A> List<List<A>> transpose(List<List<A>> list) { return null; }
    public abstract List<List<A>> subsequences();
    public abstract List<List<A>> nonEmptySubsequences();
    public abstract List<List<A>> permutations();

    public abstract <B> B foldl(BiFunction<B, A, B> f, B b);
    public abstract <B> B foldr(BiFunction<A, B, B> f, B b);
    public static <A> List<A> concat(List<List<A>> list) { return list.match(
            List::empty,
            h -> t -> h.append(concat(t))
    ); }
    public abstract Boolean any(Function<A, Boolean> p);
    public abstract Boolean all(Function<A, Boolean> p);

    public abstract List<A> take(int i);
    public abstract List<A> drop(int i);
    public abstract Tuple2<List<A>, List<A>> splitAt(int n);
    public abstract List<A> takeWhile(Function<A, Boolean> p);
    public abstract List<A> dropWhile(Function<A, Boolean> p);
    public abstract Maybe<List<A>> stripPrefix(List<A> prefix);
    public abstract List<List<A>> inits();
    public abstract List<List<A>> tails();

    public abstract Boolean isPrefixOf(List<A> prefix);
    public abstract Boolean isSuffixOf(List<A> suffix);
    public abstract Boolean isInfixOf(List<A> infix);
    public abstract Boolean isSubsequenceOf(List<A> seq);

    public abstract Boolean elem(A a);
    public abstract Boolean notElem(A a);
    public abstract Maybe<A> find(Function<A, Boolean> p);
    public abstract List<A> filter(Function<A, Boolean> p);
    public abstract Tuple2<List<A>, List<A>> partition(Function<A, Boolean> p);

    public abstract A elemOf(int n);
    public abstract Maybe<Integer> elemIndex(A a);
    public abstract List<Integer> elemIndices(A a);
    public abstract Maybe<Integer> findIndex(Function<A, Boolean> p);
    public abstract List<Integer> findIndices(Function<A, Boolean> p);

    public abstract <B> List<Tuple2<A, B>> zip(List<B> list);
    public abstract List<A> nub();
    public abstract List<A> delete(A a);

    public abstract Stream<A> stream();
    public abstract Object[] toJArray();
    public abstract java.util.List<A> toJList();
    public abstract java.util.Set<A> toJSet();
    public abstract <T, R extends Collection<A>> R toCollection(Collector<A, T, R> collector);

    public abstract <R> R match(Supplier<R> f, Function<A, Function<List<A>, R>> g);
    public abstract <R> R match2(Supplier<R> f, Function<List<A>, R> g);

    public static <A> List<A> pure(A a) { return new Cons<>(a, empty()); }
    public static <A> List<A> cons(A a, List<A> list) { return new Cons<>(a, list); }
    public static <A> List<A> empty() { return new Nil<>(); }
    public static <A, B> B foldl(BiFunction<B, A, B> f, B b, List<A> list) { return list.foldl(f, b); }
    public static <A, B> B foldr(BiFunction<A, B, B> f, B b, List<A> list) { return list.foldr(f, b); }
    public static <A, B> List<A> unfoldr(Function<B, Maybe<Tuple2<A, B>>> f, B b) { return f.apply(b).match(
            List::empty,
            t -> cons(fst(t), unfoldr(f, snd(t)))
    ); }
    public static <A> Collector<A, Builder<A>, List<A>> toList() {
        return Collector.of(Builder::empty, Builder::append, Builder::combine, Builder::build);
    }

    public static final class Builder<A> {

        private List<A> list;

        private Builder(List<A> list) { this.list = list; }

        public Builder<A> append(A a) { list = list.append(pure(a)); return this; }
        public Builder<A> combine(Builder<A> next) { list = list.append(next.list); return this; }
        public List<A> build() { return list; }

        public static <A> Builder<A> empty() { return new Builder<>(List.empty()); }
        public static <A> Builder<A> of(List<A> list) { return new Builder<>(list); }
    }

    @Override public String toString() { return match(
            () -> "[]",
            h -> t -> "[" + toStringTail(h, t)
    ); }
    private String toStringTail(A h, List<A> tail) { return tail.match(
            () -> h + "]",
            h2 -> t2 -> h + ", " + tail.toStringTail(h2, t2)
    ); }
}
