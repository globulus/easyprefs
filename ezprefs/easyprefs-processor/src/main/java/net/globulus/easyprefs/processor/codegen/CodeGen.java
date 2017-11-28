package net.globulus.easyprefs.processor.codegen;

import java.io.IOException;

import javawriter.EzprefsJavaWriter;

public interface CodeGen<T> {

  void generateCode(T type, EzprefsJavaWriter jw) throws IOException;
}
