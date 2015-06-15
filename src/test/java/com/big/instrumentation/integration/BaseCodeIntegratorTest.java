package com.big.instrumentation.integration;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.lang.instrument.Instrumentation;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.Before;
import org.junit.Test;
import com.big.instrumentation.dummy.DummyClass;
import com.big.instrumentation.util.InstrumentationUtils;
import com.big.instrumentation.util.JavassistUtils;

/**
 * Created by patrick.kleindienst on 01.06.2015.
 */
public class BaseCodeIntegratorTest {

	private BaseCodeIntegrator	baseCodeIntegrator;
	private ClassPool			classPool;
	private Class				dummyClass;
	private Instrumentation		mockInstrumentation;

	@Before
	public void setUp() throws Exception {
		baseCodeIntegrator = new BaseCodeIntegrator() {
			@Override
			protected CtMethod enhanceMethodCode(CtMethod ctMethod) {
				ctMethod.setName("newMethod");
				return ctMethod;
			}
		};
		classPool = new ClassPool(true);
		dummyClass = DummyClass.class;

		mockInstrumentation = mock(Instrumentation.class);
		when(mockInstrumentation.getAllLoadedClasses()).thenReturn(new Class[] { DummyClass.class });

		InstrumentationUtils.setInstrumentation(mockInstrumentation);
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
