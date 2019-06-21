package io.github.knoldus

import java.io.File
import scala.io.Source

class LinesAnalyzer extends Analyzer {
  def analyze(sources: Seq[File], packageBin: File, encoding: String) = {
    val lines: Seq[Line] = for {
      file <- sources
      line <- Source.fromFile(file, encoding).getLines
    } yield new Line(line)

    val totalLines = lines.length
    val codeLines = lines.filter(_.isCode).length
    val commentLines = lines.filter(_.isComment).length
    val bracketLines = lines.filter(_.isBracket).length
    val blankLines = lines.filter(_.isBlank).length
    //val avgLength = lines.filter(_.isCode).map(_.length).foldLeft(0)(_ + _) / totalLines // check that totalLines is not 0

    new LinesAnalyzerResult(totalLines, codeLines, commentLines, bracketLines, blankLines)
  }
}

class LinesAnalyzerResult(
    totalLines: Int,
    codeLines: Int,
    commentLines: Int,
    bracketLines: Int,
    blankLines: Int)
  extends AnalyzerResult {

  val title = "Lines"
  val metrics =
    Seq(
      new AnalyzerMetric("Total:     ", totalLines, "lines"),
      new AnalyzerMetric("Code:      ", codeLines, totalLines, "lines"),
      new AnalyzerMetric("Comment:   ", commentLines, totalLines, "lines"),
      new AnalyzerMetric("Blank:     ", blankLines, totalLines, "lines"),
      new AnalyzerMetric("Bracket:   ", bracketLines, totalLines, "lines"))
      // new AnalyzerMetric("Avg length:", avgLength, "characters (code lines only, not inc spaces)"))
}
