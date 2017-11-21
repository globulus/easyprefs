package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.PrefField;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import javax.lang.model.element.Modifier;

import javawriter.EzprefsJavaWriter;

public class GenericCodeGen implements FieldCodeGen {

    @Override
    public void generateCode(PrefField field, EzprefsJavaWriter jw) throws IOException {
      Set<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);

      String defaultValue;
      if (field.defaultValue.equals("null")) {
          defaultValue = String.format("(%s) null", field.fieldType);
      } else {
          defaultValue = field.defaultValue;
      }

      jw.beginMethod(field.fieldType, "get" + field.fieldName, modifiers,
              "Context", "context");
      jw.emitStatement("return getPreferencesField(context, \"%s\", %s)", field.key, defaultValue);
      jw.endMethod();

      jw.beginMethod("void", "put" + field.fieldName, modifiers,
              "Context", "context", field.fieldType, "value");
      jw.emitStatement("putPreferencesField(context, \"%s\", value)", field.key);
      jw.endMethod();

      if (field.fieldType.equals("Set<String>")) {
          jw.beginMethod("void", "addTo" + field.fieldName, modifiers,
                  "Context", "context", "String", "value");
          jw.emitStatement("addToPreferencesField(context, \"%s\", value)", field.key);
          jw.endMethod();

          jw.beginMethod("void", "removeFrom" + field.fieldName, modifiers,
                  "Context", "context", "String", "value");
          jw.emitStatement("removeFromPreferencesField(context, \"%s\", value)", field.key);
          jw.endMethod();
      }
    }
}
