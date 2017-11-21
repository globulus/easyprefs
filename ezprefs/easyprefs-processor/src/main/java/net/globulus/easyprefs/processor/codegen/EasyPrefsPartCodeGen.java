package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.PrefField;
import net.globulus.easyprefs.processor.PrefType;

import java.util.EnumSet;

import javax.lang.model.element.Modifier;

import javawriter.EzprefsJavaWriter;

public class EasyPrefsPartCodeGen {

    public void generate(PrefType type, EzprefsJavaWriter jw) throws Exception {
    	if (type.staticClass) {
    		jw.emitEmptyLine();
			jw.beginType(type.name, "class", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), null);
			jw.emitEmptyLine();
		}

		GenericCodeGen codeGen = new GenericCodeGen();
		for (PrefField field : type.fields) {
			codeGen.generateCode(field, jw);
		}

		if (type.staticClass) {
			jw.endType();
			jw.emitEmptyLine();
		}
    }
}
