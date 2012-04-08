package com.g4share.jSynch.guice;

import com.g4share.jSynch.guice.annotations.DevelopmentEnvironment;
import com.g4share.jSynch.guice.annotations.ProductionEnvironment;

import java.lang.annotation.Annotation;

/**
 * User: gm
 * Date: 4/7/12
 */
public enum BinderEnvironment {
    PRODUCTION(0, ProductionEnvironment.class),
    DEVELOPMENT(1, DevelopmentEnvironment.class);

    private int code;
    private Class<? extends Annotation> annotation;

    private BinderEnvironment(int code, Class<? extends Annotation> annotation) {
        this.code = code;
        this.annotation = annotation;
    }

    public int getCode() {
        return code;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public static BinderEnvironment getDefault(){
        return BinderEnvironment.PRODUCTION;
    }

    public static BinderEnvironment fromString(String text) {
        if (text != null) {
            for (BinderEnvironment node : BinderEnvironment.values()) {
                if (node.name().equalsIgnoreCase(text)){
                    return node;
                }
            }
        }
        return null;
    }
}
