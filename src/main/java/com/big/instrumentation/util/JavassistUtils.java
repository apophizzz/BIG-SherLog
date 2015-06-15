package com.big.instrumentation.util;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick.kleindienst on 02.06.2015.
 */
public class JavassistUtils {

	private static ClassPool		classPool	= new ClassPool(true);
	public static Instrumentation	instr		= null;

	public static List<String> getSignaturesForMethod(String fullQualifiedClassName, String methodName) {
		init();
		List<String> signatures = new ArrayList<>();
		try {
			CtClass ctClass = classPool.get(fullQualifiedClassName);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				if (ctMethod.getName().equalsIgnoreCase(methodName)) {
					signatures.add(ctMethod.getSignature());
				}
			}

		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return signatures;
	}

	public static byte[] getBytecodeFromClass(Class aClass) {
		init();
		try {
			CtClass ctClass = classPool.get(aClass.getName());
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

	public static void setInstrumentation(Instrumentation instrumentation) {
		instr = instrumentation;
	}

	private static void init() {
		if (instr != null) {
			for (Class aClass : instr.getAllLoadedClasses()) {
				classPool.appendClassPath(new LoaderClassPath(aClass.getClassLoader()));
			}
		}

	}
}
