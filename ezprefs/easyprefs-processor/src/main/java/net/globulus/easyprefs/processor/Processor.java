package net.globulus.easyprefs.processor;

import net.globulus.easyprefs.annotation.PrefsMaster;
import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefClass;
import net.globulus.easyprefs.processor.codegen.EasyPrefsCodeGen;
import net.globulus.easyprefs.processor.util.FrameworkUtil;
import net.globulus.easyprefs.processor.util.ProcessorLog;

import java.lang.annotation.Annotation;
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
			PrefsMaster.class,
			PrefClass.class,
			Pref.class
	);

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

		boolean foundMaster = false;
		String masterTag = PrefsMaster.class.getSimpleName();
		String masterError = masterTag + " must of of format: public static SharedPreferences NAME(Context context)!";
		for (Element element : roundEnv.getElementsAnnotatedWith(PrefsMaster.class)) {
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

//		if (!foundMaster) {
//			ProcessorLog.error(null, "Unable to find " + masterTag);
//		}

		for (Element element : roundEnv.getElementsAnnotatedWith(PrefClass.class)) {
			if (!isValid(element)) {
				continue;
			}

			List<PrefField> fields = new ArrayList<>();

			PrefClass annotation = element.getAnnotation(PrefClass.class);
			boolean autoInclude = annotation.autoInclude();

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

		new EasyPrefsCodeGen().generate(mFiler, masterMethod, prefTypes);

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
