package com.a9032676.fpstd.list;

import com.a9032676.fpstd.basic.Standard;
import com.a9032676.fpstd.data.List;
import com.a9032676.fpstd.exception.Undefined;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static com.a9032676.fpstd.data.List.*;
import static com.a9032676.fpstd.data.Maybe.just;
import static com.a9032676.fpstd.data.Maybe.nothing;
import static com.a9032676.fpstd.data.Tuple.tuple2;
import static org.junit.jupiter.api.Assertions.*;

public class NilTest {

    @Test
    void basic() {
        appendToEmpty();
        appendToAppend();
        appendToCons();
        assertThrows(Undefined.class, () -> empty().head());
        assertThrows(Undefined.class, () -> empty().last());
        assertThrows(Undefined.class, () -> empty().tail());
        assertThrows(Undefined.class, () -> empty().init());
        assertEquals(empty().uncons(), nothing());
        assertTrue(empty().isNil());
        assertFalse(empty().isCons());
        assertEquals(empty().length(), 0);
    }

    private void appendToEmpty() {
        assertEquals(empty(), new List.Nil<>());
        assertEquals(empty(), empty());
        assertEquals(empty().append(empty()), empty());
        assertEquals(empty().append(empty().append(empty())), empty());
        assertEquals(empty().append(empty()).append(empty()), empty());
    }

    private void appendToAppend() {
        assertEquals(List.<Integer>empty().append(List.pure(1).append(empty())), pure(1));
        assertEquals(List.<Integer>empty().append(List.pure(1).append(empty())), cons(1, empty()));
        assertEquals(List.<Integer>empty().append(List.pure(1).append(pure(2))), cons(1, pure(2)));
        assertEquals(List.<Integer>empty().append(List.pure(1).append(pure(2).append(empty()))), cons(1, pure(2)));
        assertEquals(List.<Integer>empty().append(List.pure(1).append(pure(2)).append(empty())), cons(1, pure(2)));
        assertEquals(List.<Integer>empty().append(List.pure(1).append(pure(2))).append(empty()), cons(1, pure(2)));
    }

    private void appendToCons() {
        assertEquals(empty().append(pure(1)), pure(1));
        assertEquals(empty().append(cons(1, empty())), pure(1));
        assertEquals(empty().append(cons(1, cons(2, empty()))), cons(1, pure(2)));
    }

    @Test
    void transformation() {
        assertEquals(List.<Integer>empty().map(i -> i + 1), empty());
        assertEquals(List.<Integer>empty().reverse(), empty());
        assertEquals(List.<Character>empty().intersperse(','), empty());
        assertEquals(intercalate(empty(), empty()), empty());
        assertEquals(intercalate(cons(0, empty()), empty()), empty());
        assertEquals(List.<Character>empty().subsequences(), empty());
        assertEquals(List.<Character>empty().permutations(), empty());
    }

    @Test
    void folds() {
        assertEquals(List.<Integer, Integer>foldl((b, a) -> b + a, 0, List.empty()), 0);
        assertEquals(List.<Integer>empty().foldl((b, a) -> b + a, 0), 0);
        assertEquals(List.<Integer, List<Integer>>foldl((b, a) -> cons(a, b), empty(), empty()), empty());
        assertEquals(List.<List<Integer>>empty().foldl((b, a) -> cons(a, b), empty()), empty());
        assertEquals(List.<Integer, Integer>foldr((b, a) -> b + a, 0, List.empty()), 0);
        assertEquals(List.<Integer>empty().foldr((b, a) -> b + a, 0), 0);
        assertEquals(List.<Integer, List<Integer>>foldr(List::cons, empty(), empty()), empty());
        assertEquals(List.<List<Integer>>empty().foldr(List::cons, empty()), empty());
        assertEquals(concat(empty()), empty());
        assertEquals(List.<Boolean>empty().any(Standard::identity), false);
        assertEquals(List.<Boolean>empty().all(Standard::identity), true);
        assertEquals(List.unfoldr(b -> b == 0 ? nothing() : just(tuple2(b, b - 1)), 0), empty());
    }

