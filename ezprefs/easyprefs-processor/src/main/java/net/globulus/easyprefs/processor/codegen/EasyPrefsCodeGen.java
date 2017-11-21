package net.globulus.easyprefs.processor.codegen;

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
			jw.emitImports("android.content.Context");
			jw.emitImports("android.content.SharedPreferences");
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

			final String editorInit = "SharedPreferences.Editor editor = getPrefs(context).edit()";
			final String editorCommit = "editor.commit()";

			jw.beginMethod("void", "removePreferencesField", modifiers,
					"Context", "context", "String", "key");
			jw.emitStatement(editorInit);
			jw.emitStatement("editor.remove(key)");
			jw.emitStatement(editorCommit);
			jw.endMethod();
			jw.emitEmptyLine();

			for (String type : Arrays.asList("int", "long", "float", "boolean", "String", "Set<String>")) {
				String method = type;
				boolean addRemove = false;
				if (type.equals("Set<String>")) {
					method = "StringSet";
					addRemove = true;
				} else if (!type.equals("String")) {
					method = type.substring(0, 1).toUpperCase() + type.substring(1);
				}

				jw.beginMethod("void", "putPreferencesField", modifiers,
						"Context", "context", "String", "key", type, "value");
				jw.emitStatement(editorInit);
				jw.emitStatement("editor.put%s(key, value)", method);
				jw.emitStatement(editorCommit);
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

			jw.endType();
			jw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
