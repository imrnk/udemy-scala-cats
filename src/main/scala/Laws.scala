import ByteEncoder.IntEncoder
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.Configuration
import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

trait ByteCodecLaws[A] {
  def codec: ByteCodec[A]

  //law methods
  def isomorphism(a: A): Boolean = codec.decode(codec.encode(a)) == Some(a)
}

trait ByteCodecTests[A] extends Laws{
  def laws : ByteCodecLaws[A]

  def byteCodec(implicit arb: Arbitrary[A]) : RuleSet = new DefaultRuleSet(
    name = "byteCodec",
    parent = None,
    "isomorphism" -> forAll(laws.isomorphism _)
  )
}

object ByteCodecTests {
  def apply[A](implicit bc: ByteCodec[A]) : ByteCodecTests[A] = new ByteCodecTests[A] {
    override def laws = new ByteCodecLaws[A] {
      override def codec = bc
    }
  }
}

 object IntByteCodeLaws extends ByteCodecLaws[Int]{
  override def codec = new ByteCodec[Int]{
    override def encode(a: Int) = ByteEncoder[Int].encode(a)
    override def decode(bytes: Array[Byte]) = ByteDecoder[Int].decode(bytes)
  }
}

 object StringByteCodeLaws extends ByteCodecLaws[String]{
  override def codec = new ByteCodec[String]{
    override def encode(s: String) = ByteEncoder[String].encode(s)
    override def decode(bytes: Array[Byte]) = ByteDecoder[String].decode(bytes)
  }
}

  object IntByteCodecTests extends ByteCodecTests[Int] {
    override def laws = IntByteCodeLaws
  }

  object StringByteCodecTests extends ByteCodecTests[String] {
    override def laws = StringByteCodeLaws
  }

class ByteCodecSpec extends AnyFunSuite with Configuration with FunSuiteDiscipline {
  checkAll("ByteCodec[Int]", IntByteCodecTests.byteCodec)
  checkAll("ByteCodec[String]", StringByteCodecTests.byteCodec)
}