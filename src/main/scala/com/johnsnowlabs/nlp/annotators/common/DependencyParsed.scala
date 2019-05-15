package com.johnsnowlabs.nlp.annotators.common

import com.johnsnowlabs.nlp.{Annotation, AnnotatorType}


case class DependencyParsedSentence(tokens: Array[WordWithDependency])

case class WordWithDependency(word: String, begin: Int, end: Int, head: Int)

object DependencyParsed extends Annotated[DependencyParsedSentence]{

  private val ROOT_INDEX = -1

  override def annotatorType: String = AnnotatorType.DEPENDENCY

  override def unpack(annotations: Seq[Annotation]): Seq[DependencyParsedSentence] = {
    val sentences = TokenizedWithSentence.unpack(annotations)
    val depAnnotations = annotations
      .filter(a => a.annotatorType == annotatorType)
      .sortBy(a => a.begin)

    var last = 0
    sentences.map{sentence =>
      val sorted = sentence.indexedTokens.sortBy(t => t.begin)
      val dependencies = (last until (last + sorted.length)).map { i =>
        depAnnotations(i).metadata("head").toInt
      }

      last += sorted.length

      val words = sorted.zip(dependencies).map{
        case (token, dependency) =>
          WordWithDependency(token.token, token.begin, token.end, dependency)
      }

      DependencyParsedSentence(words)
    }
  }

  override def pack(items: Seq[DependencyParsedSentence]): Seq[Annotation] = {
    items.flatMap{sentence =>
      //val sizeSentence = sentence.tokens.length
      sentence.tokens.map { token =>
        val headWord = getHeadWord(token.head, sentence)
        val word = token.word
        val relatedWords = s"($headWord, $word)"
        val realHead = if (token.head == sentence.tokens.length) 0 else token.head + 1 //updateHeadsWithRootIndex(token.head, sizeSentence)
        Annotation(annotatorType, token.begin, token.end, relatedWords, Map("head" -> realHead.toString))
      }
    }
  }

  def getHeadWord(head: Int, sentence: DependencyParsedSentence): String = {
    var headWord = "ROOT"
    if (head != ROOT_INDEX) {
      headWord = sentence.tokens.lift(head).map(_.word)
        .getOrElse(sentence.tokens.find(_.head == sentence.tokens.length).get.word)
    }
    headWord
  }

//  def updateHeadsWithRootIndex(head: Int, sizeSentence: Int): Int = {
//    var newHead = ROOT_INDEX
//    if (head != sizeSentence) {
//      newHead = head + 1
//    }
//    newHead
//  }

}