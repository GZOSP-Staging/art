/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.lang.reflect.Method;

public class Main {

  /// CHECK-START: int Main.signByte(byte) builder (after)
  /// CHECK-DAG:     <<Result:i\d+>> InvokeStaticOrDirect intrinsic:IntegerSignum
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signByte(byte) instruction_simplifier (after)
  /// CHECK-DAG:     <<Result:i\d+>> Compare
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signByte(byte) instruction_simplifier (after)
  /// CHECK-NOT:                     InvokeStaticOrDirect

  private static int signByte(byte x) {
    return Integer.signum(x);
  }

  /// CHECK-START: int Main.signShort(short) builder (after)
  /// CHECK-DAG:     <<Result:i\d+>> InvokeStaticOrDirect intrinsic:IntegerSignum
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signShort(short) instruction_simplifier (after)
  /// CHECK-DAG:     <<Result:i\d+>> Compare
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signShort(short) instruction_simplifier (after)
  /// CHECK-NOT:                     InvokeStaticOrDirect

  private static int signShort(short x) {
    return Integer.signum(x);
  }

  /// CHECK-START: int Main.signChar(char) builder (after)
  /// CHECK-DAG:     <<Result:i\d+>> InvokeStaticOrDirect intrinsic:IntegerSignum
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signChar(char) instruction_simplifier (after)
  /// CHECK-DAG:     <<Result:i\d+>> Compare
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signChar(char) instruction_simplifier (after)
  /// CHECK-NOT:                     InvokeStaticOrDirect

  private static int signChar(char x) {
    return Integer.signum(x);
  }

  /// CHECK-START: int Main.signInt(int) builder (after)
  /// CHECK-DAG:     <<Result:i\d+>> InvokeStaticOrDirect intrinsic:IntegerSignum
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signInt(int) instruction_simplifier (after)
  /// CHECK-DAG:     <<Result:i\d+>> Compare
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signInt(int) instruction_simplifier (after)
  /// CHECK-NOT:                     InvokeStaticOrDirect

  private static int signInt(int x) {
    return Integer.signum(x);
  }

  /// CHECK-START: int Main.signLong(long) builder (after)
  /// CHECK-DAG:     <<Result:i\d+>> InvokeStaticOrDirect intrinsic:LongSignum
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signLong(long) instruction_simplifier (after)
  /// CHECK-DAG:     <<Result:i\d+>> Compare
  /// CHECK-DAG:                     Return [<<Result>>]

  /// CHECK-START: int Main.signLong(long) instruction_simplifier (after)
  /// CHECK-NOT:                     InvokeStaticOrDirect

  private static int signLong(long x) {
    return Long.signum(x);
  }


  public static void testSignBoolean() throws Exception {
    Method signBoolean = Class.forName("Main2").getMethod("signBoolean", boolean.class);
    expectEquals(0, (int)signBoolean.invoke(null, false));
    expectEquals(1, (int)signBoolean.invoke(null, true));
  }

  public static void testSignByte() {
    expectEquals(-1, signByte((byte)Byte.MIN_VALUE));
    expectEquals(-1, signByte((byte)-64));
    expectEquals(-1, signByte((byte)-1));
    expectEquals(0, signByte((byte)0));
    expectEquals(1, signByte((byte)1));
    expectEquals(1, signByte((byte)64));
    expectEquals(1, signByte((byte)Byte.MAX_VALUE));
  }

  public static void testSignShort() {
    expectEquals(-1, signShort((short)Short.MIN_VALUE));
    expectEquals(-1, signShort((short)-12345));
    expectEquals(-1, signShort((short)-1));
    expectEquals(0, signShort((short)0));
    expectEquals(1, signShort((short)1));
    expectEquals(1, signShort((short)12345));
    expectEquals(1, signShort((short)Short.MAX_VALUE));
  }

  public static void testSignChar() {
    expectEquals(0, signChar((char)0));
    expectEquals(1, signChar((char)1));
    expectEquals(1, signChar((char)12345));
    expectEquals(1, signChar((char)Character.MAX_VALUE));
  }

  public static void testSignInt() {
    expectEquals(-1, signInt(Integer.MIN_VALUE));
    expectEquals(-1, signInt(-12345));
    expectEquals(-1, signInt(-1));
    expectEquals(0, signInt(0));
    expectEquals(1, signInt(1));
    expectEquals(1, signInt(12345));
    expectEquals(1, signInt(Integer.MAX_VALUE));

    for (int i = -11; i <= 11; i++) {
      int expected = 0;
      if (i < 0) expected = -1;
      else if (i > 0) expected = 1;
      expectEquals(expected, signInt(i));
    }
  }

  public static void testSignLong() {
    expectEquals(-1, signLong(Long.MIN_VALUE));
    expectEquals(-1, signLong(-12345L));
    expectEquals(-1, signLong(-1L));
    expectEquals(0, signLong(0L));
    expectEquals(1, signLong(1L));
    expectEquals(1, signLong(12345L));
    expectEquals(1, signLong(Long.MAX_VALUE));

    expectEquals(-1, signLong(0x800000007FFFFFFFL));
    expectEquals(-1, signLong(0x80000000FFFFFFFFL));
    expectEquals(1, signLong(0x000000007FFFFFFFL));
    expectEquals(1, signLong(0x00000000FFFFFFFFL));
    expectEquals(1, signLong(0x7FFFFFFF7FFFFFFFL));
    expectEquals(1, signLong(0x7FFFFFFFFFFFFFFFL));

    for (long i = -11L; i <= 11L; i++) {
      int expected = 0;
      if (i < 0) expected = -1;
      else if (i > 0) expected = 1;
      expectEquals(expected, signLong(i));
    }

    for (long i = Long.MIN_VALUE; i <= Long.MIN_VALUE + 11L; i++) {
      expectEquals(-1, signLong(i));
    }

    for (long i = Long.MAX_VALUE; i >= Long.MAX_VALUE - 11L; i--) {
      expectEquals(1, signLong(i));
    }
  }


  public static void main(String args[]) throws Exception {
    testSignBoolean();
    testSignByte();
    testSignShort();
    testSignChar();
    testSignInt();
    testSignLong();

    System.out.println("passed");
  }

  private static void expectEquals(int expected, int result) {
    if (expected != result) {
      throw new Error("Expected: " + expected + ", found: " + result);
    }
  }
}
