package net.globulus.easyprefs.processor.codegen;

import net.globulus.easyprefs.processor.PrefField;
import net.globulus.easyprefs.processor.util.FrameworkUtil;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Modifier;

import javawriter.EzprefsJavaWriter;

public class PrefFieldCodeGen implements CodeGen<PrefField> {

    private static final String GET_PREFERENCES_FIELD_FORMAT = "getPreferencesField(context, \"%s\", %s)";
    private static final String EDITOR_REMOVE_FORMAT = "editor.remove(\"%s\")";

    @Override
    public void generateCode(PrefField field, EzprefsJavaWriter jw) throws IOException {
      Set<Modifier> modifiers = EnumSet.of(Modifier.PUBLIC, Modifier.STATIC);

      if (!field.comment.isEmpty()) {
          jw.emitEmptyLine();
          jw.emitSingleLineComment("");
          jw.emitSingleLineComment(field.comment);
          jw.emitSingleLineComment("");
      }

      String defaultValue;
      if (field.defaultValue == null) {
          defaultValue = field.getRawDefaultValue();
      } else if (field.defaultValue.equals("null")) {
          defaultValue = String.format("(%s) null", field.fieldType);
      } else {
          defaultValue = field.defaultValue;
      }

      boolean hasOldKey = field.oldKey != null && !field.oldKey.isEmpty();
      if (hasOldKey) {
          defaultValue = String.format(GET_PREFERENCES_FIELD_FORMAT, field.oldKey, defaultValue);
      }

      String name = FrameworkUtil.capitalize(field.fieldName);

      String mappingFunction = field.getFunction();
      boolean hasMapping = (mappingFunction != null);
      String functionInstance;
      if (hasMapping) {
          functionInstance = String.format("%s mapper = new %s()", mappingFunction, mappingFunction);
      } else {
          functionInstance = null;
      }

      jw.emitEmptyLine();
      jw.beginMethod(field.fieldType, "get" + name, modifiers,
              "Context", "context");
      String getPrefString = String.format(GET_PREFERENCES_FIELD_FORMAT, field.key, defaultValue);
      if (hasMapping) {
          jw.emitStatement(functionInstance);
          jw.emitStatement("return mapper.get(%s)", getPrefString);
      } else {
          jw.emitStatement("return %s", getPrefString);
      }
      jw.endMethod();

      jw.emitEmptyLine();
      if (field.autoset == null || field.autoset.isEmpty()) {
          jw.beginMethod("void", "put" + name, modifiers,
                  "Context", "context", field.fieldType, "value");
          String value;
          if (hasMapping) {
              jw.emitStatement(functionInstance);
              value = "mapper.put(value)";
          } else {
              value = "value";
          }
          jw.emitStatement("putPreferencesField(context, \"%s\", %s)", field.key, value);
      } else {
          jw.beginMethod("void", "put" + name, modifiers,
                  "Context", "context");
          String autoset;
          if (field.fieldType.equals("String")) {
              autoset = "\"" + field.autoset + "\"";
          } else {
              autoset = field.autoset;
          }
          String value;
          if (hasMapping) {
              jw.emitStatement(functionInstance);
              value = "mapper.put(" + autoset + ")";
          } else {
              value = autoset;
          }
          jw.emitStatement("putPreferencesField(context, \"%s\", %s)", field.key, value);
      }
      jw.endMethod();

      if (field.fieldType.contains(FrameworkUtil.TYPE_SET_STRING)) {
          jw.emitEmptyLine();
          jw.beginMethod("void", "addTo" + name, modifiers,
                  "Context", "context", "String", "value");
          jw.emitStatement("addToPreferencesField(context, \"%s\", value)", field.key);
          jw.endMethod();

          jw.emitEmptyLine();
          jw.beginMethod("void", "removeFrom" + name, modifiers,
                  "Context", "context", "String", "value");
          jw.emitStatement("removeFromPreferencesField(context, \"%s\", value)", field.key);
          jw.endMethod();
      }

      if (field.addClearMethod) {
          jw.emitEmptyLine();
          jw.beginMethod("void", "clear" + name, modifiers,
                  "Context", "context");
          jw.emitStatement(FrameworkUtil.LINE_EDITOR_INIT);
          jw.emitStatement(EDITOR_REMOVE_FORMAT, field.key);
          if (hasOldKey) {
              jw.emitStatement(EDITOR_REMOVE_FORMAT, field.oldKey);
          }
          jw.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
          jw.endMethod();
      }

        if (!field.comment.isEmpty()) {
            jw.emitEmptyLine();
            jw.emitSingleLineComment("");
            jw.emitSingleLineComment("END " + field.comment);
            jw.emitSingleLineComment("");
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
                jw.emitStatement(EDITOR_REMOVE_FORMAT, field.key);
                if (field.oldKey != null && !field.oldKey.isEmpty()) {
                    jw.emitStatement(EDITOR_REMOVE_FORMAT, field.oldKey);

                }
            }
            jw.emitStatement(FrameworkUtil.LINE_EDITOR_COMMIT);
        }
        jw.endMethod();
    }
}
