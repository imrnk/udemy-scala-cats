package exercise.laws.discipline

import exercise.Eq
import exercise.laws.EqLaws
import org.scalacheck.Arbitrary
import org.scalacheck.Prop.forAll
import org.typelevel.discipline.Laws

trait EqTests[A] extends Laws {
  def laws: EqLaws[A]

  // #14: Define a RuleSet containing the laws in EqLaws
  def eq(implicit arb:  Arbitrary[A]) : RuleSet = new DefaultRuleSet(
    name = "eq",
    parent = None,
    "reflexive" -> forAll(laws.reflexive _),
    "symmetric" -> forAll(laws.symmetric _),
    "transitive" -> forAll(laws.transitive _)
  )

  // #15: Define a companion object with an 'apply' method so that we can
  //           easily instantiate tests with e.g. EqTests[Int]
}

object EqTests{
  def apply[A](implicit e : Eq[A]): EqTests[A] = new EqTests[A] {
    override def laws = new EqLaws[A] {
      override def eq = e
    }
  }
}
