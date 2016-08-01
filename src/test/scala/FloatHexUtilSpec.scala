import org.scalatest._
import com.murraywilliams.FloatHexUtil._

class FloatHexUtilSpec extends FlatSpec with Matchers {
  
  "HexReverse" should " reverse a hex string" in {
    switchEndian("D80F4940") shouldEqual "40490FD8"
  }
  
  "pi" should " decode properly" in {
    doubleToHex(Math.PI) shouldEqual "400921FB54442D18"
    floatToHex(Math.PI.asInstanceOf[Float]) shouldEqual "40490FDB"
    doubleToHex(Math.E) shouldEqual "4005BF0A8B145769"
    floatToHex(Math.E.asInstanceOf[Float]) shouldEqual "402DF854"
  }
  
  "lists" should " work as expected" in {
    val l : List[Float] = List(Math.PI, Math.E, 2.0f * Math.PI).map(_.asInstanceOf[Float])
    floatListToHex(l) shouldEqual "40490FDB402DF85440C90FDB"
    toFloatList("40490FDB402DF85440C90FDB") shouldEqual l
    floatListToHex(l, changeEndian=true) shouldEqual "DB0F494054F82D40DB0FC940"
  }
}