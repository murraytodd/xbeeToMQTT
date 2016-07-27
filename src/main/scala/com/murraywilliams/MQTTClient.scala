package com.murraywilliams

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

object MQTTClient {
  
  private[this] val logger = org.log4s.getLogger
  
  lazy val mqttClient = {
    import XBeeReceiver.conf
    
    try {
      val memoryPersistence = new MemoryPersistence()
      val brokerUrl = conf.getString("mqtt.url")
      val client = new MqttClient(brokerUrl, "xbeeSensorsClientId", memoryPersistence)
      val opts = new MqttConnectOptions()
      opts.setCleanSession(true)
      logger.info(s"Connecting to broker ${brokerUrl}...")
      client.connect(opts)
      logger.info("Connected")
      Some(client)
    } catch {
      case t : Throwable => {
        logger.error(t)("Could not connect to MQTT server.")
        None
      }
    }
  }
  
  lazy val mqttTopicRoot = XBeeReceiver.conf.getString("mqtt.topic")
  
  def mqttSend(topicSuffix : String, message : Any) = {
    val msg = message match {
      case str : String => str.getBytes
      case byteArray : Array[Byte] => byteArray
      case _ => message.toString.getBytes
    }
    mqttClient match {
      case Some(client) if client.isConnected => client.publish(s"${mqttTopicRoot}/${topicSuffix}",new MqttMessage(msg))
      case None => logger.warn("No connection to MQTT broker. Not sending any data.")
    }      
  }
  
}