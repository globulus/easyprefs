package net.globulus.easyprefs.processor.codegen;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;

import net.globulus.easyprefs.processor.PrefField;
import net.globulus.easyprefs.processor.PrefType;
import net.globulus.easyprefs.processor.util.FrameworkUtil;

import java.io.Writer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

import javawriter.EzprefsJavaWriter;

/**
 * Created by gordanglavas on 01/10/16.
 */
public class EasyPrefsCodeGen {

	public void generate(Filer filer,
						 String masterMethod,
						 List<PrefType> classes) {
		try {
			String packageName = FrameworkUtil.getEasyPrefsPackageName();
			String className = "EasyPrefs";

			JavaFileObject jfo = filer.createSourceFile(packageName + "." + className);
			Writer writer = jfo.openWriter();
			EzprefsJavaWriter jw = new EzprefsJavaWriter(writer);
			jw.emitPackage(packageName);
			jw.emitImports("java.util.Set");
			jw.emitImports("java.util.HashSet");
			jw.emitImports(FrameworkUtil.IMPORT_CONTEXT);
			jw.emitImports(FrameworkUtil.IMPORT_SHARED_PREFERENCES);
			jw.emitEmptyLine();

			jw.emitJavadoc("Generated class by @%s . Do not modify this code!", className);
			jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null);
			jw.emitEmptyLine();

			Set<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);

			jw.beginMethod("SharedPreferences", "getPrefs", modifiers,
					"Context", "context");
			jw.emitStatement("return %s(context)", masterMethod);
			jw.endMethod();

			jw.emitEmptyLine();
			jw.emitSingleLineComment("Basic storage methods");
			jw.emitEmptyLine();

			jw.beginMethod("void", "removePreferencesField", modifiers,
					"Context", "context", "String", "key");
			jw.emitStatement(FrameworkUtil.LINE_EDITOR_INIT);
			jw.emitStatement("editor.remove(key)");
			jw.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
			jw.endMethod();
			jw.emitEmptyLine();

			for (String type : Arrays.asList("int", "long", "float", "boolean", "String", "Set<String>")) {
				String method = type;
				boolean addRemove = false;
				if (type.equals(FrameworkUtil.TYPE_SET_STRING)) {
					method = "StringSet";
					addRemove = true;
				} else if (!type.equals("String")) {
					method = FrameworkUtil.capitalize(type);
				}

				jw.beginMethod("void", "putPreferencesField", modifiers,
						"Context", "context", "String", "key", type, "value");
				jw.emitStatement(FrameworkUtil.LINE_EDITOR_INIT);
				jw.emitStatement("editor.put%s(key, value)", method);
				jw.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
				jw.endMethod();
				jw.emitEmptyLine();

				jw.beginMethod(type, "getPreferencesField", modifiers,
						"Context", "context", "String", "key", type, "defaultValue");
				jw.emitStatement("return getPrefs(context).get%s(key, defaultValue)", method);
				jw.endMethod();
				jw.emitEmptyLine();

				if (addRemove) {
					jw.beginMethod("void", "addToPreferencesField", modifiers,
							"Context", "context", "String", "key", "String", "value");
					jw.emitStatement("Set<String> set = getPreferencesField(context, key, new HashSet<String>())");
					jw.emitStatement("set.add(value)");
					jw.emitStatement("putPreferencesField(context, key, set)");
					jw.endMethod();
					jw.emitEmptyLine();

					jw.beginMethod("void", "removeFromPreferencesField", modifiers,
							"Context", "context", "String", "key", "String", "value");
					jw.emitStatement("Set<String> set = getPreferencesField(context, key, new HashSet<String>())");
					jw.emitStatement("set.remove(value)");
					jw.emitStatement("putPreferencesField(context, key, set)");
					jw.endMethod();
					jw.emitEmptyLine();
				}
			}

			jw.emitSingleLineComment("Generated methods");
			jw.emitEmptyLine();

			EasyPrefsPartCodeGen codeGen = new EasyPrefsPartCodeGen();

			for (PrefType prefType : classes) {
				codeGen.generate(prefType, jw);
			}

			List<PrefField> clearables = Stream.of(classes)
					.filter(new Predicate<PrefType>() {
						@Override
						public boolean test(PrefType value) {
							return !value.staticClass;
						}
					})
					.map(new Function<PrefType, List<PrefField>>() {
						@Override
						public List<PrefField> apply(PrefType prefType) {
							return prefType.fields;
						}
					})
					.flatMap(new Function<List<PrefField>, Stream<PrefField>>() {
						@Override
						public Stream<PrefField> apply(List<PrefField> prefFields) {
							return Stream.of(prefFields);
						}
					})
					.filter(new Predicate<PrefField>() {
						@Override
						public boolean test(PrefField value) {
							return value.clear;
						}
					})
					.toList();
			GenericCodeGen.generateClearMethod(clearables, jw);

			jw.emitEmptyLine();
			jw.beginMethod("void", "clearAll", modifiers, "Context", "context");
			jw.emitStatement("clear(context)");
			List<PrefType> clearableClasses = Stream.of(classes)
					.filter(new Predicate<PrefType>() {
						@Override
						public boolean test(PrefType value) {
							return value.staticClass;
						}
					})
					.toList();
			for (PrefType prefType : clearableClasses) {
				jw.emitStatement("%s.clear(context)", prefType.name);
			}
			jw.endMethod();

			jw.endType();
			jw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
