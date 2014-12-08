package com.jdsu.drivetest.diag;

import com.sun.jna.NativeLong;
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
public class siginfo extends Structure {
    public int si_signo;
    public int si_errno;
    public int si_code;
    /**
     * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
     * C type : _sifields_union
     */
    public _sifields_union _sifields;

    public siginfo() {
        super();
    }

    ;

    /**
     * @param _sifields WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
     *                  C type : _sifields_union
     */
    public siginfo(int si_signo, int si_errno, int si_code, _sifields_union _sifields) {
        super();
        this.si_signo = si_signo;
        this.si_errno = si_errno;
        this.si_code = si_code;
        this._sifields = _sifields;
    }

    public siginfo(Pointer peer) {
        super(peer);
    }

    protected List<?> getFieldOrder() {
        return Arrays.asList("si_signo", "si_errno", "si_code", "_sifields");
    }

    /**
     * <i>native declaration : asm-generic/siginfo.h:63</i>
     */
    public static abstract class _sifields_union extends Union {
        public int[] _pad = new int[1];
        /**
         * C type : _kill_struct
         */
        public _kill_struct _kill;
        /**
         * C type : _timer_struct
         */
        public _timer_struct _timer;
        /**
         * C type : _rt_struct
         */
        public _rt_struct _rt;
        /**
         * C type : _sigchld_struct
         */
        public _sigchld_struct _sigchld;
        /**
         * C type : _sigfault_struct
         */
        public _sigfault_struct _sigfault;
        /**
         * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
         * C type : _sigpoll_struct
         */
        public _sigpoll_struct _sigpoll;
        /**
         * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
         * C type : _sigsys_struct
         */
        public _sigsys_struct _sigsys;

        public _sifields_union() {
            super();
        }

        ;

        /**
         * @param _kill C type : _kill_struct
         */
        public _sifields_union(_kill_struct _kill) {
            super();
            this._kill = _kill;
            setType(_kill_struct.class);
        }

        ;

        /**
         * @param _timer C type : _timer_struct
         */
        public _sifields_union(_timer_struct _timer) {
            super();
            this._timer = _timer;
            setType(_timer_struct.class);
        }

        ;

        /**
         * @param _sigchld C type : _sigchld_struct
         */
        public _sifields_union(_sigchld_struct _sigchld) {
            super();
            this._sigchld = _sigchld;
            setType(_sigchld_struct.class);
        }

        ;

        /**
         * @param _sigsys WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
         *                C type : _sigsys_struct
         */
        public _sifields_union(_sigsys_struct _sigsys) {
            super();
            this._sigsys = _sigsys;
            setType(_sigsys_struct.class);
        }

        ;

        /**
         * @param _sigfault C type : _sigfault_struct
         */
        public _sifields_union(_sigfault_struct _sigfault) {
            super();
            this._sigfault = _sigfault;
            setType(_sigfault_struct.class);
        }

        ;

        /**
         * @param _rt C type : _rt_struct
         */
        public _sifields_union(_rt_struct _rt) {
            super();
            this._rt = _rt;
            setType(_rt_struct.class);
        }

        ;

        /**
         * @param _sigpoll WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
         *                 C type : _sigpoll_struct
         */
        public _sifields_union(_sigpoll_struct _sigpoll) {
            super();
            this._sigpoll = _sigpoll;
            setType(_sigpoll_struct.class);
        }

