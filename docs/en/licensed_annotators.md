---
layout: docs
header: true
title: Spark NLP for Healthcare Annotators
permalink: /docs/en/licensed_annotators
key: docs-licensed-annotators
modify_date: "2020-08-10"
use_language_switcher: "Python-Scala"
---

<div class="h3-box" markdown="1">

> A Spark NLP for Healthcare subscription includes access to several pretrained annotators. 
Check out [www.johnsnowlabs.com](www.johnsnowlabs.com) for more information.

</div><div class="h3-box" markdown="1">

## AssertionLogReg 

This annotator classifies each clinically relevant named entity into its assertion: `present`, `absent`, `hypothetical`, `conditional`, `associated_with_other_person`, etc.

**Input types:** sentence, ner_chunk, embeddings

**Output type:** assertion

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.assertion.logreg.AssertionLogRegApproach">AssertionLogRegApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.assertion.logreg.AssertionLogRegModel">AssertionLogRegModel</a>

**Functions**:

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `AssertionLogReg` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `AssertionLogReg` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
logRegAssert = AssertionLogRegApproach()
    .setInputCols(["sentence", "ner_chunk", "embeddings"]) \
    .setOutputCol("pos") \
    .setLabelCol("label") \
    .setMaxIter(26) \
    .setReg(0.00192) \
    .setEnet(0.9) \
    .setBefore(10) \
    .setAfter(10) \
    .setStartCol("start") \
    .setEndCol("end")
```

```scala
val logRegAssert = new AssertionLogRegApproach()
    .setInputCols(Array("sentence", "ner_chunk", "embeddings"))
    .setOutputCol("pos")
    .setLabelCol("label")
    .setMaxIter(26)
    .setReg(0.00192)
    .setEnet(0.9)
    .setBefore(10)
    .setAfter(10)
    .setStartCol("start")
    .setEndCol("end")
```

</div></div><div class="h3-box" markdown="1">

## AssertionDL 

This annotator classifies each clinically relevant named entity into its assertion type: `present`, `absent`, `hypothetical`, `conditional`, `associated_with_other_person`, etc.

**Input types:** sentence, ner_chunk, embeddings

**Output type:** assertion

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.assertion.dl.AssertionDLApproach">AssertionDLApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.assertion.dl.AssertionDLModel">AssertionDLModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `AssertionDL` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `AssertionDL` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
dlAssert = AssertionDLApproach() \
    .setInputCols(["sentence", "ner_chunk", "embeddings"]) \
    .setOutputCol("pos") \
    .setGraphFolder("path/to/graphs") \
    .setConfigProtoBytes(b) \
    .setLabelCol("label") \
    .setBatchSize(64) \
    .setEpochs(5) \
    .setLearningRate(0.001) \
    .setDropout(0.05) \
    .setMaxSentLen(250) \
    .setStartCol("start") \
    .setEndCol("end")
```

```scala
val dlAssert = new AssertionDLApproach()
    .setInputCols(Array("sentence", "ner_chunk", "embeddings"))
    .setOutputCol("pos")
    .setGraphFolder("path/to/graphs")
    .setConfigProtoBytes(b)
    .setLabelCol("label")
    .setBatchSize(64)
    .setEpochs(5)
    .setLearningRate(0.001)
    .setDropout(0.05)
    .setMaxSentLen(250)
    .setStartCol("start")
    .setEndCol("end")
```

</div></div><div class="h3-box" markdown="1">

## Chunk2Token

Transforms a complete chunk Annotation into a token Annotation without further tokenization, as opposed to ChunkTokenizer.

**Input types:** "chunk",

**Output type:** "token"

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.Chunk2Token">Chunk2Token</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `Chunk2Token` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `Chunk2Token` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
chunk2Token = Chunk2Token() \
    .setInputCols(["chunk"]) \
    .setOutputCol("token")
```
```scala
val chunk2Token = new Chunk2Token()
    .setInputCols("chunk")
    .setOutputCol("token")
