package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.PrefField;

import java.io.IOException;

import javawriter.EzprefsJavaWriter;

public interface FieldCodeGen {

  void generateCode(PrefField field, EzprefsJavaWriter jw) throws IOException;
}
