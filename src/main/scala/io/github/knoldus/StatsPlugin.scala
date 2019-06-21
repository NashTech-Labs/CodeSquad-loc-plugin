package io.github.knoldus

import java.io.{File, PrintWriter}

import sbt.Keys._
import sbt._

import scala.util.{Failure, Success, Try}

object StatsPlugin extends Plugin {

  lazy val statsAnalyzers = SettingKey[Seq[Analyzer]]("stats-analyzers")
  lazy val statsProject = TaskKey[Unit]("stats-project", "Prints code statistics for a single project, the current one")
  lazy val statsProjectNoPrint = TaskKey[Seq[AnalyzerResult]](
    "stats-project-no-print", "Returns code statistics for a project, without printing it (shouldn't be used directly)")
  lazy val statsEncoding = TaskKey[String]("stats-encoding")

  override lazy val settings = Seq(
    commands += statsCommand,
    statsAnalyzers := Seq(new FilesAnalyzer(), new LinesAnalyzer(), new CharsAnalyzer()),
    statsProject <<= (statsProjectNoPrint, name, state) map { (res, n, s) => statsProjectTask(res, n, s.log) },
    statsProjectNoPrint <<= (statsAnalyzers, sources in Compile, packageBin in Compile, state, statsEncoding, compile in Compile) map {
      (ana, src, packg, s, enc, c) => statsProjectNoPrintTask(ana, src, packg, enc, s.log)
    },
    statsEncoding <<= scalacOptions.map {
      _.sliding(2).foldLeft("UTF-8") {
        case (_, List("-encoding", enc)) => enc
        case (enc, _) => enc
      }
    },
    aggregate in statsProject := false,
    aggregate in statsProjectNoPrint := false
  )

  def statsCommand = Command.command("stats") { state => doCommand(state) }

  private def doCommand(state: State): State = {
    val log = state.log
    val extracted: Extracted = Project.extract(state)
    val structure = extracted.structure
    val projectRefs = structure.allProjectRefs

    val results: Seq[AnalyzerResult] = projectRefs.flatMap {
      projectRef =>
        EvaluateTask(structure, statsProjectNoPrint, state, projectRef) match {
          case Some((state, Value(seq))) =>
              Try {
                val moduleName = projectRef.project
                val file = new File(s"$moduleName/target/$moduleName.log")
                file.createNewFile()
                val printWriter = new PrintWriter(file)
                printWriter.print(seq.mkString(""))
                printWriter.flush()
                printWriter.close()
              } match {
                case Success(_) => log.info("File has been created successfully.")
                case Failure(exception) => log.info(s"File creation has been failed reason: $exception")
              }
            seq
          case _ => Seq()
        }
    }

    val distinctTitles = results.map(_.title).distinct
    val summedResults = distinctTitles.map(t => results.filter(r => r.title == t).reduceLeft(_ + _))


    log.info("")
    log.info("Code Statistics for project:")
    log.info("")

    summedResults.foreach(res => {
      log.info(res.toString)
      log.info("")
    })

    // return unchanged state
    state
  }

  private def statsProjectTask(results: Seq[AnalyzerResult], name: String, log: Logger) {
    log.info("")
    log.info("Code Statistics for project '" + name + "':")
    log.info("")

    results.foreach(res => {
      log.info(res.toString)
      log.info("")
    })
  }

  private def statsProjectNoPrintTask(analyzers: Seq[Analyzer], sources: Seq[File], packageBin: File, encoding: String, log: Logger) = {
    for (a <- analyzers) yield a.analyze(sources, packageBin, encoding)
  }
}
