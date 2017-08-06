package com.andframe.processor;

import com.andframe.processor.model.Id;
import com.andframe.processor.model.IdQualified;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.sun.source.util.Trees;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.andframe.processor.constant.Constants.BINDINGS;
import static com.andframe.processor.constant.Constants.LISTENERS;
import static com.andframe.processor.constant.Constants.OPTION_DEBUGGABLE;
import static com.andframe.processor.constant.Constants.OPTION_SDK_INT;
import static com.andframe.processor.constant.Constants.RESOURCES;

@AutoService(Processor.class)
public class AndFrameProcessor extends AbstractProcessor {


    private int sdk;
    private boolean debuggable;
    private Filer filer;
    private Trees trees;
    private Types typeUtils;
    private Elements elementUtils;
    private final Map<IdQualified, Id> symbols = new LinkedHashMap<>();


    //<editor-fold desc="重写方法">
    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        typeUtils = env.getTypeUtils();
        elementUtils = env.getElementUtils();

        debuggable = !"false".equals(env.getOptions().get(OPTION_DEBUGGABLE));

        try {
            trees = Trees.instance(processingEnv);
        } catch (IllegalArgumentException ignored) {
        }

        String sdk = env.getOptions().get(OPTION_SDK_INT);
        if (sdk != null) {
            try {
                this.sdk = Integer.parseInt(sdk);
            } catch (NumberFormatException e) {
                env.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "Unable to parse supplied minSdk option '"
                                + sdk
                                + "'. Falling back to API 1 support.");
            }
        }
    }

    @Override
    public Set<String> getSupportedOptions() {
        return ImmutableSet.of(OPTION_SDK_INT, OPTION_DEBUGGABLE);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {

        return false;
    }

    //</editor-fold>

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.addAll(BINDINGS);
        annotations.addAll(RESOURCES);
        annotations.addAll(LISTENERS);
        return annotations;
    }
}
