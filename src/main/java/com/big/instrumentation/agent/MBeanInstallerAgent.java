package com.big.instrumentation.agent;

import com.big.instrumentation.jmx.JmxInstrumentationService;
import com.big.instrumentation.util.InstrumentationUtils;
import com.big.instrumentation.util.JavassistUtils;
import javax.management.*;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;

/**
 * This class defines the agent's premain method which is called before the
 * application server bootstrapping process is started. Additionally, a
 * reference to the {@link Instrumentation} object received from JVM is handed
 * over to the utility classes.<br/>
 *
 * Created by patrick.kleindienst on 05.06.2015.
 * 
 * 
 * @author patrick.kleindienst
 * 
 */
public class MBeanInstallerAgent {

	public static void premain(String agentArgs, Instrumentation instrumentation) {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objectName = new ObjectName("com.big.instrument:type=InstrumentationService");
			mBeanServer.registerMBean(new JmxInstrumentationService(instrumentation), objectName);
			InstrumentationUtils.setInstrumentation(instrumentation);
			JavassistUtils.setInstrumentation(instrumentation);

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
