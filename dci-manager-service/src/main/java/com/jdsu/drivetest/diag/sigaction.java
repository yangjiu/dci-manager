package com.jdsu.drivetest.diag;

import com.jdsu.drivetest.diag.CLibrary.__signalfn_t;
import com.sun.jna.Callback;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

import java.util.Arrays;
import java.util.List;

/**
 * <i>native declaration : asm/signal.h:11</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class sigaction extends Structure {
    /**
     * C type : _u_union
     */
    public _u_union _u;
    /**
     * C type : sigset_t
     */
    public NativeLong sa_mask;
    public NativeLong sa_flags;
    /**
     * C type : sa_restorer_callback*
     */
    public sa_restorer_callback sa_restorer;

    public sigaction() {
        super();
    }

    ;

    /**
     * @param _u          C type : _u_union<br>
     * @param sa_mask     C type : sigset_t<br>
     * @param sa_restorer C type : sa_restorer_callback*
     */
    public sigaction(_u_union _u, NativeLong sa_mask, NativeLong sa_flags, sa_restorer_callback sa_restorer) {
        super();
        this._u = _u;
        this.sa_mask = sa_mask;
        this.sa_flags = sa_flags;
        this.sa_restorer = sa_restorer;
    }

    ;

    public sigaction(Pointer peer) {
        super(peer);
    }

    protected List<?> getFieldOrder() {
        return Arrays.asList("_u", "sa_mask", "sa_flags", "sa_restorer");
    }

    /**
     * <i>native declaration : asm/signal.h:10</i>
     */
    public interface sa_restorer_callback extends Callback {
        void apply();
    }

    /**
     * <i>native declaration : asm/signal.h:9</i>
     */
    public static class _u_union extends Union {
        /**
         * C type : __sighandler_t
         */
        public __signalfn_t _sa_handler;
        /**
         * C type : _sa_sigaction_callback*
         */
        public _sa_sigaction_callback _sa_sigaction;

        public _u_union() {
            super();
        }

        ;

        /**
         * @param _sa_sigaction C type : _sa_sigaction_callback*
         */
        public _u_union(_sa_sigaction_callback _sa_sigaction) {
            super();
            this._sa_sigaction = _sa_sigaction;
            setType(_sa_sigaction_callback.class);
        }

        /**
         * @param _sa_handler C type : __sighandler_t
         */
        public _u_union(__signalfn_t _sa_handler) {
            super();
            this._sa_handler = _sa_handler;
            setType(__signalfn_t.class);
        }

        public _u_union(Pointer peer) {
            super(peer);
        }

        /**
         * <i>native declaration : asm/signal.h:8</i>
         */
        public interface _sa_sigaction_callback extends Callback {
            void apply(int int1, siginfo siginfoPtr1, Pointer voidPtr1);
        }

        public static class ByReference extends _u_union implements Structure.ByReference {

        }

        ;

        public static class ByValue extends _u_union implements Structure.ByValue {

        }

        ;
    }

    public static class ByReference extends sigaction implements Structure.ByReference {

    }

    ;

    public static class ByValue extends sigaction implements Structure.ByValue {

    }

    ;
}
