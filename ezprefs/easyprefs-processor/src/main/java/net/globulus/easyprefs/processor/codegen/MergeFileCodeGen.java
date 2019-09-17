package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.util.FrameworkUtil;
import net.globulus.easyprefs.processor.util.ProcessorLog;

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

	public static final String CLASS_NAME = "EasyPrefsMerge_";
	public static final String MERGE_FIELD_NAME = "MERGE";
	public static final String NEXT_FIELD_NAME = "NEXT";

	public void generate(Filer filer, long timestamp, EasyPrefsCodeGen.Input input) {
		try {
			String packageName = FrameworkUtil.getEasyPrefsPackageName();
			byte[] bytes = convertToBytes(input);
			final int step = 8_000;
			for (int i = 0, count = 0; i < bytes.length; i += step, count++) {
				String className = CLASS_NAME + (timestamp + count);
				ProcessorLog.warn(null, "Creating file " + className);
				JavaFileObject jfo = filer.createSourceFile(packageName + "." + className);
				Writer writer = jfo.openWriter();
				try (EzprefsJavaWriter jw = new EzprefsJavaWriter(writer)) {
					jw.emitPackage(packageName);
					jw.emitEmptyLine();

					jw.emitJavadoc("Generated class by @%s. Do not modify this code!", className);
					jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null);
					jw.emitEmptyLine();

					jw.emitField("byte[]", MERGE_FIELD_NAME, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
							fromBytes(Arrays.copyOfRange(bytes, i, Math.min(bytes.length, i + step))));

					jw.emitField("boolean", NEXT_FIELD_NAME, EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
							Boolean.toString(i < bytes.length - step));

					jw.endType();
				}
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
