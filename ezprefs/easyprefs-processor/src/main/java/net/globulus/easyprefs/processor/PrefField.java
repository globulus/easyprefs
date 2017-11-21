package net.globulus.easyprefs.processor;

import net.globulus.easyprefs.annotation.Pref;
import net.globulus.easyprefs.processor.util.ProcessorLog;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by gordanglavas on 30/09/16.
 */
public class PrefField {

	public final String fieldType;
	public final String fieldName;
	public final String key;
	public final String defaultValue;
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
		}
		this.key = key;
		if (element.getConstantValue() != null) {
			if (typeUtils.isSameType(type, elementUtils.getTypeElement("java.lang.String").asType())) {
				this.defaultValue = String.format("\"%s\"", element.getConstantValue());
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
					if (typeUtils.isSameType(type, elementUtils.getTypeElement("java.lang.String").asType())) {
						this.defaultValue = "\"\"";
					} else if (this.fieldType.contains("Set<String>")) {
						this.defaultValue = null;
					} else {
						this.defaultValue = null;
						error(element);
						break;
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

	private void error(Element element) {
		ProcessorLog.error(element, "Can't use " + fieldType + " as a pref field!");
		error = true;
	}
}
