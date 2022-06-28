import java.io.FileOutputStream
import java.nio.ByteBuffer
import scala.util.Using

trait Channel {
  def write(obj: Any) : Unit
}

object FileChannel extends Channel {

  override def write(obj: Any): Unit = {
    val bytes: Array[Byte] = obj match {
    case i: Int =>
      val bb = ByteBuffer.allocate(4)
      bb.putInt(i)
      bb.array()
    case s: String =>
      s.getBytes
    case _ => throw new RuntimeException("Unhandled")
    }

    Using(new FileOutputStream("udemy-scala-cats/test")) { os =>
      os.write(bytes)
      os.flush()
    }
  }
}

FileChannel.write("A test content")