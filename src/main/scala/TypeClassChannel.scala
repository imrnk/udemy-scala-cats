import ByteEncoder.IntEncoder

import java.io.FileOutputStream
import scala.util.Using
object TypeClassChannel extends App {

  object FileChannel extends Channel {
    override def write[A](obj: A)(implicit enc: ByteEncoder[A]): Unit = {
      val bytes: Array[Byte] = enc.encode(obj)

      Using(new FileOutputStream("udemy-scala-cats/test")) { os =>
        os.write(bytes)
        os.flush()
      }
    }

    override def read[A]()(implicit dec: ByteDecoder[A]) = ???
  }

  implicit object Not3StringEncoder extends ByteEncoder[String] {
    override def encode(a: String): Array[Byte] = a.getBytes.map(b =>(b + 3).toByte)
  }

  object BooleanEncoder extends ByteEncoder[Boolean] {
    override def encode(a: Boolean) =
      if (a) IntEncoder.encode(1) else IntEncoder.encode(0)
  }

  case class Switch(isOn: Boolean)

  //Good place to put the implicit instances of type classes is inside
  // the companion object of the type
  object Switch {
    implicit object SwitchByteEncoder extends ByteEncoder[Switch] {
      override def encode(a: Switch) = BooleanEncoder.encode(a.isOn)
    }
  }

  //import ByteEncoder._
  FileChannel.write[Int](5)
  FileChannel.write[String]("one")
  FileChannel.write[Switch](Switch(false))
  //Common Situation
  //For each of type A:
  // - 1 main instance
  // - couple more instances for specific instances

  /*
  Goal:
    - Use the main instance by default
    - Provide the other instances for specific instances
   */

}