    @Test
    void infinite() {
        // iterate
        // repeat
        // replicate
        // cycle
    }

    @Test
    void sublists() {
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().take(i), empty()));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().drop(i), empty()));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().splitAt(i), tuple2(empty(), empty())));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().takeWhile(o -> i > 5), empty()));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().dropWhile(o -> i > 5), empty()));
        assertEquals(empty().stripPrefix(empty()), just(empty()));
        assertEquals(empty().stripPrefix(cons('a', cons('b', cons('c', empty())))), nothing());

        List<Integer> c0 = cons(1, cons(2, cons(3, cons(4, cons(5, empty())))));
        List<Integer> c1 = cons(3, cons(4, cons(5, empty())));

        boolean r1 = c1.isSuffixOf(c0);
        System.out.println(r1);

        List<List<Integer>> r2 = c0.tails();
        System.out.println(r2);

        for (List<Integer> it = c0; it.hasNext(); it = it.tail()) {
            int i = it.next();
            System.out.println(i);
        }

//        System.out.println(Stream.iterate(0, i -> i + 1).limit(10).collect(Collectors.toList()));
    }

    @Test
    void predicates() {
        assertEquals(empty().isPrefixOf(empty()), true);
        assertEquals(empty().isPrefixOf(cons('a', cons('b', cons('c', empty())))), false);
        assertEquals(empty().isSuffixOf(empty()), true);
        assertEquals(empty().isSuffixOf(cons('d', cons('e', cons('f', empty())))), false);
        assertEquals(empty().isInfixOf(empty()), true);
        assertEquals(empty().isInfixOf(cons('b', cons('c', cons('d', empty())))), false);
        assertEquals(empty().isSubsequenceOf(empty()), true);
        assertEquals(empty().isSubsequenceOf(cons('b', cons('c', cons('d', empty())))), false);
    }

    @Test
    void searching() {
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().elem(i), false));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().notElem(i), true));
        assertEquals(empty().find(o -> true), nothing());
        assertEquals(empty().find(o -> false), nothing());
        assertEquals(List.<Integer>empty().find(i -> i > 5), nothing());
        assertEquals(empty().filter(o -> true), empty());
        assertEquals(empty().filter(o -> false), empty());
        assertEquals(List.<Integer>empty().filter(i -> i > 5), empty());
        assertEquals(empty().partition(o -> true), tuple2(empty(), empty()));
        assertEquals(empty().partition(o -> false), tuple2(empty(), empty()));
        assertEquals(List.<Integer>empty().partition(i -> i > 5), tuple2(empty(), empty()));
    }

    @Test
    void indexing() {
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertThrows(NoSuchElementException.class, () -> empty().elemOf(i)));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().elemIndex(i), nothing()));
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().elemIndices(i), empty()));
        assertEquals(empty().findIndex(o -> true), nothing());
        assertEquals(empty().findIndex(o -> false), nothing());
        assertEquals(List.<Integer>empty().findIndex(i -> i > 5), nothing());
        assertEquals(empty().findIndices(o -> true), empty());
        assertEquals(empty().findIndices(o -> false), empty());
        assertEquals(List.<Integer>empty().findIndices(i -> i > 5), empty());
    }

    @Test
    void setOp() {
        assertEquals(empty().nub(), empty());
        assertEquals(empty().zip(empty()), empty());
        assertEquals(empty().zip(cons(1, empty())), empty());
        assertEquals(empty().zip(cons(1, cons(2, empty()))), empty());
        assertEquals(empty().zip(cons(1, cons(2, cons(3, empty())))), empty());
        Stream.iterate(0, i -> i + 1).limit(10).forEach(i -> assertEquals(empty().delete(i), empty()));
        System.out.println(cons(2, cons(3, cons(1, cons(4, empty())))).delete(1));
        //delete
    }
}
