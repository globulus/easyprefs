package net.globulus.easyprefs.processor;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

/**
 * Created by gordanglavas on 28/11/2017.
 */

public class ExposedMethod {

    public final String name;
    public final String originalMethod;
    public final String returnType;
    public final List<String> params;
    public final List<String> thrown;

    public ExposedMethod(Element element) {
        ExecutableType method = (ExecutableType) element.asType();
        TypeElement declaringClass = (TypeElement) element.getEnclosingElement();
        this.name = element.getSimpleName().toString();
        this.originalMethod = declaringClass.getQualifiedName().toString() + "." + element.getSimpleName();
        this.returnType = method.getReturnType().toString();
        this.params = new ArrayList<>();
        int count = 0;
        for (TypeMirror param : method.getParameterTypes()) {
            this.params.add(param.toString());
            String[] components = param.toString().toLowerCase().split("\\.");
            String paramName = components[components.length - 1];
            if (paramName.endsWith(">")) {
                paramName = paramName.substring(0, paramName.length() - 1);
            }
            this.params.add(paramName + count);
            count++;
        }
        this.thrown = Stream.of(method.getThrownTypes()).map(new Function<TypeMirror, String>() {
            @Override
            public String apply(TypeMirror typeMirror) {
                return typeMirror.toString();
            }
        }).toList();
    }
}
