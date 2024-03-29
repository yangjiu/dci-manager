package com.jdsu.drivetest.diag;

import com.sun.jna.Pointer;
import com.sun.jna.Union;

/**
 * <i>native declaration : asm-generic/siginfo.h:3</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class sigval extends Union {
    public int sival_int;
    /**
     * C type : void*
     */
    public Pointer sival_ptr;

    public sigval() {
        super();
    }

    /**
     * @param sival_ptr C type : void*
     */
    public sigval(Pointer sival_ptr) {
        super();
        this.sival_ptr = sival_ptr;
        setType(Pointer.class);
    }

    public sigval(int sival_int) {
        super();
        this.sival_int = sival_int;
        setType(Integer.TYPE);
    }

    public static class ByReference extends sigval implements com.sun.jna.Structure.ByReference {

    }

    ;

    public static class ByValue extends sigval implements com.sun.jna.Structure.ByValue {

    }

    ;
}
