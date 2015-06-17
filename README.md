# SherLog Debugging Tool #
## What is _SherLog_? ##
_SherLog_ enables the integration of logging statements into an Java Enterprise Application at runtime. It provides several out-of-the-box
implementations for performance measurement or logging of instance variables. Moreover, _SherLog_ can be extended in an easy manner,
offering interfaces for your own logging integration implementations. 

The __SherLog Debugging Tool__ has been developed in the context of my bachelor thesis at [_Bertsch Innovation GmbH_](http://bertschinnovation.com/) (Stuttgart/Germany). 
Implemented to support developers and project leads in their daily work by faciliating quick debugging without stopping and restarting the application
server, it's name infers from the probably most popular detective in the world.



## How to build _SherLog_? ##
* Clone the repository to your machine
* Inside the root folder, start a Maven build calling _mvn package_
* After a successful build the generated JAR can be found in the _target_ subfolder



## How to prepare JBoss AS for _SherLog_?* ##
In order to be able to use SherLog, your JBoss AS configuration has to be customized. Assuming you're running a Windows machine, open __standalone.conf.bat__ in your favorite test editor and append the following lines __right on top__ of the file:

These lines prepare JBoss for being accessible via JMX:

+ _set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.port=1090"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl.need.client.auth=false"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl=false"_

Because _SherLog_ has to be ran as a Java agent, making JVM call a premain method right before regular server startup, we have to add additional JARs to JBoss classpath manually. Doing so, we can avoid JBoss complaining about missing logmanager setup on agent startup:

+ _set "JAVA_OPTS=%JAVA_OPTS% -Djava.util.logging.manager=org.jboss.logmanager.LogManager"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/jboss/logmanager/main/jboss-logmanager-1.2.2.GA.jar"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/jboss/logmanager/log4j/main/jboss-logmanager-log4j-1.0.0.GA.jar"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/apache/log4j/main/log4j-1.2.16.jar"_
+ _set "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.logmanager"_



## How to start _SherLog_? ##
With our JBoss configuration prepared, we're almost done. What's left do is to start our JBoss Application with the _-javaagent_ parameter and pass it the location of the _SherLog_ JAR we built at the beginning.

A possible setup might look like this: __-javaagent:C:\path\to\sherlog_jar\sherlog.jar__

Make sure that your application starts properly.


## How to connect to _SherLog_? ##
Having applied all the steps so far you now should be able to connect to _SherLog_. Open __jconsole__ and open a connection to your application. Inspect the list of MBeans registered in the running MBean server and search for a MBean named _com.big.sherlog.InstrumentationService_. 

__Congratulations, from now on you can enjoy simple runtime debugging with _SherLog_!__


 __*Caution: As for now, it will be exlpained how to setup and use _SherLog_ in a JBoss AS 7 environment. Further documentation for Tomcat etc. will follow soon.__
