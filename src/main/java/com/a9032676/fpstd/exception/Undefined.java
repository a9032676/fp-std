package com.a9032676.fpstd.exception;

public class Undefined extends RuntimeException {

    private Undefined() {
        super("** Exception: fpstd.undefined");
    }

    public static Undefined undefined() { return new Undefined(); }
}
