package io.github.knoldus

import java.io.File
import scala.io.Source

class CharsAnalyzer extends Analyzer {
  def analyze(sources: Seq[File], packageBin: File, encoding: String) = {
    val lines: Seq[Line] = for {
      file <- sources
      line <- Source.fromFile(file, encoding).getLines
    } yield new Line(line)

    val totalChars = lines.map(_.length).foldLeft(0)(_ + _)
    val codeChars = lines.filter(_.isCode).map(_.length).foldLeft(0)(_ + _)
    val commentChars = lines.filter(_.isComment).map(_.length).foldLeft(0)(_ + _)

    new CharAnalyzerResult(totalChars, codeChars, commentChars)
  }
}

class CharAnalyzerResult(totalChars: Int, codeChars: Int, commentChars: Int) extends AnalyzerResult {

  val title = "Characters"
  val metrics =
    Seq(
      new AnalyzerMetric("Total:     ", totalChars, "chars"),
      new AnalyzerMetric("Code:      ", codeChars, totalChars, "chars"),
      new AnalyzerMetric("Comment:   ", commentChars, totalChars, "chars"))
}
