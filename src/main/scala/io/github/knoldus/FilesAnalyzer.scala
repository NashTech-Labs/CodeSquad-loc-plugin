package io.github.knoldus

import java.io.File
import scala.io.Source

class FilesAnalyzer extends Analyzer {
  def analyze(sources: Seq[File], packageBin: File, encoding: String) = {
    val scalaFiles = sources.filter(_.getName.endsWith("scala")).length
    val javaFiles = sources.filter(_.getName.endsWith("java")).length
    val totalSize = sources.map(_.length).foldLeft(0l)(_ + _)
    val avgSize = if (sources.length == 0) 0 else totalSize / sources.length
    val avgLines =
      if (sources.length == 0) 0
      else sources.map(s => Source.fromFile(s, encoding).getLines.length).foldLeft(0)(_ + _) / sources.length

    new FilesAnalyzerResult(sources.length, scalaFiles, javaFiles, totalSize, avgSize, avgLines)
  }
}

class FilesAnalyzerResult(
    totalFiles: Int,
    scalaFiles: Int,
    javaFiles: Int,
    totalSize: Long,
    avgSize: Long,
    avgLines: Int)
  extends AnalyzerResult {

  val title = "Files"
  val metrics =
    Seq(
      new AnalyzerMetric("Total:     ", totalFiles, "files"),
      new AnalyzerMetric("Scala:     ", scalaFiles, totalFiles, "files"),
      new AnalyzerMetric("Java:      ", javaFiles, totalFiles, "files"),
      new AnalyzerMetric("Total size:", totalSize.toDouble, "Bytes"),
      new AnalyzerMetric("Avg size:  ", avgSize.toDouble, "Bytes"),
      new AnalyzerMetric("Avg length:", avgLines, "lines"))
}
