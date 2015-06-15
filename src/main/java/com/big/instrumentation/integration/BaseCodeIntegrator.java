package com.big.instrumentation.integration;

import java.io.IOException;

import javassist.*;

/**
 * Created by patrick.kleindienst on 01.06.2015.
 */
public abstract class BaseCodeIntegrator {

	protected static final String	PROVIDED_LOGGER	= "com.big.instrumentation.logger.LoggerProvider.LOGGER";

	protected abstract CtMethod enhanceMethodCode(CtClass ctClass, CtMethod ctMethod);

	public byte[] performCodeIntegration(String className, String methodName, String methodSignature, ClassLoader classLoader, byte[] bytes) {
		className = className.replace("/", ".");
		ClassPool classPool = new ClassPool(true);
		classPool.appendClassPath(new LoaderClassPath(classLoader));
		classPool.appendClassPath(new ByteArrayClassPath(className, bytes));

		try {
			CtClass ctClass = classPool.get(className);

			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				if (ctMethod.getName().equals(methodName)) {
					if (methodSignature != null && ctMethod.getSignature().equals(methodSignature)) {
						ctClass.removeMethod(ctMethod);
						ctClass.addMethod(enhanceMethodCode(ctClass, ctMethod));
					} else if (methodSignature == null) {
						ctClass.removeMethod(ctMethod);
						ctClass.addMethod(enhanceMethodCode(ctClass, ctMethod));
					}
				}
			}
			return ctClass.toBytecode();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
