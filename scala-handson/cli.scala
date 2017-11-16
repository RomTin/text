object Main {
  def sum(list: List[Double]): Double =
    list match {
      case Nil => 0
      case head :: tail => head + sum(tail)
    }

  def length(list: List[Double]): Double =
    list match {
      case Nil => 0
      case _ :: tail => 1 + length(tail)
    }

  def mean(list: List[Double]): Double = sum(list) / length(list)

  def main(args: Array[String]): Unit = {
    val list = args.map(_.toDouble).toList
    println(s"mean: ${mean(list)}")
  }
}
