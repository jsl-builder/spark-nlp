package com.johnsnowlabs.nlp.datasets

import java.io.File

import com.johnsnowlabs.ml.crf.{CrfDataset, DatasetMetadata, InstanceLabels, TextSentenceLabels}
import com.johnsnowlabs.nlp.AnnotatorType
import com.johnsnowlabs.nlp.annotators.common.TaggedSentence
import com.johnsnowlabs.nlp.annotators.ner.crf.{DictionaryFeatures, FeatureGenerator}
import com.johnsnowlabs.nlp.embeddings.{WordEmbeddingsRetriever, WordEmbeddingsFormat, WordEmbeddingsIndexer}
import com.johnsnowlabs.nlp.util.io.ExternalResource

/**
  * Helper class for to work with CoNLL 2003 dataset for NER task
  * Class is made for easy use from Java
  */
class CoNLL2003NerReader(wordEmbeddingsFile: String,
                         wordEmbeddingsNDims: Int,
                         normalize: Boolean,
                         embeddingsFormat: WordEmbeddingsFormat.Format,
                         possibleExternalDictionary: Option[ExternalResource]) {

  private val nerReader = CoNLL(3, AnnotatorType.NAMED_ENTITY)
  private val posReader = CoNLL(1, AnnotatorType.POS)

  private var wordEmbeddings: WordEmbeddingsRetriever = _

  if (wordEmbeddingsFile != null) {
    require(new File(wordEmbeddingsFile).exists())

    var fileDb = wordEmbeddingsFile + ".db"

    if (!new File(fileDb).exists()) {
      embeddingsFormat match {
        case WordEmbeddingsFormat.TEXT =>
          WordEmbeddingsIndexer.indexText(wordEmbeddingsFile, fileDb)
        case WordEmbeddingsFormat.BINARY =>
          WordEmbeddingsIndexer.indexBinary(wordEmbeddingsFile, fileDb)
        case WordEmbeddingsFormat.SPARKNLP =>
          fileDb = wordEmbeddingsFile
      }
    }

    if (new File(fileDb).exists()) {
      wordEmbeddings = WordEmbeddingsRetriever(fileDb, wordEmbeddingsNDims, normalize)
    }
  }

  private val fg = FeatureGenerator(
    DictionaryFeatures.read(possibleExternalDictionary),
    wordEmbeddings
  )

  private def readDataset(er: ExternalResource): Seq[(TextSentenceLabels, TaggedSentence)] = {
    val labels = nerReader.readDocs(er).flatMap(_._2)
      .map(sentence => TextSentenceLabels(sentence.tags))

    val posTaggedSentences = posReader.readDocs(er).flatMap(_._2)
    labels.zip(posTaggedSentences)
  }

  def readNerDataset(er: ExternalResource, metadata: Option[DatasetMetadata] = None): CrfDataset = {
    val lines = readDataset(er)
    if (metadata.isEmpty)
      fg.generateDataset(lines)
    else {
      val labeledInstances = lines.map { line =>
        val instance = fg.generate(line._2, metadata.get)
        val labels = InstanceLabels(line._1.labels.map(l => metadata.get.label2Id.getOrElse(l, -1)))
        (labels, instance)
      }
      CrfDataset(labeledInstances, metadata.get)
    }
  }
}