```

</div></div><div class="h3-box" markdown="1">

## ChunkEntityResolver

Assigns a standard code (ICD10 CM, PCS, ICDO; CPT) to chunk tokens identified from TextMatchers or the NER Models and embeddings pooled by ChunkEmbeddings

**Input types:** chunk_token, embeddings

**Output type:** resolution

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.resolution.ChunkEntityResolverApproach">ChunkEntityResolverApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.resolution.ChunkEntityResolverModel">ChunkEntityResolverModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `ChunkEntityResolver` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `ChunkEntityResolver` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
resolver = ChunkEntityResolverApproach() \
    .setInputCols(["chunk_token", "chunk_embeddings"]) \
    .setOutputCol("token") \
    .setLabelCol("label") \
    .setNormalizedCol("normalized") \
    .setNeighbours(500) \
    .setAlternatives(25) \
    .setThreshold(5) \
    .setExtramassPenalty(1) \
    .setEnableWmd(True) \
    .setEnableTfidf(True) \
    .setEnableJaccard(True) \
    .setEnableSorensenDice(False) \
    .setEnableJaroWinkler(False) \
    .setEnableLevenshtein(False) \
    .setDistanceWeights([1,2,2,0,0,0]) \
    .setPoolingStrategy("MAX") \
    .setMissAsEmpty(True)
```
```scala
val resolver = new ChunkEntityResolverApproach()
    .setInputCols(Array("chunk_token", "chunk_embeddings"))
    .setOutputCol("token")
    .setLabelCol("label")
    .setNormalizedCol("normalized")
    .setNeighbours(500)
    .setAlternatives(25)
    .setThreshold(5)
    .setExtramassPenalty(1)
    .setEnableWmd(true)
    .setEnableTfidf(true)
    .setEnableJaccard(true)
    .setEnableSorensenDice(false)
    .setEnableJaroWinkler(false)
    .setEnableLevenshtein(false)
    .setDistanceWeights(Array(1,2,2,0,0,0))
    .setPoolingStrategy("MAX")
    .setMissAsEmpty(true)
```
</div></div><div class="h3-box" markdown="1">

## SentenceEntityResolver

Assigns a standard code (ICD10 CM, PCS, ICDO; CPT) to sentence embeddings pooled over chunks from TextMatchers or the NER Models.  
This annotator is particularly handy when workING with BertSentenceEmbeddings from the upstream chunks.  

**Input types:** sentence_embeddings

**Output type:** resolution

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.resolution.SentenceEntityResolverApproach">SentenceEntityResolverApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.resolution.SentenceEntityResolverModel">SentenceEntityResolverModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `SentenceEntityResolver` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelCol()`:
    - `setNormalizedCol()`:
    - `setDistanceFunction()`: Setter for what distance function to use for KNN: `EUCLIDEAN` or `COSINE`
    - `setNeighbours()`: Setter for number of neighbours to consider in the KNN query to calculate WMD
    - `setThreshold()`: Setter for threshold value for the aggregated distance
    - `setMissAsEmpty(Boolean)`: Setter for whether or not to return an empty annotation on unmatched chunks

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `SentenceEntityResolver` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getLabelCol()`:
    - `getNormalizedCol()`:
    - `getDistanceFunction()`: Getter for what distance function to use for KNN: `EUCLIDEAN` or `COSINE`
    - `getNeighbours()`: Getter for number of neighbours to consider in the KNN query to calculate WMD
    - `getThreshold()`: Getter for threshold value for the aggregated distance
    - `getMissAsEmpty()`: Getter for whether or not to return an empty annotation on unmatched chunks

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
resolver = SentenceEntityResolverApproach() \
    .setInputCols(["sentence_embeddings"]) \
    .setOutputCol("prediction") \
    .setLabelCol("label") \
    .setNormalizedCol("normalized") \
    .setNeighbours(500) \
    .setThreshold(5) \
    .setMissAsEmpty(True)
```
```scala
val resolver = new SentenceEntityResolverApproach()
    .setInputCols(Array("chunk_token", "chunk_embeddings"))
    .setOutputCol("prediction")
    .setLabelCol("label")
    .setNormalizedCol("normalized")
    .setNeighbours(500)
    .setThreshold(5)
    .setMissAsEmpty(true)
```

</div></div><div class="h3-box" markdown="1">

## DocumentLogRegClassifier

A convenient TFIDF-LogReg classifier that accepts "token" input type and outputs "selector"; an input type mainly used in RecursivePipelineModels

**Input types:** token

**Output type:** category

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.classification.DocumentLogRegClassifierApproach">DocumentLogRegClassifierApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.classification.DocumentLogRegClassifierModel">DocumentLogRegClassifierModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `DocumentLogRegClassifier` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelCol()`:
    - `setMaxIter()`:
    - `setTol()`:
    - `setFitIntercept()`:

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `DocumentLogRegClassifier` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getLabelCol()`:
    - `getMaxIter()`:
    - `getTol()`:
    - `getFitIntercept()`:

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
logregClassifier = DocumentLogRegClassifierApproach() \
    .setInputCols("chunk_token") \
    .setOutputCol("category") \
    .setLabelCol("label_col") \
    .setMaxIter(10) \
    .setTol(1e-6) \
    .setFitIntercept(True)
```
```scala
val logregClassifier = new DocumentLogRegClassifierApproach()
    .setInputCols("chunk_token")
    .setOutputCol("category")
    .setLabelCol("label_col")
    .setMaxIter(10)
    .setTol(1e-6)
    .setFitIntercept(true)
```

