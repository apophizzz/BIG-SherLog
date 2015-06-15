package com.big.instrumentation.agent;

import com.big.instrumentation.jmx.JmxInstrumentationService;
import com.big.instrumentation.util.InstrumentationUtils;
import com.big.instrumentation.util.JavassistUtils;

import javax.management.*;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;

/**
 * Created by patrick.kleindienst on 05.06.2015.
 */
public class MBeanInstallerAgent {

	public static void premain(String agentArgs, Instrumentation instrumentation) {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objectName = new ObjectName("com.big.instrument:type=InstrumentationService");
			mBeanServer.registerMBean(new JmxInstrumentationService(instrumentation), objectName);
			InstrumentationUtils.setInstrumentation(instrumentation);
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		}
	}
}
