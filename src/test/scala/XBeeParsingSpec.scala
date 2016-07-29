import org.scalatest._
import com.digi.xbee.api.utils.HexUtils
import com.murraywilliams.XBeeListener

class XBeeParsingSpec extends FlatSpec with Matchers {
  
  "reader" should "parse float data" in {
    val data = HexUtils.hexStringToByteArray("424d503038352d54656d70463a716d7944716d7944cd6c7944")
    val listener = new XBeeListener
    val colonIdx = data.indexOf(':')
    val payload = data.slice(colonIdx+1,data.length)
    println(listener.toHexString(payload))
    val header = new String(data.slice(0,colonIdx))
    val json = listener.parseReadings(header,payload)
  }
  
}