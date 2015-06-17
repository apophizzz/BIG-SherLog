# BIG-SherLog
## What is SherLog? ##
_SherLog_ is a tool enabling integration of logging statements into an Java Enterprise Application at runtime.

## How to build SherLog? ##
* Clone the repository to your machine
* Inside the root folder, start a Maven build calling _mvn package_
* After a successful build the generated JAR can be found in the _target_ subfolder



## How to prepare JBoss AS for _SherLog_?* ##
In order to be able to use SherLog, your JBoss AS configuration has to be customized. Assuming you're running a Windows machine, open __standalone.conf.bat__ in your favorite test editor and append the following lines __right on top__ of the file:

These lines prepare JBoss for being accessable via JMX:

+ set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.port=1090"
+ set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false"
+ set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl.need.client.auth=false"
+ set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote"
+ set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl=false"

Because _SherLog_ has to be ran as a agent, making JVM call a premain method right before regular server startup, we have to add additional JARs to JBoss classpath manually. Doing so, we can avoid JBoss complaining about missing logmanager setup on agent startup:

+ set "JAVA_OPTS=%JAVA_OPTS% -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
+ set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/jboss/logmanager/main/jboss-logmanager-1.2.2.GA.jar"
+ set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/jboss/logmanager/log4j/main/jboss-logmanager-log4j-1.0.0.GA.jar"
+ set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/apache/log4j/main/log4j-1.2.16.jar"
+ set "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.logmanager"



## How to start SherLog properly? ##
With our JBoss configuration customized, we're almost done. What's left do is to start JBoss with the _-javaagent_ parameter and pass it the location of the _SherLog_ JAR we built at the beginning.

A possible setup might look like this: __-javaagent:C:\path\to\sherlog_jar\sherlog.jar__


 *Caution: As for now, it will be exlpained how to setup and use _SherLog_ in an __JBoss AS 7 environment__. Further documentation for Tomcat etc. will follow soon.
