package com.big.sherlog;

import com.big.sherlog.integration.BaseCodeIntegratorTest;
import com.big.sherlog.service.InstrumentationServiceTest;
import com.big.sherlog.transform.BaseTransformerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by patrick.kleindienst on 02.06.2015.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({ BaseCodeIntegratorTest.class, BaseTransformerTest.class, InstrumentationServiceTest.class })
public class InstrumentationTestSuite {

}
