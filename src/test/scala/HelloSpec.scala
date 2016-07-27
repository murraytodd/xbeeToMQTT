import org.scalatest._
import com.murraywilliams.arduino.data.ArduinoData
import com.digi.xbee.api.utils.HexUtils
import com.murraywilliams.arduino.data.ArduinoMCP9808

class HelloSpec extends FlatSpec with Matchers {
  
  "Hello" should "have tests" in {
    true should === (true)
  }
  
  "ArduinoData " should "parse raw data" in {
    val data = ArduinoData.parseByteArray(HexUtils.hexStringToByteArray("54656D70733A50C250C251C252C253C253C253C254C255C257C2"))
    data shouldBe an [ArduinoMCP9808]
    val values = data.asInstanceOf[ArduinoMCP9808].getValues
    values(0) shouldBe 37.0
  }
}
