package com.johnsnowlabs.nlp.annotators.spell.norvig

import com.johnsnowlabs.nlp.{AnnotatorBuilder, ContentProvider, DataBuilder}
import org.scalatest._

class NorvigSweetingTestSpec extends FlatSpec with NorvigSweetingBehaviors{

  "A norvig sweeting approach" should behave like testDefaultTokenCorpusParameter

 "an isolated spell checker" should behave like isolatedNorvigChecker(
    Seq(
      ("mral", "meal"),
      ("delicatly", "delicately"),
      ("efusive", "effusive"),
      ("lauging", "laughing"),
      //("gr8", "great"),
      ("juuuuuuuuuuuuuuuussssssssssttttttttttt", "just"),
      ("screeeeeeeewed", "screwed"),
      ("readampwritepeaceee", "readampwritepeaceee")
    )
  )

  "a spark spell checker" should behave like sparkBasedSpellChecker(DataBuilder.basicDataBuild("efusive", "destroyd"))

  "a spark spell checker using dataframes" should behave like sparkBasedSpellChecker(DataBuilder.basicDataBuild("efusive", "destroyd"), "TXTDS")

  "a good sized dataframe" should behave like sparkBasedSpellChecker(
    AnnotatorBuilder.withDocumentAssembler(ContentProvider.parquetData.limit(5000))
  )

  "a good sized dataframe trained with dataframe" should behave like datasetBasedSpellChecker

  "a norvig spell checker trained from fit" should behave like trainFromFitSpellChecker(Seq(
    "mral",
    "delicatly",
    "efusive",
    "lauging",
    "juuuuuuuuuuuuuuuussssssssssttttttttttt",
    "screeeeeeeewed",
    "readampwritepeaceee"
  ))
  
}