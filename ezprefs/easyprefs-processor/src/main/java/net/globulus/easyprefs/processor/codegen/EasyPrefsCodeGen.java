package net.globulus.easyprefs.processor.codegen;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import net.globulus.easyprefs.processor.ExposedMethod;
import net.globulus.easyprefs.processor.PrefField;
import net.globulus.easyprefs.processor.PrefType;
import net.globulus.easyprefs.processor.util.FrameworkUtil;
import net.globulus.easyprefs.processor.util.ProcessorLog;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
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

	private static final Set<Modifier> PUBLIC_MODIFIER = EnumSet.of(Modifier.PUBLIC);
	private static final Set<Modifier> PUBLIC_STATIC_MODIFIERS = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);

	public void generate(Filer filer, Input input) {
		try {
			String packageName = FrameworkUtil.getEasyPrefsPackageName();
			String className = "EasyPrefs";

			JavaFileObject jfo = filer.createSourceFile(packageName + "." + className);
			Writer writer = jfo.openWriter();
			try (EzprefsJavaWriter jw = new EzprefsJavaWriter(writer)) {
				jw.emitPackage(packageName);
				jw.emitImports("java.util.Set");
				jw.emitImports("java.util.HashSet");
				jw.emitImports(FrameworkUtil.IMPORT_CONTEXT);
				jw.emitImports(FrameworkUtil.IMPORT_SHARED_PREFERENCES);
				jw.emitEmptyLine();

				jw.emitJavadoc("Generated class by @%s. Do not modify this code!", className);
				jw.beginType(className, "class", EnumSet.of(Modifier.PUBLIC), null);
				jw.emitEmptyLine();

				jw.beginConstructor(EnumSet.of(Modifier.PRIVATE));
				jw.endConstructor();
				jw.emitEmptyLine();

				jw.emitSingleLineComment("Set IEasyPrefs instance");
				jw.beginStaticBlock();
				jw.emitStatement("IEasyPrefs.setInstance(new IEasyPrefs() {");

				writePrefMethods(jw, true, PUBLIC_MODIFIER,
						(w, args) ->
								w.emitStatement("EasyPrefs.putPreferencesField(context, key, value)"),
						(w, args) ->
								w.emitStatement("return EasyPrefs.getPreferencesField(context, key, defaultValue)")
				);
				writeRemoveMethod(jw, true, PUBLIC_MODIFIER, (w, args) ->
						w.emitStatement("EasyPrefs.removePreferencesField(context, key)"));

				jw.emitAnnotation(Override.class);
				jw.beginMethod("void", "clearAll", PUBLIC_MODIFIER, "Context", "context");
				jw.emitStatement("EasyPrefs.clearAll(context)");
				jw.endMethod();

				jw.emitStatement("})");
				jw.endStaticBlock();

				jw.beginMethod("SharedPreferences", "getPrefs", PUBLIC_STATIC_MODIFIERS,
						"Context", "context");
				jw.emitStatement("return %s(context)", input.masterMethod);
				jw.endMethod();

				jw.emitEmptyLine();
				jw.emitSingleLineComment("Basic storage methods");
				jw.emitEmptyLine();

				writeRemoveMethod(jw, false, PUBLIC_STATIC_MODIFIERS, (w, args) -> {
					w.emitStatement(FrameworkUtil.LINE_EDITOR_INIT);
					w.emitStatement("editor.remove(key)");
					w.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
				});

				writePrefMethods(jw, false, PUBLIC_STATIC_MODIFIERS,
						(w, args) -> {
					w.emitStatement(FrameworkUtil.LINE_EDITOR_INIT);
					w.emitStatement("editor.put%s(key, value)", args[0]);
					w.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
				}, (w, args) ->
					w.emitStatement("return getPrefs(context).get%s(key, defaultValue)", args[0]));

				jw.emitEmptyLine()
						.emitSingleLineComment("Generated methods")
						.emitEmptyLine();

				PrefClassCodeGen typeCodeGen = new PrefClassCodeGen();
				for (PrefType prefType : input.classes) {
					typeCodeGen.generateCode(prefType, jw);
				}

				PrefMethodCodeGen methodCodeGen = new PrefMethodCodeGen();
				for (ExposedMethod exposedMethod : input.methods) {
					methodCodeGen.generateCode(exposedMethod, jw);
				}

				List<PrefField> clearables = Stream.of(input.classes)
						.filter(value -> !value.staticClass)
						.map(prefType -> prefType.fields)
						.flatMap((Function<List<PrefField>, Stream<PrefField>>) Stream::of)
						.filter(value -> value.clear)
						.toList();
				PrefFieldCodeGen.generateClearMethod(clearables, jw);

				jw.emitEmptyLine();
				jw.beginMethod("void", "clearAll", PUBLIC_STATIC_MODIFIERS, "Context", "context");
				jw.emitStatement("clear(context)");
				List<PrefType> clearableClasses = Stream.of(input.classes)
						.filter(value -> value.staticClass)
						.toList();
				for (PrefType prefType : clearableClasses) {
					jw.emitStatement("%s.clear(context)", prefType.name);
				}
				jw.endMethod();

				jw.endType();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeRemoveMethod(EzprefsJavaWriter jw,
								   boolean isOverride,
								   Set<Modifier> modifiers,
								   PrefMethodBodyWriter bodyWriter) throws IOException {
		if (isOverride) {
			jw.emitAnnotation(Override.class);
		}
		jw.beginMethod("void", "removePreferencesField", modifiers,
				"Context", "context", "String", "key");
		bodyWriter.writeBody(jw);
		jw.endMethod();
		jw.emitEmptyLine();
	}

	private void writePrefMethods(EzprefsJavaWriter jw,
								  boolean isOverride,
								  Set<Modifier> modifiers,
								  PrefMethodBodyWriter putBodyWriter,
								  PrefMethodBodyWriter getBodyWriter) throws IOException {
		for (String type : Arrays.asList("int", "long", "float", "boolean", "String", "Set<String>")) {
			String method = type;
			boolean addRemove = false;
			if (type.equals("Set<String>")) {
				method = "StringSet";
				addRemove = true;
			} else if (!type.equals("String")) {
				method = FrameworkUtil.capitalize(type);
			}

			if (isOverride) {
				jw.emitAnnotation(Override.class);
			}
			jw.beginMethod("void", "putPreferencesField", modifiers,
					"Context", "context", "String", "key", type, "value");
			putBodyWriter.writeBody(jw, method);
			jw.endMethod();
			jw.emitEmptyLine();

			if (isOverride) {
				jw.emitAnnotation(Override.class);
			}
			jw.beginMethod(type, "getPreferencesField", modifiers,
					"Context", "context", "String", "key", type, "defaultValue");
			getBodyWriter.writeBody(jw, method);
			jw.endMethod();
			jw.emitEmptyLine();

			if (addRemove) {
				if (isOverride) {
					jw.emitAnnotation(Override.class);
				}
				jw.beginMethod("void", "addToPreferencesField", modifiers,
						"Context", "context", "String", "key", "String", "value");
				jw.emitStatement("Set<String> set = getPreferencesField(context, key, new HashSet<String>())");
				jw.emitStatement("set.add(value)");
				jw.emitStatement("putPreferencesField(context, key, set)");
				jw.endMethod();
				jw.emitEmptyLine();

				if (isOverride) {
					jw.emitAnnotation(Override.class);
				}
				jw.beginMethod("void", "removeFromPreferencesField", modifiers,
						"Context", "context", "String", "key", "String", "value");
				jw.emitStatement("Set<String> set = getPreferencesField(context, key, new HashSet<String>())");
				jw.emitStatement("set.remove(value)");
				jw.emitStatement("putPreferencesField(context, key, set)");
				jw.endMethod();
				jw.emitEmptyLine();
			}
		}
	}

	public static class Input implements Serializable {

		final String masterMethod;
		final List<PrefType> classes;
		final List<ExposedMethod> methods;

		public Input(String masterMethod, List<PrefType> classes, List<ExposedMethod> methods) {
			ProcessorLog.warn(null, "input constructor " + masterMethod);
			this.masterMethod = masterMethod;
			this.classes = classes;
			this.methods = methods;
		}

		public Input mergedUp(Input other) {
			String masterMethod = (other.masterMethod != null) ? other.masterMethod : this.masterMethod;
			ProcessorLog.warn(null, "merge up constructor " + masterMethod);
			List<PrefType> classes = new ArrayList<>(other.classes);
			classes.addAll(this.classes);
			List<ExposedMethod> methods = new ArrayList<>(other.methods);
			methods.addAll(this.methods);
			return new Input(masterMethod, classes, methods);
		}

		public static Input fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
			try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				 ObjectInput in = new ObjectInputStream(bis)) {
				return (Input) in.readObject();
			}
		}
	}

	private interface PrefMethodBodyWriter {
		void writeBody(EzprefsJavaWriter jw, String... args) throws IOException;
	}
}
