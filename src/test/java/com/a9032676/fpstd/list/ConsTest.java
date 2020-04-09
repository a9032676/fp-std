package com.a9032676.fpstd.list;

import org.junit.jupiter.api.Test;

import static com.a9032676.fpstd.data.List.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsTest {

    @Test
    void basic() {
        appendToAppend();
        appendToCons();
        consToAppend();
    }

    void appendToAppend() {
        assertEquals(pure(1).append(pure(2).append(empty())), cons(1, pure(2)));
        assertEquals(pure(1).append(pure(2).append(empty())), cons(1, pure(2)));
        assertEquals(pure(1).append(pure(2).append(pure(3))), cons(1, cons(2, pure(3))));
        assertEquals(pure(1).append(pure(2).append(pure(3).append(empty()))), cons(1, cons(2, pure(3))));
        assertEquals(pure(1).append(pure(2).append(pure(3)).append(empty())), cons(1, cons(2, pure(3))));
        assertEquals(pure(1).append(pure(2).append(pure(3))).append(empty()), cons(1, cons(2, pure(3))));
        assertEquals(pure(1).append(pure(2)).append(pure(3)).append(pure(4)), cons(1, cons(2, cons(3, pure(4)))));
        assertEquals(pure(1).append(empty()).append(pure(2)).append(pure(3)).append(pure(4)), cons(1, cons(2, cons(3, pure(4)))));
        assertEquals(pure(1).append(pure(2)).append(empty()).append(pure(3)).append(pure(4)), cons(1, cons(2, cons(3, pure(4)))));
        assertEquals(pure(1).append(pure(2)).append(pure(3)).append(empty()).append(pure(4)), cons(1, cons(2, cons(3, pure(4)))));
        assertEquals(pure(1).append(pure(2)).append(pure(3)).append(pure(4)).append(empty()), cons(1, cons(2, cons(3, pure(4)))));
    }

    void appendToCons() {
        assertEquals(pure(1).append(pure(2)), cons(1, pure(2)));
        assertEquals(pure(1).append(cons(2, empty())), cons(1, pure(2)));
        assertEquals(pure(1).append(cons(2, cons(3, empty()))), cons(1, cons(2, pure(3))));
    }

    void consToAppend() {
        assertEquals(cons(1, empty()), pure(1));
        assertEquals(cons(1, empty().append(empty())), pure(1));
        assertEquals(cons(1, empty().append(empty())), pure(1));
        assertEquals(cons(1, empty()).append(empty()), pure(1));
        assertEquals(cons(1, empty().append(empty().append(pure(2)))), cons(1, pure(2)));
        assertEquals(cons(1, empty().append(empty()).append(pure(2))), cons(1, pure(2)));
        assertEquals((cons(1, empty()).append(empty())).append(pure(2)), cons(1, pure(2)));
        assertEquals(cons(1, cons(2, empty().append(empty()))), cons(1, pure(2)));
        assertEquals(cons(1, cons(2, empty()).append(empty())), cons(1, pure(2)));
        assertEquals(cons(1, cons(2, empty())).append(empty()), cons(1, pure(2)));
        assertEquals(cons(1, cons(2, empty().append(empty().append(pure(3))))), cons(1, cons(2, pure(3))));
        assertEquals(cons(1, cons(2, empty()).append(empty()).append(pure(3))), cons(1, cons(2, pure(3))));
        assertEquals((cons(1, cons(2, empty())).append(empty())).append(pure(3)), cons(1, cons(2, pure(3))));
    }
}
