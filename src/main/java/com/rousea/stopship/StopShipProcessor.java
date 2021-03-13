package com.republicwireless.stopship;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import javax.lang.model.util.Types;
import javax.lang.model.type.PrimitiveType;

import java.util.Collections;
import java.util.Set;

public class StopShipProcessor extends AbstractProcessor {
  private Messager messager;
  private Types types;
  private Diagnostic.Kind logLevel;

  @Override
  public synchronized void init(ProcessingEnvironment env) {
    this.messager = env.getMessager();
    this.types = env.getTypeUtils();
    String type = env.getOptions().get("stopShip");
    this.logLevel = "debug".equals(type) ? Diagnostic.Kind.NOTE : Diagnostic.Kind.ERROR;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(StopShip.class.getName());
  }

  @Override
  public Set<String> getSupportedOptions() {
    return Collections.singleton("stopShip");
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
    for (TypeElement t : annotations) {
      for (Element annotatedElement : env.getElementsAnnotatedWith(t)) {
        if (annotatedElement.getKind() != ElementKind.FIELD) {
          messager.printMessage(logLevel, "StopShip can on be applied to fields");
          continue;
        }

        VariableElement element = (VariableElement) annotatedElement;
        Object value = element.getConstantValue();
        Element enclosing = element.getEnclosingElement();
        Name name = element.getSimpleName();

        if (value == null || !(value instanceof Boolean)) {
          String error = String.format("StopShip can on be applied to final booleans primitives:\n\tField %s %s is %s: %s",
                                          enclosing, name, element.asType(), value);
          messager.printMessage(logLevel, error);
          continue;
        }

        Boolean b = (Boolean) value;
        if (b.booleanValue()) {
          String msg = String.format("Debug flag %s %s enabled, STOP SHIP", enclosing, name);
          messager.printMessage(logLevel, msg);
        }
      }
    }
    return true;
  }
}
