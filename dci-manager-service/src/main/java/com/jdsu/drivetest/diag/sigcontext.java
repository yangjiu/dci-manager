package com.jdsu.drivetest.diag;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * ***************************************************************************<br>
 * **<br>
 * **   This header was automatically generated from a Linux kernel header<br>
 * **   of the same name, to make information necessary for userspace to<br>
 * **   call into the kernel available to libc.  It contains only constants,<br>
 * **   structures, and macros generated from the original header, and thus,<br>
 * **   contains no copyrightable information.<br>
 * **<br>
 * ***************************************************************************<br>
 * **************************************************************************<br>
 * <i>native declaration : asm/sigcontext.h:34</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> , <a href="http://rococoa.dev.java.net/">Rococoa</a>, or <a href="http://jna.dev.java.net/">JNA</a>.
 */
public class sigcontext extends Structure {
    public NativeLong trap_no;
    public NativeLong error_code;
    public NativeLong oldmask;
    public NativeLong arm_r0;
    public NativeLong arm_r1;
    public NativeLong arm_r2;
    public NativeLong arm_r3;
    public NativeLong arm_r4;
    public NativeLong arm_r5;
    public NativeLong arm_r6;
    public NativeLong arm_r7;
    public NativeLong arm_r8;
    public NativeLong arm_r9;
    public NativeLong arm_r10;
    public NativeLong arm_fp;
    public NativeLong arm_ip;
    public NativeLong arm_sp;
    public NativeLong arm_lr;
    public NativeLong arm_pc;
    public NativeLong arm_cpsr;
    public NativeLong fault_address;

    public sigcontext() {
        super();
    }

    public sigcontext(Pointer peer) {
        super(peer);
    }

    protected List<?> getFieldOrder() {
        return Arrays.asList("trap_no", "error_code", "oldmask", "arm_r0", "arm_r1", "arm_r2", "arm_r3", "arm_r4", "arm_r5", "arm_r6", "arm_r7", "arm_r8", "arm_r9", "arm_r10", "arm_fp", "arm_ip", "arm_sp", "arm_lr", "arm_pc", "arm_cpsr", "fault_address");
    }

    public static class ByReference extends sigcontext implements Structure.ByReference {

    }

    ;

    public static class ByValue extends sigcontext implements Structure.ByValue {

    }

    ;
}
