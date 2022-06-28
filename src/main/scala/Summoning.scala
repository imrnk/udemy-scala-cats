import ByteDecoder.ByteDecoderOps
import ByteEncoder.ByteEncoderOps

object Summoning extends App{

  implicit object Rot3StringEncoder extends ByteEncoder[String] {
    override def encode(a: String): Array[Byte] = {
      val res = a.getBytes.map(b => (b + 3).toByte)
      println(res.toList.toString())
      res
    }
  }

  //implicitly summons the closes implicit object available for use
  implicitly[ByteEncoder[String]].encode("Hello")

  //Using the summon method which does the same thing as implicitly
  ByteEncoder.summon[String].encode("Hello")
  //Using the apply method
  ByteEncoder[String].encode("Hello")

  //ByteDecoder[String].decode(Array(98, 105, 101, 110, 32, 58, 41)).foreach(println)

  ByteEncoder[Option[String]].encode(Some("Hello"))
  ByteEncoder[Option[String]].encode(None)

  ByteEncoder[Option[Int]].encode(Some(100))
  ByteEncoder[Option[Int]].encode(None)

  100.encode
  Array[Byte](98, 105, 101, 110, 32, 58, 41).decode[String]
}
