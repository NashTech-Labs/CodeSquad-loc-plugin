package io.github.knoldus

import java.io.File

abstract class Analyzer {
  def analyze(sources: Seq[File], packageBin: File, encoding: String): AnalyzerResult
}
