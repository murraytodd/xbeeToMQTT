name := """xbee-receiver"""

version := "1.0"

scalaVersion := "2.11.7"

enablePlugins(JavaAppPackaging)

// resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
resolvers += "Digi Releases Repository" at "http://ftp1.digi.com/support/m-repo"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
	"org.scalatest" %% "scalatest" % "2.2.4" % "test",
	"com.typesafe" % "config" % "1.3.0",
	"org.slf4j" % "slf4j-simple" % "1.7.21",
	"org.log4s" %% "log4s" % "1.3.0")

bashScriptExtraDefines += """addJava "-Djava.libaray.path=/usr/lib/jni""""

cancelable in Global := true