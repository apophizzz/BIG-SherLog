package com.big.sherlog.service;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import com.big.sherlog.dummy.DummyClass;
import com.big.sherlog.exception.RuntimeInstrumentationException;
import com.big.sherlog.transform.BaseTransformer;
import com.big.sherlog.util.JavassistUtils;

/**
 * Created by patrick.kleindienst on 02.06.2015.
 */
public class InstrumentationServiceTest {

	private InstrumentationService	classUnderTest;
	private Instrumentation			mockInstrumentation;
	private BaseTransformer			mockBaseTransformer;
	private Class					dummyClass;
	private String					dummyMethodName;
	private byte[]					dummyByteCode;

	@Before
	public void setUp() throws Exception {
		dummyClass = DummyClass.class;
		dummyMethodName = "printSomething";
		dummyByteCode = JavassistUtils.getBytecodeFromClass(DummyClass.class);

		mockInstrumentation = mock(Instrumentation.class);
		when(mockInstrumentation.getAllLoadedClasses()).thenReturn(new Class[] { DummyClass.class });

		mockBaseTransformer = mock(BaseTransformer.class);
		when(mockBaseTransformer.transform(dummyClass.getClassLoader(), dummyClass.getName(), null, null, new byte[] {})).thenReturn(dummyByteCode);

		classUnderTest = new InstrumentationService(mockInstrumentation, mockBaseTransformer);
	}

	// ########################################################
	// UNIT TESTS
	// ########################################################

	/**
	 * Check if an {@link RuntimeInstrumentationException} is thrown if the
	 * provided class name refers to a class that does not exist or has not been
	 * loaded so far.
	 *
	 * @throws Exception
	 */
	@Test(expected = RuntimeInstrumentationException.class)
	public void testThrowInstrumentationExceptionOnInvalidClassName() throws Exception {
		classUnderTest.doInstrumentation("com.big.fail.InvalidClassName", dummyMethodName, JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName).get(0));
		verify(mockInstrumentation, atLeastOnce()).getAllLoadedClasses();
	}

	/**
	 * Test if class identification by name works fine for a class that is known
	 * by the JVM.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInstrumentationAcceptsValidClassName() throws Exception {
		try {
			classUnderTest.doInstrumentation(dummyClass.getName(), dummyMethodName, JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName).get(0));
			verify(mockInstrumentation, atLeastOnce()).getAllLoadedClasses();
		} catch (RuntimeInstrumentationException e) {
			fail("Unexpected RuntimeInstrumentationException: " + e.getStackTrace());
		}
	}

	/**
	 * Check if an exception is thrown if a transformer object should be
	 * configured although it's still null.
	 * 
	 * @throws Exception
	 */
	@Test(expected = InvocationTargetException.class)
	public void testInitializeTransformerThrowsExceptionOnNullTransformer() throws Exception {
		classUnderTest.setTransformer(null);
		Method initializeTransformer = classUnderTest.getClass().getDeclaredMethod("initializeTransformer", ClassLoader.class, String.class, String.class, String.class);
		initializeTransformer.setAccessible(true);
		initializeTransformer.invoke(classUnderTest, dummyClass.getClassLoader(), dummyClass.getName(), dummyMethodName, JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName)
				.get(0));
	}

	/**
	 * Check if configuration works fine for an existing transformer instance.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInitializeTransformerWorksProperlyWithTransformer() throws Exception {
		try {
			Method initializeTransformer = classUnderTest.getClass().getDeclaredMethod("initializeTransformer", ClassLoader.class, String.class, String.class, String.class);
			initializeTransformer.setAccessible(true);
			initializeTransformer.invoke(classUnderTest, dummyClass.getClassLoader(), dummyClass.getName(), dummyMethodName,
					JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName).get(0));
		} catch (InvocationTargetException e) {
			fail("Unexpected InvocationTargetException: " + e.getStackTrace());
		}
		verifyTransformerConfigCalls();
	}

	/**
	 * Check if instr fails if instr instance is still null.
	 * 
	 * @throws Exception
	 */
	@Test(expected = InvocationTargetException.class)
	public void testInstrumentThrowsExceptionOnMissingInstrumentationObject() throws Exception {
		classUnderTest.setInstrumentation(null);
		Method instrument = classUnderTest.getClass().getDeclaredMethod("instrument", Class.class, String.class, String.class);
		instrument.setAccessible(true);
		instrument.invoke(classUnderTest, dummyClass, dummyMethodName, JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName).get(0));
	}

	/**
	 * Test if a transformer in use is added, used and finally removed as
	 * expected.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testInstrumentDelegatesToInstrumentationObject() throws Exception {
		try {
			Method instrument = classUnderTest.getClass().getDeclaredMethod("instrument", Class.class, String.class, String.class);
			instrument.setAccessible(true);
			instrument.invoke(classUnderTest, dummyClass, dummyMethodName, JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName).get(0));
		} catch (InvocationTargetException e) {
			fail("Unexpected InvocationTargetException: " + e.getStackTrace());
		}
		verifyTransformerUsageCalls();
	}

	/**
	 * Test if transformer is used correctly in order to restore the original
	 * class definition.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testRestoreClass() throws Exception {
		classUnderTest.restoreClass(dummyClass);

		assertThat(mockInstrumentation.removeTransformer(mockBaseTransformer), equalTo(false));
		verifyRestoreCalls();
	}

	// ########################################################
	// UTIL METHODS
	// ########################################################

	private void verifyTransformerConfigCalls() {
		verify(mockBaseTransformer, times(1)).setClassName(dummyClass.getName());
		verify(mockBaseTransformer, times(1)).setMethodName(dummyMethodName);
		verify(mockBaseTransformer, times(1)).setMethodSignature(JavassistUtils.getSignaturesForMethod(dummyClass.getName(), dummyMethodName).get(0));
		verify(mockBaseTransformer, times(1)).setClassLoader(dummyClass.getClassLoader());
	}

	private void verifyTransformerUsageCalls() throws Exception {
		verify(mockInstrumentation, times(1)).addTransformer(mockBaseTransformer, true);
		verify(mockInstrumentation, times(1)).retransformClasses(dummyClass);
		verify(mockInstrumentation, times(1)).removeTransformer(mockBaseTransformer);
	}

	private void verifyRestoreCalls() throws Exception {
		verify(mockInstrumentation, times(1)).retransformClasses(dummyClass);
		verify(mockBaseTransformer, times(0)).transform(dummyClass.getClassLoader(), dummyClass.getName(), null, null, new byte[] {});
	}
}
