# SherLog Debugging Tool #

## What is _SherLog_? ##
_SherLog_ enables the integration of logging statements into an Java Enterprise Application at runtime. It provides several out-of-the-box
implementations for performance measurement or logging of instance variables. Moreover, _SherLog_ can be extended in an easy manner,
offering interfaces for your own logging integration implementations. 


## How to build _SherLog_? ##
Follow these steps to build and package _SherLog_:
```
$ git clone https://github.com/Patrick-Kleindienst/BIG-SherLog.git
$ cd BIG-SherLog/
$ mvn clean package
```
After a successful build the generated JAR can be found in the _target_ subfolder

__Caution: Make sure that the SherLog JAR resides on the classpath of the app you want to debug when it's built and deployed. Only then our class which is responsible for logging can be found by the JBoss AS ModuleClassloader!__


## How to prepare JBoss AS for _SherLog_?* ##
In order to be able to use SherLog, your JBoss AS configuration has to be customized. Assuming you're running a Windows machine, open __standalone.conf.bat__ in your favorite test editor and append the following lines __right on top__ of the file:

These lines prepare JBoss for being accessible via JMX:
```
set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.port=1090"
set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.authenticate=false"
set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl.need.client.auth=false"
set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote"
set "JAVA_OPTS=%JAVA_OPTS% -Dcom.sun.management.jmxremote.ssl=false"
```

Because _SherLog_ has to be ran as a Java agent, making JVM call a premain method right before regular server startup, we have to add additional JARs to JBoss classpath manually. Doing so, we can avoid JBoss complaining about missing logmanager setup on agent startup:

```
set "JAVA_OPTS=%JAVA_OPTS% -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/jboss/logmanager/main/jboss-logmanager-1.2.2.GA.jar"
set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/jboss/logmanager/log4j/main/jboss-logmanager-log4j-1.0.0.GA.jar"
set "JAVA_OPTS=%JAVA_OPTS% -Xbootclasspath/p:%JBOSS_HOME%/modules/org/apache/log4j/main/log4j-1.2.16.jar"
set "JAVA_OPTS=%JAVA_OPTS% -Djboss.modules.system.pkgs=org.jboss.logmanager"
```


## How to start _SherLog_? ##
With our JBoss configuration prepared, we're almost done. What's left do is to start our JBoss Application with the _-javaagent_ parameter and pass it the location of the _SherLog_ JAR we built at the beginning.

Extend your JBoss run configuration in your favorite IDE with the following JVM argument:
```
-javaagent:C:\path\to\sherlog_jar\sherlog.jar
```
In case you're running JBoss from command line, make sure the JVM argument is added to __standalone.conf.bat__.


## How to connect to _SherLog_? ##
Having applied all the steps so far you now should be able to connect to _SherLog_. Open __jconsole__ and setup a connection to your application, i.e. the according JBoss process. Inspect the list of MBeans registered in the running MBean server and search for a MBean named _com.big.sherlog.InstrumentationService_.
This MBean offers several operations that you can immediately try out in the context of your own application.


## How to extend _SherLog_? ##



## About _SherLog_:
The _SherLog Debugging Tool_ has been developed in the context of my bachelor thesis at [_Bertsch Innovation GmbH_](http://bertschinnovation.com/) (Stuttgart/Germany). 
Implemented to support developers and project leads in their daily work by faciliating quick debugging without stopping and restarting the application
server, it's name infers from the probably most popular detective in the world.


 __*Caution: As for now, it will be exlpained how to setup and use _SherLog_ in a JBoss AS 7 environment. Further documentation for Tomcat etc. will follow soon.__
