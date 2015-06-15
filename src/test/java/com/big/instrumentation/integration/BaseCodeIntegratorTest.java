package com.big.instrumentation.integration;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;

import com.big.instrumentation.dummy.DummyClass;
import com.big.instrumentation.util.JavassistUtils;
import javassist.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by patrick.kleindienst on 01.06.2015.
 */
public class BaseCodeIntegratorTest {

	private BaseCodeIntegrator	baseCodeIntegrator;
	private ClassPool			classPool;
	private Class				dummyClass;

	@Before
	public void setUp() throws Exception {
		baseCodeIntegrator = new BaseCodeIntegrator() {
			@Override
			protected CtMethod enhanceMethodCode(CtClass ctClass, CtMethod ctMethod) {
				ctMethod.setName("newMethod");
				return ctMethod;
			}
		};
		classPool = new ClassPool(true);
		dummyClass = DummyClass.class;
	}

	// ########################################################
	// UNIT TESTS
	// ########################################################

	/**
	 * Test if adding a method to our {@link DummyClass} works as expected by
	 * invoking the code integration code and checking if the inserted method is
	 * actually present.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMethodInstrumentationInsertsCodeProperly() throws Exception {
		String methodSignature = JavassistUtils.getSignaturesForMethod(dummyClass.getName(), "printSomething").get(0);
		byte[] newBytes = baseCodeIntegrator.performCodeIntegration(dummyClass.getName(), "printSomething", methodSignature, dummyClass.getClassLoader(),
				JavassistUtils.getBytecodeFromClass(dummyClass));
		CtClass instrumentedClass = classPool.makeClass(new ByteArrayInputStream(newBytes));
		instrumentedClass.setName(dummyClass.getName() + "$Instr");
		instrumentedClass.toClass();
		instrumentedClass = classPool.get(dummyClass.getName() + "$Instr");

		assertThat(instrumentedClass.getDeclaredMethod("newMethod"), notNullValue());
	}

}
