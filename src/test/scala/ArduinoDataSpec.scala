import org.scalatest._
import com.murraywilliams.arduino.data.ArduinoData
import com.digi.xbee.api.utils.HexUtils
import com.murraywilliams.arduino.data.ArduinoMCP9808
import com.murraywilliams.arduino.data.ArduinoConversion

class ArduinoDataSpec extends FlatSpec with Matchers {
  
  "Hello" should "have tests" in {
    true should === (true)
  }
  
  "ArduinoData " should "parse raw data" in {
    val data = ArduinoData.parseByteArray(HexUtils.hexStringToByteArray("54656D70733A50C250C251C252C253C253C253C254C255C257C2"))
    data shouldBe an [ArduinoMCP9808]
    val values = data.asInstanceOf[ArduinoMCP9808].getValues
    values(0) shouldBe 37.0
  }
  
  "Arduino Pi " should "be sliced right" in {
    val data = HexUtils.hexStringToByteArray("D80F4940")
    val pi : Float = ArduinoConversion.readFloat(data)

    pi shouldEqual 3.141f +- 0.1f
  }
}
