package net.globulus.easyprefs.processor;

import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.annotation.PrefFunctionStub;
import net.globulus.easyprefs.processor.util.FrameworkUtil;
import net.globulus.easyprefs.processor.util.ProcessorLog;

import java.io.Serializable;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by gordanglavas on 30/09/16.
 */
public class PrefField implements Serializable {

	public final String fieldType;
	public final String fieldName;
	public final String key;
	public final String defaultValue;
	public final String autoset;
	public final boolean clear;
	public final String comment;
	public final String oldKey;

	private String function;
	private String rawDefaultValue;
	private boolean error = false;

	private PrefField(VariableElement element,
					 Elements elementUtils,
					 Types typeUtils) {
		TypeMirror type = element.asType();
		this.fieldType = element.asType().toString();
		this.fieldName = element.getSimpleName().toString();
		Pref annotation = element.getAnnotation(Pref.class);
		String key = this.fieldName;
		if (annotation != null) {
			if (!annotation.key().isEmpty()) {
				key = annotation.key();
			}
			this.autoset = annotation.autoset();
			try {
				annotation.function();
			} catch (MirroredTypeException e) {
				this.function = e.getTypeMirror().toString();
				if (this.function != null && this.function.contains(PrefFunctionStub.class.getSimpleName())) {
					this.function = null;
				}
			}
			this.clear = annotation.clear();
			this.comment = annotation.comment();
			this.oldKey = annotation.oldKey();
			if (this.function != null && annotation.rawDefaultValue().isEmpty()) {
				ProcessorLog.error(element, "If you supply a mapping function, you must supply a rawDefaultValue as well.");
				error = true;
			}
			rawDefaultValue = annotation.rawDefaultValue();
		} else {
			this.clear = true;
			this.autoset = null;
			this.function = null;
			this.comment = "";
			this.oldKey = null;
		}
		this.key = key;
		if (element.getConstantValue() != null) {
			if (typeUtils.isSameType(type, elementUtils.getTypeElement(FrameworkUtil.IMPORT_STRING).asType())) {
				this.defaultValue = String.format("\"%s\"", element.getConstantValue());
			} else if (type.getKind() == TypeKind.LONG) {
				this.defaultValue = "" + element.getConstantValue() + "L";
			} else if (type.getKind() == TypeKind.FLOAT) {
				this.defaultValue = "" + element.getConstantValue() + "F";
			} else {
				this.defaultValue = "" + element.getConstantValue();
			}
		} else {
			switch (type.getKind()) {
				case BOOLEAN:
					this.defaultValue = "false";
					break;
				case BYTE:
				case SHORT:
				case INT:
					this.defaultValue = "0";
					break;
				case LONG:
					this.defaultValue = "0L";
					break;
				case FLOAT:
					this.defaultValue = "0F";
					break;
				case DECLARED: {
					if (typeUtils.isSameType(type, elementUtils.getTypeElement(FrameworkUtil.IMPORT_STRING).asType())) {
						if (annotation != null && annotation.nullable()) {
							this.defaultValue = "null";
						} else {
							this.defaultValue = "\"\"";
						}
					} else if (this.fieldType.contains(FrameworkUtil.TYPE_SET_STRING)) {
						if (annotation != null && annotation.nullable()) {
							this.defaultValue = "null";
						} else {
							this.defaultValue = "new HashSet<String>()";
						}
					} else if (this.function != null) {
						this.defaultValue = null;
					} else {
						this.defaultValue = null;
						error(element);
					}
				}
					break;
				default:
					this.defaultValue = null;
					error(element);
					break;
			}
		}
	}

	public static PrefField get(VariableElement element, Elements elementUtils, Types typeUtils) {
		PrefField field = new PrefField(element, elementUtils, typeUtils);
		if (field.error) {
			return null;
		}
		return field;
	}

	public String getFunction() {
		return function;
	}

	public String getRawDefaultValue() {
		return rawDefaultValue;
	}

	private void error(Element element) {
		ProcessorLog.error(element, "Can't use " + fieldType + " as a pref field!");
		error = true;
	}
}
