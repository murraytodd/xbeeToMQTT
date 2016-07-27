# xbeeToMQTT

XBee Java controller that publishes incoming messages to MQTT

	This project has just been converted from Java to Scala. The old Java
	version can be found in the java-legacy branch. The master branch will
	leverage Scala. (I'm happy to say, a lot of the tough code that was
	worth saving is still in src/main/java/.../arduino/data, and Scala is
	just able to use it straight away. 

## What xbeeToMQTT does

This application serves as a (currently one-way) gateway from IoT devices on an
XBee radio network to any basic MQTT broker. It handles the tricky work of dealing
with ZigBee mesh networks, moving the messages into something that's arguably more
computer/network friendly.

This server was designed to run on a Raspberry Pi attached to Ethernet (specifically,
my home network) so it could act as my home-IoT server. I have installed the 
easy-to-use [Mosquitto](https://mosquitto.org) server on my Raspberry Pi for the
MQTT broker, but you could just as easily use a MQTT broker that is part of some
cloud IoT platform.

## Installing Dependencies

This projects has a few nested dependencies that we need in order to be able to
connect to and read from the XBee master controller.

1. DIGI XBee Java Library v1.1.1 [XBeejavaLibaray](https://github.com/digidotcom/XBeeJavaLibrary/releases)
2. RXTX Java serial library v2.2.x
3. The accompanying native serial communications libraries

These dependencies are most easily managed by downloading the `xjblib-1.1.1.jar` and `RXTXcomm.jar` files and placing them in the `/lib` directory. You should be able to either package them into a single-jar assembly or manually setup your classpath to point to the right libraries when you're in production. The next sections discuss these dependencies a little bit.

### XBee Java Libaray v1.1.1

In order to build this project in Maven, we are going to assume that you are going 
to "install" the proper XBee library into your own local maven repository. 

* Download the binary release of [XBeeJavaLibrary v1.1.1](https://github.com/digidotcom/XBeeJavaLibrary/releases)
* Unpack the file and go into the `XBJL-1.1.1` directory
* Execute the following: (on one line)

```
mvn install:install-file -Dfile=xbjlib-1.1.1.jar -DgroupId=com.digi.xbee 
                         -DartifactId=xbjlib -Dversion=1.1.1 -Dpackaging=jar
```

### RXTX v2.2.x Java and native libraries

If you want to run RXTX on Linux (including the Raspberry Pi) you might want to look for the library via your natural package manager. For example, on Raspbian you can simply say:

```sudo apt-get install librxtx-java```

...in order to get your ```/usr/share/java/RXTXcomm-2.2pre2.jar``` file and its
accompanying native files in ```/usr/lib/jni/```, specifically the needed ```librxtxSerial.so``` library.

If you are wanting to test/run this on something other than Linux, such as a Mac laptop, just go into the ```/extra-libs/native``` subdirectory to find your Mac OS X or Windows  (or Linux on Intel) libraries.

#### If the program has runtime problems...

If you see an error message like 

```java.lang.UnsatisfiedLinkError: no rxtxSerial in java.library.path thrown while loading gnu.io.RXTXCommDriver```

You'll need to help the Java Virtual Machine find the native library by either setting your `LD_LIBRARY_PATH` environment variable with

```export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:/usr/lib/jni``` 

or specifying with your java command such as

```java -Djava.library.path=/usr/lib/jni```...

Note again that this is OS-specific. The `/usr/lib/jni` path is where `librxtxSerial.so`, the native library used for the Raspberry Pi is kept. If you are using a Mac, you'll want to use whatever directory that `librxtxSerial.jnilib` resides.

#### Misc Serial Port problems with Mac OS X

If you want get an error that reads like `com.digi.xbee.api.exceptions.InterfaceInUseException: Port /dev/tty.usbserial-FTH14070 is already in use by other application(s)` and looking further down you see `Caused by: gnu.io.PortInUseException: Unknown Application` then your problem is actually that the RXTX library assumes there's a `/var/lock` directory.

Try doing the following (credit goes to [Jerome Bernard](http://www.jerome-bernard.com/blog/2011/11/18/serial-ports-on-mac-os-x/) for this):
```
sudo mkdir /var/lock
sudo chmod go+rwx /var/lock
```
