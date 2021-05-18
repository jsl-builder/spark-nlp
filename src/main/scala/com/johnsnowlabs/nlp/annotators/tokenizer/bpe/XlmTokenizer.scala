/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.johnsnowlabs.nlp.annotators.tokenizer.bpe

import com.johnsnowlabs.nlp.annotators.common.{IndexedToken, Sentence, TokenPiece}
import com.johnsnowlabs.nlp.annotators.tokenizer.moses.MosesTokenizer
import com.johnsnowlabs.nlp.annotators.tokenizer.normalizer.MosesPunctNormalizer

/**
  * XLM Tokenizer
  *
  * @param merges                     Combinations of byte pairs with ranking
  * @param vocab                      Mapping from byte pair to an id
  * @param lang                       Langauge of the text (Currenlty only english supported)
  * @param specialTokens              Special Tokens of the model to not split on
  * @param doLowercaseAndRemoveAccent True for current supported model (v1.2.0), False for XLM-17 & 100
  */
private[nlp] class XlmTokenizer(
                                 merges: Map[(String, String), Int],
                                 vocab: Map[String, Int],
                                 specialTokens: SpecialTokens,
                                 padWithSentenceTokens: Boolean = false,
                                 lang: String = "en",
                                 doLowercaseAndRemoveAccent: Boolean = true
                               ) extends BpeTokenizer(merges, vocab, specialTokens, padWithSentenceTokens) {
  require(lang == "en", "Only English is supported currently.")

  /**
    * Lowercase and strips accents from a piece of text based on
    * https://github.com/facebookresearch/XLM/blob/master/tools/lowercase_and_remove_accent.py
    */
  def lowercaseAndRemoveAccent(input: String): String = {
    var text = input
    text = text.toLowerCase()
    text = java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD)
    //    output = []
    //    for char in text:
    //      cat = unicodedata.category(char)
    //    if cat == "Mn":
    //      continue
    //    output.append(char)
    //    return "".join(output).lower().split(" ")
    text.toCharArray
      .filter(Character.getType(_) != Character.NON_SPACING_MARK) // Unicode Category "Mn"
      .mkString
      .toLowerCase
  }

  val mosesNormalizer = new MosesPunctNormalizer()
  val mosesTokenizer = new MosesTokenizer(lang)

  private def mosesPipeline(text: String): Array[String] = {
    var processed = text
    processed = mosesNormalizer.normalize(processed)
    processed = mosesNormalizer.removeNonPrintingChar(processed)
    mosesTokenizer.tokenize(processed)
  }

  override def tokenizeSubText(text: String, indexOffset: Int): Array[IndexedToken] = {
    val mosesTokenized = mosesPipeline(text)

    val processedText = if (doLowercaseAndRemoveAccent)
      lowercaseAndRemoveAccent(mosesTokenized.mkString(" "))
    else mosesTokenized.mkString(" ")

    val indexedTokens = processedText.split(" ").map((token: String) => {
      val tokenTextIndex = processedText.indexOf(token)
      IndexedToken(
        token,
        indexOffset + tokenTextIndex,
        indexOffset + tokenTextIndex + token.length - 1
      ) // TODO: What if special characters were removed?
    })
    indexedTokens
  }

  override def preProcessTokenForBpe(token: String): String = token + "</w>" // TODO
}
