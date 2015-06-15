package com.big.instrumentation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a
 * {@link com.big.instrumentation.transform.BaseTransformer} subclass as a
 * transformer and makes it available for
 * {@link com.big.instrumentation.scan.TransformerClasspathScanner}'s
 * auto-discovery.<br/>
 *
 * Created by patrick.kleindienst on 11.06.2015.
 * 
 * @author patrick.kleindienst
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Transformer {
}
