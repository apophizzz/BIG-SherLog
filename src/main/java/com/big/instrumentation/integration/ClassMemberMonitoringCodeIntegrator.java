package com.big.instrumentation.integration;

import com.big.instrumentation.util.InstrumentationUtils;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.reflect.Field;

/**
 * Created by patrick.kleindienst on 11.06.2015.
 */
public class ClassMemberMonitoringCodeIntegrator extends BaseCodeIntegrator
{

    @Override protected CtMethod enhanceMethodCode(CtClass ctClass, CtMethod ctMethod)
    {
        try
        {
            ctMethod.insertAfter(buildLoggingStatement(ctClass, ctMethod));
        }
        catch (CannotCompileException e)
        {
            e.printStackTrace();
        }
        return ctMethod;
    }

    private String buildLoggingStatement(CtClass ctClass, CtMethod ctMethod)
    {
        StringBuilder builder = new StringBuilder();
        Class loadedClass = InstrumentationUtils.getLoadedClassByName(ctClass.getName());
        for (Field field : loadedClass.getDeclaredFields())
        {
            builder.append(PROVIDED_LOGGER + ".debug(\"Found Class member '" + field.getName() + "' with value: \"  + " + field.getName() + ");");
        }

        return builder.toString();
    }
}
