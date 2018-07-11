package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.util.FrameworkUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import javawriter.EzprefsJavaWriter;

/**
 * Created by gordanglavas on 01/10/16.
 */
public class MergeFileCodeGen {

	public void generate(Filer filer, EasyPrefsCodeGen.Input input) {
		try {
			String packageName = FrameworkUtil.getEasyPrefsPackageName();
			String className = "EasyPrefsMerge";

			JavaFileObject jfo = filer.createSourceFile(packageName + "." + className);
			Writer writer = jfo.openWriter();
			try (EzprefsJavaWriter jw = new EzprefsJavaWriter(writer)) {
				jw.emitPackage(packageName);
				jw.emitEmptyLine();

				jw.emitJavadoc("Generated class by @%s . Do not modify this code!", className);
				jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null);
				jw.emitEmptyLine();

				byte[] bytes = convertToBytes(input);
				jw.emitField("byte[]", "MERGE", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
						fromBytes(bytes));

				jw.endType();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String fromBytes(byte[] bytes) {
		return Arrays.toString(bytes).replace('[', '{').replace(']', '}');
	}

	private byte[] convertToBytes(Object object) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(object);
			return bos.toByteArray();
		}
	}
}
