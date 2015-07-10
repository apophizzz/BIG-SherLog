package com.big.sherlog.agent;

import com.big.sherlog.jmx.JmxInstrumentationService;
import com.big.sherlog.util.InstrumentationUtils;
import com.big.sherlog.util.JavassistUtils;
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
public class SherlogSetupAgent
{

	private static Instrumentation	instrumentationImpl	= null;

	public static void premain(String agentArgs, Instrumentation instrumentation) {
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName objectName = new ObjectName("com.big.sherlog:type=SherlogService");
			mBeanServer.registerMBean(new JmxInstrumentationService(instrumentation), objectName);

			instrumentationImpl = instrumentation;

			// System.out.println("Agent: " +
			// SherlogSetupAgent.class.getClassLoader());

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

	public static Instrumentation getInstrumentation() {
		return instrumentationImpl;
	}
}
