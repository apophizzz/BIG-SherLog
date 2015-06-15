package com.big.instrumentation;

import com.big.instrumentation.integration.BaseCodeIntegratorTest;
import com.big.instrumentation.service.InstrumentationServiceTest;
import com.big.instrumentation.transform.BaseTransformerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by patrick.kleindienst on 02.06.2015.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ BaseCodeIntegratorTest.class, BaseTransformerTest.class, InstrumentationServiceTest.class })
public class InstrumentationTestSuite {

}
