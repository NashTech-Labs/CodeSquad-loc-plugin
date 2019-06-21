package io.github.knoldus

abstract class AnalyzerResult {
  def title: String
  def metrics: Seq[AnalyzerMetric]

  override def toString = title + "\n- " + metrics.mkString("\n- ") + "\n"
  private implicit def anyToString(x: Any) = x.toString

  def +(that: AnalyzerResult): AnalyzerResult = {
    val combinedMetrics = this.metrics ++ that.metrics
    val distinctTitles = combinedMetrics.map(_.title).distinct
    val summedMetrics = distinctTitles.map(t => combinedMetrics.filter(m => m.title == t).reduceLeft(_ + _))
    new GenericAnalyzerResult(title, summedMetrics)
  }
}

case class GenericAnalyzerResult(title: String, metrics: Seq[AnalyzerMetric]) extends AnalyzerResult