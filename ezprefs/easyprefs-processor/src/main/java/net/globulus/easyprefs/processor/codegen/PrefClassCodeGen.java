package net.globulus.easyprefs.processor.codegen;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;

import net.globulus.easyprefs.processor.PrefField;
import net.globulus.easyprefs.processor.PrefType;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import javax.lang.model.element.Modifier;

import javawriter.EzprefsJavaWriter;

public class PrefClassCodeGen implements CodeGen<PrefType> {

    public void generateCode(PrefType type, EzprefsJavaWriter jw) throws IOException {
    	if (type.staticClass) {
    		jw.emitEmptyLine();
			jw.beginType(type.name, "class", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), null);
			jw.emitEmptyLine();
		}

		PrefFieldCodeGen codeGen = new PrefFieldCodeGen();
		for (PrefField field : type.fields) {
			codeGen.generateCode(field, jw);
		}

		if (type.staticClass) {
			List<PrefField> clearables = Stream.of(type.fields)
					.filter(new Predicate<PrefField>() {
						@Override
						public boolean test(PrefField value) {
							return value.clear;
						}
					})
					.toList();
			PrefFieldCodeGen.generateClearMethod(clearables, jw);

			jw.endType();
			jw.emitEmptyLine();
		}
    }
}