</div></div><div class="h3-box" markdown="1">

## DeIdentificator

Identifies potential pieces of content with personal information about patients and remove them by replacing with semantic tags.

**Input types:** sentence, token, ner_chunk

**Output type:** sentence

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.deid.DeIdentification">DeIdentification</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.deid.DeIdentificationModel">DeIdentificationModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `DeIdentificator` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setRegexPatternsDictionary(path: ExternalResource)`: 
    - `setMode(m: String)`:
    - `setDateTag(s: String)`:
    - `setObfuscateDate(s: Boolean)`:
    - `setDays(k: Int)`:
    - `setDateToYear(s: Boolean)`:
    - `setMinYear(s: Int)`:
    - `setDateFormats(s: Array[String])`:
    - `setConsistentObfuscation(s: Boolean)`:
    - `setSameEntityThreshold(s: Double)`:

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `DeIdentificator` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getRegexPatternsDictionary()`:
    - `getMode()`:
    - `getDateTag()`:
    - `getObfuscateDate()`:
    - `getDays()`:
    - `getDateToYear()`:
    - `getMinYear()`:
    - `getDateFormats()`:
    - `getConsistentObfuscation()`:
    - `getSameEntityThreshold()`:

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
deid = DeIdentificationApproach() \
      .setInputCols("sentence", "token", "ner_chunk") \
      .setOutputCol("deid_sentence") \
      .setRegexPatternsDictionary("src/test/resources/de-identification/dic_regex_patterns_main_categories.txt") \
      .setMode("mask") \
      .setDateTag("DATE") \
      .setObfuscateDate(False) \
      .setDays(5) \
      .setDateToYear(False) \
      .setMinYear(1900) \
      .setDateFormats(["MM-dd-yyyy","MM-dd-yy"]) \
      .setConsistentObfuscation(True) \
      .setSameEntityThreshold(0.9)
```
```scala
val deid = new DeIdentificationApproach()
      .setInputCols("sentence", "token", "ner_chunk")
      .setOutputCol("deid_sentence")
      .setRegexPatternsDictionary("src/test/resources/de-identification/dic_regex_patterns_main_categories.txt") \
      .setMode("mask")
      .setDateTag("DATE")
      .setObfuscateDate(false)
      .setDays(5)
      .setDateToYear(false)
      .setMinYear(1900)
      .setDateFormats(Seq("MM-dd-yyyy","MM-dd-yy"))
      .setConsistentObfuscation(true)
      .setSameEntityThreshold(0.9)
```

</div></div><div class="h3-box" markdown="1">

## Contextual Parser

This annotator provides Regex + Contextual Matching, based on a JSON file.

**Output type:** sentence, token

**Input types:** chunk

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.context.ContextualParserApproach">ContextualParserApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.context.ContextualParserModel">ContextualParserModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `Contextual Parser` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setJsonPath(String)`: Path to json file with rules
    - `setCaseSensitive(Boolean)`: optional: Whether to use case sensitive when matching values, default is false
    - `setPrefixAndSuffixMatch(Boolean)`: optional: Whether to force both before AND after the regex match to annotate the hit
    - `setContextMatch(Boolean)`: optional: Whether to include prior and next context to annotate the hit
    - `setUpdateTokenizer(Boolean)`: optional: Whether to update tokenizer from pipeline when detecting multiple words on dictionary values
    - `setDictionary(Array[Dictionary])`: optional: Path to dictionary file in tsv or csv format

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `Contextual Parser` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getJsonPath()`: Getter for Path to json file with rules
    - `getCaseSensitive()`: Getter for whether to use case sensitive when matching values, default is false
    - `getPrefixAndSuffixMatch()`: Getter for whether to force both before AND after the regex match to annotate the hit
    - `getContextMatch()`: Getter for whether to include prior and next context to annotate the hit
    - `getUpdateTokenizer()`: Getter for whether to update tokenizer from pipeline when detecting multiple words on dictionary values
    - `getDictionary()`: Getter for path to dictionary file in tsv or csv format

**JSON format:**
```
{
  "entity": "Stage",
  "ruleScope": "sentence",
  "regex": "[cpyrau]?[T][0-9X?][a-z^cpyrau]*",
  "matchScope": "token"
}
```

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
contextual_parser = ContextualParserApproach() \
        .setInputCols(["sentence", "token"]) \
        .setOutputCol("entity_stage") \
        .setJsonPath("data/Stage.json")
```
```scala
val contextualParser = new ContextualParserApproach()
        .setInputCols(Array("sentence", "token"))
        .setOutputCol("entity_stage")
        .setJsonPath("data/Stage.json")
```

