package exercise

trait Eq[A] {
  //  #1: Define an 'eq' method that takes two A values as parameters, and returns a Boolean
  def eq(left: A, right: A) : Boolean
}

object Eq {
  //  #2: Define the method 'apply' so we can summon instances from implicit scope
    def apply[A](implicit ev: Eq[A]) : Eq[A] = ev
  //  #3: Define the method 'instance' so we can build instances of the Eq typeclass more easily.
  //          It should take as the only parameter a function of type (A, A) => Boolean
    def instance[A](f: (A,A) => Boolean) = new Eq[A] {
      override def eq(left: A, right: A) = f(left, right)
  }
  //  #4: Define an Eq instance for String
  implicit val stringEq : Eq[String] = instance[String](_ == _)
  //  #5: Define an Eq instance for Int
  implicit val intEq : Eq[Int] = instance[Int](_ == _)
  // #6: Define an Eq instance for Person. Two persons are equal if both their names and ids are equal.
  //          Extra points: receive implicit instances for String and Int and use them
  implicit val personEq : Eq[Person] = instance[Person]{
    (p1, p2) =>
      Eq[String].eq(p1.name, p2.name) && Eq[Int].eq(p1.id, p2.id)
  }
  // #7: Provide a way to automatically derive instances for Eq[Option[A]] given that we have an implicit
  //          instance for Eq[A]
  implicit def optionEq[A](implicit eqA: Eq[A]) : Eq[Option[A]] = new Eq[Option[A]]{
    override def eq(left: Option[A], right: Option[A]) : Boolean = (left, right) match {
      case (Some(l), Some(r)) => eqA.eq(l, r)
      case (Some(l), None) => false
      case (None, Some(r)) => false
      case (None, None) => false
    }
  }
  object Syntax {
    // TODO #8: Define a class 'EqOps' with a method 'eqTo' that enables the following syntax:
    //          "hello".eqTo("world")
    implicit class EqOps[A](val left : A) extends AnyVal {
      def eq(right: A)(implicit ev : Eq[A]) : Boolean = ev.eq(left, right)
    }
  }
}
