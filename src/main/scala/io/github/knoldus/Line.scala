package io.github.knoldus

class Line(l: String) {
  private val line = l.trim
  val length = line.length
  val isComment = line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")
  val isBracket = line == "{" || line == "}"
  val isBlank = line.length == 0
  val isCode = !isComment && !isBlank
}