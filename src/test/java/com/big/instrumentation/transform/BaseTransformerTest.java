package com.big.instrumentation.transform;

import com.big.instrumentation.dummy.DummyClass;
import com.big.instrumentation.integration.BaseCodeIntegrator;
import com.big.instrumentation.util.JavassistUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * A JUnit 4 test class for testing {@link BaseTransformer}.
 * 
 * Created by patrick.kleindienst on 02.06.2015.
 */

public class BaseTransformerTest {

	private BaseTransformer		classUnderTest;
	private Class				testClass;
	private String				testMethod;
	private ClassLoader			testClassLoader;
	private BaseCodeIntegrator	baseCodeIntegratorMock;

	@Before
	public void setUp() {
		testClass = DummyClass.class;
		testMethod = "printSomething";
		testClassLoader = DummyClass.class.getClassLoader();
		baseCodeIntegratorMock = mock(BaseCodeIntegrator.class);
		classUnderTest = new BaseTransformer(testClassLoader, testClass.getName(), testMethod, JavassistUtils.getSignaturesForMethod(testClass.getName(), testMethod).get(0), baseCodeIntegratorMock) {
		};

		when(
				baseCodeIntegratorMock.performCodeIntegration(classUnderTest.getClassName(), classUnderTest.getMethodName(), classUnderTest.getMethodSignature(), classUnderTest.getClassLoader(),
						JavassistUtils.getBytecodeFromClass(testClass))).thenReturn(JavassistUtils.getBytecodeFromClass(testClass));
	}

	/**
	 * Verify that we get an empty byte array if the provided class name does
	 * not match the name the {@link BaseTransformer} was constructed with.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReturnEmptyByteArrayOnClassMismatch() throws Exception {
		byte[] bytes = classUnderTest.transform(testClassLoader, "wrongClassName", null, null, new byte[] {});

		verify(baseCodeIntegratorMock, times(0)).performCodeIntegration(classUnderTest.getClassName(), classUnderTest.getMethodName(), classUnderTest.getMethodSignature(),
				classUnderTest.getClassLoader(), JavassistUtils.getBytecodeFromClass(classUnderTest.getClass()));
		assertThat("Class filtering by class name didn't work as expected!", bytes, equalTo(new byte[] {}));
	}

	/**
	 * Verify that we get an empty byte array if the provided class loader does
	 * not match the class loader the {@link BaseTransformer} was constructed
	 * with.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReturnEmptyByteArrayOnClassloaderMismatch() throws Exception {
		byte[] bytes = classUnderTest.transform(new URLClassLoader(new URL[] {}), testClass.getName(), null, null, new byte[] {});

		verify(baseCodeIntegratorMock, times(0)).performCodeIntegration(classUnderTest.getClassName(), classUnderTest.getMethodName(), classUnderTest.getMethodSignature(),
				classUnderTest.getClassLoader(), JavassistUtils.getBytecodeFromClass(classUnderTest.getClass()));
		assertThat("Class filtering by class loader didn't work as expected!", bytes, equalTo(new byte[] {}));
	}

	/**
	 * Verify that we get a correct result when {@link BaseTransformer} is
	 * called properly. For testing purposes the {@link BaseCodeIntegrator} mock
	 * simply returns the original bytes of the test class.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReturnTransformationBytesOnMatchingInput() throws Exception {
		byte[] bytes = classUnderTest.transform(testClassLoader, testClass.getName(), null, null, JavassistUtils.getBytecodeFromClass(testClass));

		verify(baseCodeIntegratorMock, times(1)).performCodeIntegration(classUnderTest.getClassName(), classUnderTest.getMethodName(), classUnderTest.getMethodSignature(),
				classUnderTest.getClassLoader(), JavassistUtils.getBytecodeFromClass(testClass));
		assertThat("Class filtering didn't work as expected!", bytes, equalTo(JavassistUtils.getBytecodeFromClass(testClass)));
	}
}
