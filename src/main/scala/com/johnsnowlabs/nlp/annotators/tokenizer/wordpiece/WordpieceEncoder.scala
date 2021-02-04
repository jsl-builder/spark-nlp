package com.johnsnowlabs.nlp.annotators.tokenizer.wordpiece

import com.johnsnowlabs.nlp.annotators.common.{IndexedToken, TokenPiece}
import scala.collection.mutable.ArrayBuffer


private[nlp] class WordpieceEncoder(vocabulary: Map[String, Int],
                                    unkToken: String = "[UNK]",
                                    maxInputCharsPerWord: Int = 200,
                                    partPrefix: String = "##") {

  require(vocabulary.contains(unkToken), "token " + unkToken + " not found in vocabulary")

  def encode(indexedToken: IndexedToken): Array[TokenPiece] = {
    val unkId = vocabulary(unkToken)

    if (indexedToken.token.length > maxInputCharsPerWord)
      return Array(TokenPiece(unkToken, indexedToken.token, unkId, true, indexedToken.begin, indexedToken.end))

    val result = ArrayBuffer[TokenPiece]()

    val text = indexedToken.token
    var start = 0
    var end = text.length

    // Greedy search for next largest substring
    while (end > start && start < text.length) {
      val toFind = (if (start > 0) partPrefix else "") + text.substring(start, end)

      val found = vocabulary.get(toFind)
      if (found.nonEmpty) {
        val subToken = TokenPiece(toFind, indexedToken.token, found.get, start == 0,
          indexedToken.begin + start, indexedToken.begin + end - 1)
        result.append(subToken)
        start = end
        end = text.length
      } else {
        end = end - 1

        if (end == start) {
          // Not Found anything in vocabulary
          return Array(TokenPiece(unkToken, indexedToken.token, unkId, true, indexedToken.begin, indexedToken.end))
        }
      }
    }

    result.toArray
  }
}