</div></div><div class="h3-box" markdown="1">

## RelationExtraction 

Extracts and classifier instances of relations between named entities.

**Input types:** pos, ner_chunk, embeddings, dependency

**Output type:** category

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.re.RelationExtractionApproach">RelationExtractionApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.re.RelationExtractionModel">RelationExtractionModel</a>

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `RelationExtraction` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelColumn(String)`: Column with label per each document
    - `setEpochsNumber(int)`: Maximum number of epochs to train
    - `setBatchSize(int)`: Setter for Batch Size
    - `setDropout(dropout: Float)`: Dropout coefficient
    - `setlearningRate(lr: Float)`: Learning Rate
    - `setModelFile(modelFile: String)`: Set the model file name
    - `setFixImbalance(fix: Boolean)`: Fix imbalance of training set
    - `setValidationSplit(validationSplit: Float)`: Choose the proportion of training dataset to be validated against the model on each Epoch. The value should be between **0.0** and **1.0** and by default it is **0.0** and off.
    - `setFromEntity(beginCol: String, endCol: String, labelCol: String)`: Set from entity
    - `setToEntity(beginCol: String, endCol: String, labelCol: String)`: Set to entity

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `RelationExtraction` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getLabelColumn()`: Getter for Column with label per each document
    - `getEpochsNumber()`: Getter for Maximum number of epochs to train
    - `getBatchSize()`: Getter for Batch Size
    - `getDropout()`: Getter for Dropout coefficient
    - `getlearningRate()`: Getter for Learning Rate
    - `getModelFile()`: Getter for the model file name
    - `getFixImbalance()`: Getter for Fix imbalance of training set
    - `getValidationSplit()`: Getter for proportion of training dataset to be validated against the model on each Epoch. The value should be between **0.0** and **1.0** and by default it is **0.0** and off.
    - `getFromEntity()`: Getter for from entity
    - `getToEntity()`: Getter for to entity

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
reApproach = sparknlp_jsl.annotator.RelationExtractionApproach()\
    .setInputCols(["embeddings", "pos_tags", "ner_chunks", "dependencies"])\
    .setOutputCol("relations")\
    .setLabelColumn("target_rel")\
    .setEpochsNumber(300)\
    .setBatchSize(200)\
    .setLearningRate(0.001)\
    .setModelFile("RE.in1200D.out20.pb")\
    .setFixImbalance(True)\
    .setValidationSplit(0.05)\
    .setFromEntity("from_begin", "from_end", "from_label")\
    .setToEntity("to_begin", "to_end", "to_label")
```

```scala
val reApproach = new RelationExtractionApproach()
  .setInputCols(Array("embeddings", "pos_tags", "ner_chunks", "dependencies"))
  .setOutputCol("relations")
  .setLabelColumn("target_rel")
  .setEpochsNumber(300)
  .setBatchSize(200)
  .setlearningRate(0.001f)
  .setModelFile("RE.in1200D.out20.pb")
  .setFixImbalance(true)
  .setValidationSplit(0.05f)
  .setFromEntity("from_begin", "from_end", "from_label")
  .setToEntity("to_begin", "to_end", "to_label")

