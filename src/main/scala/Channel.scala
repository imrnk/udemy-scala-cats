trait Channel {
  def write[A](obj: A)(implicit enc: ByteEncoder[A]): Unit
  def read[A]()(implicit dec : ByteDecoder[A]) : A
}



