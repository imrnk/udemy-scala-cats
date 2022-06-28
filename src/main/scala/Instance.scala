object Instance extends App{

  trait ByteEncoder[A] {
    def encode(a: A): Array[Byte]
  }

  object ByteEncoder {

    implicit val stringEncoder : ByteEncoder[String] = new ByteEncoder[String] {
      override def encode(a: String): Array[Byte] = {
        val res = a.getBytes
        println(res.toList.toString())
        res
      }
    }
    def instance[A](f : A => Array[Byte]): ByteEncoder[A] = new ByteEncoder[A] {
      override def encode(a: A) = f(a)
    }

    implicit val stringEncoder2 : ByteEncoder[String] = instance[String](_.getBytes)

    def summon[A](implicit ev : ByteEncoder[A]) : ByteEncoder[A] = ev
    def apply[A](implicit ev : ByteEncoder[A]) : ByteEncoder[A] = summon
  }

}
