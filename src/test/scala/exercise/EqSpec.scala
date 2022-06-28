package exercise

import exercise.laws.EqLaws
import exercise.laws.discipline.EqTests

class EqSpec extends MySpec {
  // TODO #16: Write tests for every Eq instance (Int, String and Person)
  //           using Discipline and the 'checkAll' method
  checkAll("Eq[Int]", EqTests[Int].eq)
  checkAll("Eq[String]", EqTests[String].eq)
  checkAll("Eq[Person]", EqTests[Person].eq)
}
