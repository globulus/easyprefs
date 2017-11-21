package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.PrefField;
import net.globulus.easyprefs.processor.util.FrameworkUtil;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
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
      String name = FrameworkUtil.capitalize(field.fieldName);

      jw.beginMethod(field.fieldType, "get" + name, modifiers,
              "Context", "context");
      jw.emitStatement("return getPreferencesField(context, \"%s\", %s)", field.key, defaultValue);
      jw.endMethod();

      jw.beginMethod("void", "put" + name, modifiers,
              "Context", "context", field.fieldType, "value");
      jw.emitStatement("putPreferencesField(context, \"%s\", value)", field.key);
      jw.endMethod();

      if (field.fieldType.equals("Set<String>")) {
          jw.beginMethod("void", "addTo" + name, modifiers,
                  "Context", "context", "String", "value");
          jw.emitStatement("addToPreferencesField(context, \"%s\", value)", field.key);
          jw.endMethod();

          jw.beginMethod("void", "removeFrom" + name, modifiers,
                  "Context", "context", "String", "value");
          jw.emitStatement("removeFromPreferencesField(context, \"%s\", value)", field.key);
          jw.endMethod();
      }
    }

    public static void generateClearMethod(List<PrefField> clearables,
                                           EzprefsJavaWriter jw) throws IOException {
        jw.emitEmptyLine();
        jw.beginMethod("void", "clear", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC),
                "Context", "context");
        if (!clearables.isEmpty()) {
            jw.emitStatement(FrameworkUtil.LINE_EDITOR_INIT);
            for (PrefField field : clearables) {
                jw.emitStatement("editor.remove(\"%s\")", field.key);
            }
            jw.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
        }
        jw.endMethod();
    }
}
