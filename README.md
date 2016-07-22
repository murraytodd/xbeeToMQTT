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

You might need to add something like... 

```java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar:.``` 

to the command that launches the program, where the first path is where the native library is kept. (Unfortunately, it will differ based on which OS you're using.)