```
</div>

## NerChunker

Similar to what we used to do in `POSChunker` with `POS tags`, now we can also extract phrases that fits into a known pattern using the NER tags. `NerChunker` would be quite handy to extract entity groups with neighboring tokens when there is no pretrained NER model to address certain issues. Lets say we want to extract clinical findings and body parts together as a single chunk even if there are some unwanted tokens between.

**Output Type:** Chunk  

**Input Types:** Document, Named_Entity  

**Reference:** [NerChunker](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.ner.NerChunker)  

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setRegexParsers(Array[String])`: A list of regex patterns to match chunks
    <!-- - `addRegexParser(String)`: adds a pattern to the current list of chunk patterns. -->
    - `setLazyAnnotator(Boolean)`: Use `NerChunker` as a lazy annotator or not.

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getRegexParsers()`: A list of regex patterns to match chunks
    - `getLazyAnnotator()`: Whether `NerChunker` as a lazy annotator or not.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
ner_model = NerDLModel.pretrained("ner_radiology", "en", "clinical/models")\
    .setInputCols("sentence","token","embeddings")\
    .setOutputCol("ner")

ner_chunker = NerChunker().\
    .setInputCols(["sentence","ner"])\
    .setOutputCol("ner_chunk")\
    .setRegexParsers(["<IMAGINGFINDINGS>*<BODYPART>"])
```

```scala
ner_model = NerDLModel.pretrained("ner_radiology", "en", "clinical/models")
    .setInputCols("sentence","token","embeddings")
    .setOutputCol("ner")

ner_chunker = NerChunker().
    .setInputCols(["sentence","ner"])
    .setOutputCol("ner_chunk")
    .setRegexParsers(["<IMAGINGFINDINGS>*<BODYPART>"])
```

</div></div><div class="h3-box" markdown="1">

Refer to the NerChunker Scala docs for more details on the API.

## ChunkFilterer

ChunkFilterer will allow you to filter out named entities by some conditions or predefined look-up lists, so that you can feed these entities to other annotators like `Assertion Status` or `Entity Resolvers`. It can be used with two criteria: `isin` and `regex`.

**Output Type:** Chunk  

**Input Types:** Document, Chunk  

**Reference:** [ChunkFilterer](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.chunker.ChunkFilterer)

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setCriteria(String)`:
    - `setWhiteList(Array[String])`: If defined, list of entities to process.
    - `setLazyAnnotator(Boolean)`: Use `ChunkFilterer` as a lazy annotator or not.

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`: Whether `ChunkFilterer` used as a lazy annotator or not.
    - `getWhiteList()`: If defined, list of entities to process.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
chunk_filterer = ChunkFilterer()\
      .setInputCols("sentence","ner_chunk")\
      .setOutputCol("chunk_filtered")\
      .setCriteria("isin") \ 
      .setWhiteList(['severe fever','sore throat'])
```

```scala
chunk_filterer = ChunkFilterer()
      .setInputCols("sentence","ner_chunk")
      .setOutputCol("chunk_filtered")
      .setCriteria("isin")
      .setWhiteList(["severe fever","sore throat"])
```

</div></div><div class="h3-box" markdown="1">

Refer to the ChunkFilterer Scala docs for more details on the API.

## AssertionFilterer

`AssertionFilterer` will allow you to filter out the named entities by the list of acceptable assertion statuses. This annotator would be quite handy if you want to set a white list for the acceptable assertion statuses like present or conditional; and do not want absent conditions get out of your pipeline.

**Output Type:** Assertion  

**Input Types:** Document, Chunk, Embeddings

**Reference:** [AssertionFilterer](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.chunker.AssertionFilterer)

**Functions:**

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setWhiteList(Array[String])`: If defined, list of entities to process.
    - `setLazyAnnotator(Boolean)`: Use `AssertionFilterer` as a lazy annotator or not.
    - `setRegex(Array[String])`: If defined, list of entities to process. The rest will be ignored.

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`: Whether `AssertionFilterer` used as a lazy annotator or not.
    - `getWhiteList()`: If defined, list of entities to process.
    - `getRegex()`: If defined, list of entities to process. The rest will be ignored.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
clinical_assertion = AssertionDLModel.pretrained("assertion_dl", "en", "clinical/models") \
  .setInputCols(["sentence", "ner_chunk", "embeddings"]) \
  .setOutputCol("assertion")

assertion_filterer = AssertionFilterer()\
  .setInputCols("sentence","ner_chunk","assertion")\
  .setOutputCol("assertion_filtered")\
  .setWhiteList(["present"])
```

```scala
clinical_assertion = AssertionDLModel.pretrained("assertion_dl", "en", "clinical/models") \
  .setInputCols(["sentence", "ner_chunk", "embeddings"]) \
  .setOutputCol("assertion")

assertion_filterer = AssertionFilterer()\
  .setInputCols("sentence","ner_chunk","assertion")\
  .setOutputCol("assertion_filtered")\
  .setWhiteList(["present"])
```

</div></div><div class="h3-box" markdown="1">

Refer to the AssertionFilterer Scala docs for more details on the API.