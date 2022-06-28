import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import scala.util.Try

trait ByteCodec[A] extends ByteEncoder[A] with ByteDecoder[A]

trait ByteEncoder[A] {
  def encode(a: A): Array[Byte]
}

trait ByteDecoder[A] {
  def decode(bytes: Array[Byte]) : Option[A]
}

object ByteDecoder {
  def apply[A](implicit ev : ByteDecoder[A]): ByteDecoder[A] = ev

  def instance[A](f: Array[Byte] => Option[A]) : ByteDecoder[A] = new ByteDecoder[A] {
    override def decode(bytes: Array[Byte]) = f(bytes)
  }

  def fromByteToString(b: Array[Byte]): Option[String] = Try(new String(b, StandardCharsets.UTF_8)).toOption
  def fromByteToInt(b : Array[Byte]) : Option[Int] = {
    val bb = ByteBuffer.allocate(4)
    bb.put(b)
    bb.flip() //flip bytebuffer from write mode to read mode
    Some(bb.getInt)
  }
  //THe implicits in scope that passed when apply is called
  implicit val stringDecoder : ByteDecoder[String] = instance[String](fromByteToString)
  implicit val intDecoder : ByteDecoder[Int] = instance[Int](fromByteToInt)

  //For syntax

  /**
   * New ByteDecoderOps will be created every time syntax is used
   *
   * Alternative: Value class? val bytes and extends AnyVal
   */
  implicit class ByteDecoderOps[A](val bytes: Array[Byte]) extends AnyVal {
    def decode[A](implicit dec : ByteDecoder[A]): Option[A] = dec.decode(bytes)
  }
}

//define companion object of the type class
object ByteEncoder {
  //THe implicits in scope that passed when apply is called
  //move the implementation inside as implicit
  implicit object IntEncoder extends ByteEncoder[Int] {
    override def encode(a: Int): Array[Byte] = {
      val bb = ByteBuffer.allocate(4)
      bb.putInt(a)
      bb.array()
    }
  }

  implicit object StringEncoder extends ByteEncoder[String] {
    override def encode(a: String): Array[Byte] = a.getBytes
  }

  implicit def optionEncoder[A](implicit encA : ByteEncoder[A]): ByteEncoder[Option[A]] = new ByteEncoder[Option[A]] {
    override def encode(opt: Option[A]): Array[Byte] = opt match {
    case None => Array()
    case Some(a) => encA.encode(a)
    }
  }

  def summon[A](implicit ev : ByteEncoder[A]) : ByteEncoder[A] = ev
  def apply[A](implicit ev : ByteEncoder[A]) : ByteEncoder[A] = summon

  //For syntax
  implicit class ByteEncoderOps[A](val a: A) extends AnyVal {
    def encode(implicit enc : ByteEncoder[A]): Array[Byte] = enc.encode(a)
  }
}
