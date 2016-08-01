package com.murraywilliams

import scala.annotation.tailrec

object FloatHexUtil {

  def toFloat(hex: String, changeEndian : Boolean = false) : Float = {
    assert(hex.length==8)
    java.lang.Float.intBitsToFloat(java.lang.Long.parseLong(if (changeEndian) switchEndian(hex) else hex,16).intValue)
  }

  def toFloatList(hex: String, changeEndian : Boolean = false) : List[Float] = {
    assert((hex.length % 8) == 0)
    stringProcessGroups[Float](hex, 8, x => toFloat(x, changeEndian))
  }
  
  def toDouble(hex: String, changeEndian : Boolean = false) : Double = {
    assert(hex.length==16)
    java.lang.Double.longBitsToDouble(java.lang.Long.parseLong(if (changeEndian) switchEndian(hex) else hex,16))
  }

  def toDoubleList(hex: String, changeEndian : Boolean = false) : List[Double] = {
    assert((hex.length % 16) == 0)
    stringProcessGroups[Double](hex, 16, x => toDouble(x, changeEndian))
  }
  
  def floatToHex(float: Float, changeEndian : Boolean = false) : String = {
    val hex = java.lang.Integer.toHexString(java.lang.Float.floatToIntBits(float)).toUpperCase
    if (changeEndian) switchEndian(hex) else hex
  }

  def floatListToHex(list: List[Float], changeEndian : Boolean = false) : String = {
    listProcessGroups[Float](list, f => floatToHex(f, changeEndian))
  }
  
  def doubleToHex(double: Double, changeEndian : Boolean = false) : String = {
    val hex = java.lang.Long.toHexString(java.lang.Double.doubleToLongBits(double)).toUpperCase
    if (changeEndian) switchEndian(hex) else hex
  }
  
  def doubleListToHex(list: List[Double], changeEndian : Boolean = false) : String = {
    listProcessGroups[Double](list, d => doubleToHex(d, changeEndian))
  }

  def listProcessGroups[T](source: List[T], process : T => String) : String = {
    
    @tailrec
    def processWithAccumulator(source: List[T], accum: String) : String = {
      if (source.isEmpty) {
        accum
      } else {
        processWithAccumulator(source.tail, accum + process(source.head))
      }
    }
    
    processWithAccumulator(source, "")
  }
  
  def stringProcessGroups[T](source: String, size: Int, process : String => T) : List[T] = {
    
    @tailrec
    def processWithAccumulator(source: String, accum: List[T]) : List[T] = {
      if (source.isEmpty) {
        accum
      } else {
        processWithAccumulator(source.dropRight(size), process(source.takeRight(size)) :: accum)
      }
    }
    
    processWithAccumulator(source, List[T]())
  }
  
  def switchEndian(hex: String) : String = {
    assert(List[Int](4,8,16,32) contains hex.length)

    @tailrec
    def switchWithAccumulator(accum: String, orig: String) : String = {
      if (orig.isEmpty) {
        accum
      } else {
        switchWithAccumulator(orig.take(2) + accum, orig.drop(2))
      }
    }

    switchWithAccumulator("", hex)
  }
}