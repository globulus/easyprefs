package net.globulus.easyprefs.processor;

import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;
import net.globulus.easyprefs.annotation.PrefMaster;
import net.globulus.easyprefs.annotation.PrefMethod;
import net.globulus.easyprefs.processor.codegen.EasyPrefsCodeGen;
import net.globulus.easyprefs.processor.codegen.MergeFileCodeGen;
import net.globulus.easyprefs.processor.util.FrameworkUtil;
import net.globulus.easyprefs.processor.util.ProcessorLog;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Processor extends AbstractProcessor {

	private static final List<Class<? extends Annotation>> ANNOTATIONS = Arrays.asList(
			PrefMaster.class,
			PrefClass.class,
			Pref.class
	);
	private static final String MERGED_FILE = "ezprefs.merged";

	private Elements mElementUtils;
	private Types mTypeUtils;
	private Filer mFiler;

	@Override
	public synchronized void init(ProcessingEnvironment env) {
		super.init(env);

		ProcessorLog.init(env);

		mElementUtils = env.getElementUtils();
		mTypeUtils = env.getTypeUtils();
		mFiler = env.getFiler();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new LinkedHashSet<>();
		for (Class<? extends Annotation> annotation : ANNOTATIONS) {
			types.add(annotation.getCanonicalName());
		}
		return types;
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

		String masterMethod = null;
		List<PrefType> prefTypes = new ArrayList<>();
		List<ExposedMethod> exposedMethods = new ArrayList<>();

		boolean foundMaster = false;
		String masterTag = PrefMaster.class.getSimpleName();
		String masterError = masterTag + " must of of format: public static SharedPreferences NAME(Context context)!";
		for (Element element : roundEnv.getElementsAnnotatedWith(PrefMaster.class)) {
			if (foundMaster) {
				ProcessorLog.error(element, "Found more than one " + masterTag + "!");
				return false;
			}
			foundMaster = true;
			if (!element.getModifiers().contains(Modifier.PUBLIC)
					|| !element.getModifiers().contains(Modifier.STATIC)) {
				ProcessorLog.error(element, masterError);
				return false;
			}
			ExecutableType method = (ExecutableType) element.asType();
			TypeMirror sharedPrefs = mElementUtils.getTypeElement(FrameworkUtil.IMPORT_SHARED_PREFERENCES).asType();
			if (!mTypeUtils.isSameType(method.getReturnType(), sharedPrefs)) {
				ProcessorLog.error(element, masterError);
				return false;
			}
			List<? extends TypeMirror> params = method.getParameterTypes();
			if (params.size() != 1) {
				ProcessorLog.error(element, masterError);
				return false;
			}
			TypeMirror param0 = method.getParameterTypes().get(0);
			TypeMirror context = mElementUtils.getTypeElement(FrameworkUtil.IMPORT_CONTEXT).asType();
			if (!mTypeUtils.isSameType(param0, context)) {
				ProcessorLog.error(element, masterError);
				return false;
			}
			TypeElement declaringClass = (TypeElement) element.getEnclosingElement();
			masterMethod = declaringClass.getQualifiedName().toString() + "." + element.getSimpleName();
		}

		for (Element element : roundEnv.getElementsAnnotatedWith(PrefMethod.class)) {
			if (!element.getModifiers().contains(Modifier.PUBLIC)
					|| !element.getModifiers().contains(Modifier.STATIC)) {
				ProcessorLog.error(element, PrefMethod.class.getSimpleName() + " methods must be public static!");
				return false;
			}
			exposedMethods.add(new ExposedMethod(element));
		}

//		if (!foundMaster) {
//			ProcessorLog.error(null, "Unable to find " + masterTag);
//		}
		Boolean shouldMerge = null;
		for (Element element : roundEnv.getElementsAnnotatedWith(PrefClass.class)) {
			if (!isValid(element)) {
				continue;
			}

			List<PrefField> fields = new ArrayList<>();

			PrefClass annotation = element.getAnnotation(PrefClass.class);
			boolean autoInclude = annotation.autoInclude();

			if (annotation.origin()) {
				shouldMerge = false;
			} else if (shouldMerge == null) {
				shouldMerge = true;
			}

			List<? extends Element> memberFields = mElementUtils.getAllMembers((TypeElement) element);

			if (memberFields != null) {
				for (Element member : memberFields) {
					if (member.getKind() != ElementKind.FIELD || !(member instanceof VariableElement)) {
						continue;
					}

					if (!autoInclude) {
						Pref prefAnnotation = member.getAnnotation(Pref.class);
						if (prefAnnotation == null) {
							continue;
						}
					}

					PrefField prefField = PrefField.get((VariableElement) member, mElementUtils, mTypeUtils);
					if (prefField == null) {
						return false;
					}
					fields.add(prefField);
				}

				prefTypes.add(new PrefType(element.getSimpleName().toString(), fields, annotation.staticClass()));
			}
		}

		EasyPrefsCodeGen.Input input = new EasyPrefsCodeGen.Input(masterMethod, prefTypes, exposedMethods);
		if (shouldMerge != null && shouldMerge) {
			ByteBuffer buffer = ByteBuffer.allocate(50_000);
			try {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					Class mergeClass = Class.forName(FrameworkUtil.getEasyPrefsPackageName() + "." + MergeFileCodeGen.CLASS_NAME + i);
					buffer.put((byte[]) mergeClass.getField(MergeFileCodeGen.MERGE_FIELD_NAME).get(null));
					if (!mergeClass.getField(MergeFileCodeGen.NEXT_FIELD_NAME).getBoolean(null)) {
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			try {
				EasyPrefsCodeGen.Input merge = EasyPrefsCodeGen.Input.fromBytes(buffer.array());
				input = input.mergedUp(merge);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		new MergeFileCodeGen().generate(mFiler, input);

		new EasyPrefsCodeGen().generate(mFiler, input);

		return true;
	}

	private boolean isValid(Element element) {
		if (element.getKind() != ElementKind.CLASS) {
			ProcessorLog.error(element,
			"Element %s is annotated with @%s but is not a class. Only Classes are supported",
					element.getSimpleName(), PrefClass.class.getSimpleName());
			return false;
		}
		return true;
	}
}
