# xbeeToMQTT
XBee Java controller that publishes incoming messages to MQTT

## Installing Dependencies

This projects has a few nested dependencies that we need in order to be able to
connect to and read from the XBee master controller.

1. DIGI XBee Java Library v1.1.1 [XBeejavaLibaray](https://github.com/digidotcom/XBeeJavaLibrary/releases)
2. RXTX Java serial library v2.2.x
3. The accompanying native serial communications libraries

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
