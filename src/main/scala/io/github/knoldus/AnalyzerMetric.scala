package io.github.knoldus

case class AnalyzerMetric(title: String, value: Double, total: Option[Double] = None, unit: Option[String] = None) {
  def this(title: String, value: Double, total: Double, unit: String) = this(title, value, Some(total), Some(unit))
  def this(title: String, value: Double, total: Double) = this(title, value, Some(total))
  def this(title: String, value: Double, unit: String) = this(title, value, None, Some(unit))

  def percentage: Option[Double] = total match {
    case Some(t) if t != 0 => Some(value/t * 100)
    case Some(t) => Some(100)
    case _ => None
  }

  override def toString = (percentage, unit) match {
    case (Some(p), Some(unit)) => "%s %.0f %s (%.1f%%)".format(title, value, unit, p)
    case (Some(p), None)       => "%s %.0f (%.1f%%)".format(title, value, p)
    case (None, Some(unit))    => "%s %.0f %s".format(title, value, unit)
    case _                     => "%s %.0f".format(title, value)
  }

  def +(that: AnalyzerMetric): AnalyzerMetric = (this.total, that.total) match {
    case (Some(t1), Some(t2)) => new AnalyzerMetric(title, this.value + that.value, Some(t1 + t2), unit)
    case _ => new AnalyzerMetric(title, this.value + that.value, None, unit)
  }
}