        public _sifields_union(Pointer peer) {
            super(peer);
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:27</i>
         */
        public static class _kill_struct extends Structure {
            /**
             * C type : __kernel_pid_t
             */
            public int _pid;
            /**
             * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             * C type : __kernel_uid32_t
             */
            public int _uid;

            public _kill_struct() {
                super();
            }

            /**
             * @param _pid C type : __kernel_pid_t<br>
             * @param _uid WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             *             C type : __kernel_uid32_t
             */
            public _kill_struct(int _pid, int _uid) {
                super();
                this._pid = _pid;
                this._uid = _uid;
            }

            public _kill_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_pid", "_uid");
            }

            public static class ByReference extends _kill_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _kill_struct implements Structure.ByValue {

            }

            ;
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:35</i>
         */
        public static abstract class _timer_struct extends Structure {
            /**
             * C type : __kernel_timer_t
             */
            public int _tid;
            /**
             * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS
             */
            public int _overrun;
            public int[] _pad = new int[0];
            /**
             * C type : sigval_t
             */
            public sigval _sigval;
            public int _sys_private;

            public _timer_struct() {
                super();
            }

            /**
             * @param _tid     C type : __kernel_timer_t<br>
             * @param _overrun WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             * @param _sigval  C type : sigval_t
             */
            public _timer_struct(int _tid, int _overrun, sigval _sigval, int _sys_private) {
                super();
                this._tid = _tid;
                this._overrun = _overrun;
                this._sigval = _sigval;
                this._sys_private = _sys_private;
            }

            public _timer_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_tid", "_overrun", "_sigval", "_sys_private");
            }

            public static abstract class ByReference extends _timer_struct implements Structure.ByReference {

            }

            ;

            public static abstract class ByValue extends _timer_struct implements Structure.ByValue {

            }

            ;
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:41</i>
         */
        public static class _rt_struct extends Structure {
            /**
             * C type : __kernel_pid_t
             */
            public int _pid;
            /**
             * C type : __kernel_uid32_t
             */
            public int _uid;
            /**
             * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             * C type : sigval_t
             */
            public sigval _sigval;

            public _rt_struct() {
                super();
            }

            /**
             * @param _pid    C type : __kernel_pid_t<br>
             * @param _uid    C type : __kernel_uid32_t<br>
             * @param _sigval WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             *                C type : sigval_t
             */
            public _rt_struct(int _pid, int _uid, sigval _sigval) {
                super();
                this._pid = _pid;
                this._uid = _uid;
                this._sigval = _sigval;
            }

            public _rt_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_pid", "_uid", "_sigval");
            }

            public static class ByReference extends _rt_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _rt_struct implements Structure.ByValue {

            }

            ;
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:49</i>
         */
        public static class _sigchld_struct extends Structure {
            /**
             * C type : __kernel_pid_t
             */
            public int _pid;
            /**
             * WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             * C type : __kernel_uid32_t
             */
            public int _uid;
            public int _status;
            /**
             * C type : __kernel_clock_t
             */
            public NativeLong _utime;
            /**
             * C type : __kernel_clock_t
             */
            public NativeLong _stime;

            public _sigchld_struct() {
                super();
            }

            /**
             * @param _pid   C type : __kernel_pid_t<br>
             * @param _uid   WARNING: DO NOT EDIT, AUTO-GENERATED CODE - SEE TOP FOR INSTRUCTIONS<br>
             *               C type : __kernel_uid32_t<br>
             * @param _utime C type : __kernel_clock_t<br>
             * @param _stime C type : __kernel_clock_t
             */
            public _sigchld_struct(int _pid, int _uid, int _status, NativeLong _utime, NativeLong _stime) {
                super();
                this._pid = _pid;
                this._uid = _uid;
                this._status = _status;
                this._utime = _utime;
                this._stime = _stime;
            }

            public _sigchld_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_pid", "_uid", "_status", "_utime", "_stime");
            }

            public static class ByReference extends _sigchld_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _sigchld_struct implements Structure.ByValue {

            }

            ;
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:53</i>
         */
        public static class _sigfault_struct extends Structure {
            /**
             * C type : void*
             */
            public Pointer _addr;
            public short _addr_lsb;

            public _sigfault_struct() {
                super();
            }

            /**
             * @param _addr C type : void*
             */
            public _sigfault_struct(Pointer _addr, short _addr_lsb) {
                super();
                this._addr = _addr;
                this._addr_lsb = _addr_lsb;
            }

            public _sigfault_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_addr", "_addr_lsb");
            }

            public static class ByReference extends _sigfault_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _sigfault_struct implements Structure.ByValue {

            }

            ;
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:57</i>
         */
        public static class _sigpoll_struct extends Structure {
            public NativeLong _band;
            public int _fd;

            public _sigpoll_struct() {
                super();
            }

            public _sigpoll_struct(NativeLong _band, int _fd) {
                super();
                this._band = _band;
                this._fd = _fd;
            }

            public _sigpoll_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_band", "_fd");
            }

            public static class ByReference extends _sigpoll_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _sigpoll_struct implements Structure.ByValue {

            }

            ;
        }

        /**
         * <i>native declaration : asm-generic/siginfo.h:62</i>
         */
        public static class _sigsys_struct extends Structure {
            /**
             * C type : void*
             */
            public Pointer _call_addr;
            public int _syscall;
            public int _arch;

            public _sigsys_struct() {
                super();
            }

            /**
             * @param _call_addr C type : void*
             */
            public _sigsys_struct(Pointer _call_addr, int _syscall, int _arch) {
                super();
                this._call_addr = _call_addr;
                this._syscall = _syscall;
                this._arch = _arch;
            }

            public _sigsys_struct(Pointer peer) {
                super(peer);
            }

            protected List<?> getFieldOrder() {
                return Arrays.asList("_call_addr", "_syscall", "_arch");
            }

            public static class ByReference extends _sigsys_struct implements Structure.ByReference {

            }

            ;

            public static class ByValue extends _sigsys_struct implements Structure.ByValue {

            }

            ;
        }

        public static abstract class ByReference extends _sifields_union implements Structure.ByReference {

        }

        ;

        public static abstract class ByValue extends _sifields_union implements Structure.ByValue {

        }

        ;
    }

    public static class ByReference extends siginfo implements Structure.ByReference {

    }

    ;

    public static class ByValue extends siginfo implements Structure.ByValue {

    }

    ;
}
