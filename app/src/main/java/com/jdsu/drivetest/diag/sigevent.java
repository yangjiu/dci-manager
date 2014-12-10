package com.jdsu.drivetest.diag;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;

import java.util.Arrays;
import java.util.List;

/**
 * <i>native declaration : asm-generic/siginfo.h:64</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class sigevent extends Structure {
    /**
     * C type : sigval_t
     */
    public sigval sigev_value;
    public int sigev_signo;
    public int sigev_notify;
    /**
     * C type : _sigev_un_union
     */
    public _sigev_un_union _sigev_un;

    public sigevent() {
        super();
    }

    ;

    /**
     * @param sigev_value C type : sigval_t<br>
     * @param _sigev_un   C type : _sigev_un_union
     */
    public sigevent(sigval sigev_value, int sigev_signo, int sigev_notify, _sigev_un_union _sigev_un) {
        super();
        this.sigev_value = sigev_value;
        this.sigev_signo = sigev_signo;
        this.sigev_notify = sigev_notify;
        this._sigev_un = _sigev_un;
    }

    public sigevent(Pointer peer) {
        super(peer);
    }

    protected List<?> getFieldOrder() {
        return Arrays.asList("sigev_value", "sigev_signo", "sigev_notify", "_sigev_un");
    }

    /**
     * <i>native declaration : asm-generic/siginfo.h:63</i>
     */
    public static class _sigev_un_union extends Union {
        public int[] _pad = new int[1];
        public int _tid;
        /**
         * C type : _sigev_thread_struct
         */
        public _sigev_thread_struct _sigev_thread;

        public _sigev_un_union() {
            super();
        }

        ;

        /**
         * @param _sigev_thread C type : _sigev_thread_struct
         */
        public _sigev_un_union(_sigev_thread_struct _sigev_thread) {
            super();
            this._sigev_thread = _sigev_thread;
            setType(_sigev_thread_struct.class);
        }

        public _sigev_un_union(int _tid) {
            super();
            this._tid = _tid;
            setType(Integer.TYPE);
        }

        public _sigev_un_union(int[] _pad) {
            super();
            this._pad = _pad;
            setType(_pad.getClass());
        }

        public _sigev_un_union(Pointer peer) {
            super(peer);
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:62</i>
         */
        public static class _sigev_thread_struct extends Structure {
            /**
             * C type : _function_callback*
             */
            public _function_callback _function;
            /**
             * C type : void*
             */
            public Pointer _attribute;

            public _sigev_thread_struct() {
                super();
            }

            ;

            /**
             * @param _function  C type : _function_callback*<br>
             * @param _attribute C type : void*
             */
            public _sigev_thread_struct(_function_callback _function, Pointer _attribute) {
                super();
                this._function = _function;
                this._attribute = _attribute;
            }

            public _sigev_thread_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_function", "_attribute");
            }

            /**
             * <i>native declaration : asm-generic/siginfo.h:61</i>
             */
            public interface _function_callback extends Callback {
                void apply(sigval.ByValue sigval_t1);
            }

            public static class ByReference extends _sigev_thread_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _sigev_thread_struct implements Structure.ByValue {

            }

            ;
        }

        public static class ByReference extends _sigev_un_union implements Structure.ByReference {

        }

        ;

        public static class ByValue extends _sigev_un_union implements Structure.ByValue {

        }

        ;
    }

    public static class ByReference extends sigevent implements Structure.ByReference {

    }

    ;

    public static class ByValue extends sigevent implements Structure.ByValue {

    }

    ;
}