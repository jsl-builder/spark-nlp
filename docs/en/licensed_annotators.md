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

* ***Parameters***

    -  `afterParam: IntParam `: Amount of tokens from the context after the target
    -  `beforeParam: IntParam`: Amount of tokens from the context before the target
    -  `eNetParam: DoubleParam` : Elastic net parameter
    -  `startCol: Param[String]`: Column that contains the token number for the start of the target
    -  `endCol: Param[String]`: Column that contains the token number for the end of the target
    -  `label: Param[String] `: Column with one label per document
    -  `maxIter: IntParam` : Max number of iterations for algorithm
    -  `regParam: DoubleParam`: Regularization parameter
    -  `uid: String`: a unique identifier for the instanced AssertionDLApproach 
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `AssertionLogReg` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setMaxIter()`: Setter for max number of iterations for algorithm
    - `setReg()`: Setter for Regularization parameter
    - `setEnet()`: Setter for Elastic net parameter
    - `setBefore()`: Setter for amount of tokens from the context before the target
    - `setAfter()`: Setter for amount of tokens from the context after the target
    - `setStartCol()`: Setter for column that contains the token number for the start of the target
    - `setEndCol()`: Setter for column that contains the token number for the end of the target

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getMaxIter()`: Getter for max number of iterations for algorithm
    - `getReg()`: Getter for Regularization parameter
    - `getEnet()`: Getter for Elastic net parameter
    - `getBefore()`: Getter for amount of tokens from the context before the target
    - `getAfter()`: Getter for amount of tokens from the context after the target
    - `getStartCol()`: Getter for column that contains the token number for the start of the target
    - `getEndCol()`: Getter for column that contains the token number for the end of the target
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
    .setEndCol("end") \
    .setLazyAnnotator(False)
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
    .setLazyAnnotator(false)
```

</div></div><div class="h3-box" markdown="1">

## AssertionDL 

This annotator classifies each clinically relevant named entity into its assertion type: `present`, `absent`, `hypothetical`, `conditional`, `associated_with_other_person`, etc.

**Input types:** sentence, ner_chunk, embeddings

**Output type:** assertion

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.assertion.dl.AssertionDLApproach">AssertionDLApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.assertion.dl.AssertionDLModel">AssertionDLModel</a>

**Functions:**

* ***Parameters***
    -  `graphFolder: Param[String]`: path to Graph folder
    - `configProtoBytes(bytes: Array[Int])`: ConfigProto from tensorflow, serialized into byte array.
    -  `labelCol: Param[String]`: Column with label per each document
    -  `batchSize: IntParam` : Batch Size
    -  `epochs: IntParam`: maximum number of epochs to train
    -  `learningRate: FloatParam` : Learning Rate
    -  `dropout: FloatParam` : Dropout coefficient
    -  `maxSentLen: IntParam` : Maximum sentence length
    - ` startCol: Param[String]`: column that contains the token number for the start of the target
    -  `endCol: Param[String]`: column that contains the token number for the end of the target
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setGraphFolder(String)`: Sets path to Graph folder
    - `setConfigProtoBytes(Array[Int])`: ConfigProto from tensorflow, serialized into byte array.
    - `setLabelCol(String)`: Column with label per each document
    - `setBatchSize(int)`: Setter for Batch Size
    - `setEpochs(int)`: Sets maximum number of epochs to train
    - `setLearningRate(FLoat)`: Sets Learning Rate
    - `setDropout(float)`: Sets Dropout coefficient
    - `setMaxSentLen(int)`: Setter for Maximum sentence length
    - `setStartCol()`: Setter for column that contains the token number for the start of the target
    - `setEndCol()`: Setter for column that contains the token number for the end of the target
    - `setLazyAnnotator(Boolean)`: Use `AssertionDL` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getGraphFolder()`: Gets path to Graph folder
    - `getConfigProtoBytes()`: ConfigProto from tensorflow, serialized into byte array.
    - `getLabelCol()`: Column with label per each document
    - `getBatchSize()`: Getter for Batch Size
    - `getEpochs()`: Gets maximum number of epochs to train
    - `getLearningRate()`: Gets Learning Rate
    - `getDropout()`: Gets Dropout coefficient
    - `getMaxSentLen()`: Getter for Maximum sentence length
    - `getStartCol()`: Getter for column that contains the token number for the start of the target
    - `getEndCol()`: Getter for column that contains the token number for the end of the target
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
    .setEndCol("end") \
    .setLazyAnnotator(False)
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
    .setLazyAnnotator(false)
```

</div></div><div class="h3-box" markdown="1">

## Chunk2Token

Transforms a complete chunk Annotation into a token Annotation without further tokenization, as opposed to ChunkTokenizer.

**Input types:** "chunk",

**Output type:** "token"

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.Chunk2Token">Chunk2Token</a>

**Functions:**

* ***Parameters***
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

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
    .setOutputCol("token")\
    .setLazyAnnotator(False)
```
```scala
val chunk2Token = new Chunk2Token()
    .setInputCols("chunk")
    .setOutputCol("token")
    .setLazyAnnotator(false)
```

</div></div><div class="h3-box" markdown="1">

## ChunkEntityResolver

Assigns a standard code (ICD10 CM, PCS, ICDO; CPT) to chunk tokens identified from TextMatchers or the NER Models and embeddings pooled by ChunkEmbeddings

**Input types:** chunk_token, embeddings

**Output type:** resolution

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.resolution.ChunkEntityResolverApproach">ChunkEntityResolverApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.resolution.ChunkEntityResolverModel">ChunkEntityResolverModel</a>

**Functions:**

* ***Parameters***
    - ` labelCol: Param[String]`: Column with label per each document
    - ` normalizedCol: Param[String] `: column name for the original, normalized description
    - ` allDistancesMetadata: BooleanParam `: Whether or not to return an all distance values in the metadata. Default: `False`
    - ` alternatives: IntParam `: number of results to return in the metadata after sorting by last distance calculated
    - ` threshold: DoubleParam `: threshold value for the aggregated distance
    - ` distanceWeights: DoubleArrayParam `: distance weights to apply before pooling: [`WMD`, `TFIDF`, `Jaccard`, `SorensenDice`, `JaroWinkler`, `Levenshtein`]
    - ` extramassPenalty: DoubleParam `: penalty for extra words in the knowledge base match during WMD calculation
    - ` enableWmd: BooleanParam `: whether or not to use WMD token distance.
    - ` enableTfidf: BooleanParam `: whether or not to use TFIDF token distance.
    - ` enableJaccard: BooleanParam `: whether or not to use Jaccard token distance.
    - ` enableSorensenDice: BooleanParam `: whether or not to use Sorensen-Dice token distance.
    - ` enableJaroWinkler: BooleanParam `: whether or not to use Jaro-Winkler character distance.
    - ` enableLevenshtein: BooleanParam `: whether or not to use Levenshtein character distance.
    - ` poolingStrategy: Param[String] `: pooling strategy to aggregate distances: `AVERAGE` or `SUM`
    - ` missAsEmpty: BooleanParam `: Setter for whether or not to return an empty annotation on unmatched chunks
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `ChunkEntityResolver` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelCol(String)`: Column with label per each document
    - `setNormalizedCol(String)`: Sets column name for the original, normalized description
    - `setAllDistancesMetadata(Boolean)`: Setter for whether or not to return an all distance values in the metadata.
    - `setDistanceWeights(Array[Double])`: Sets  distance weights to apply before pooling: [`WMD`, `TFIDF`, `Jaccard`, `SorensenDice`, `JaroWinkler`, `Levenshtein`]
    - `setAlternatives(int)`: Sets number of results to return in the metadata after sorting by last distance calculated
    - `setThreshold(Double)`: Sets threshold value for the aggregated distance
    - `setExtramassPenalty(Double)`: Sets penalty for extra words in the knowledge base match during WMD calculation
    - `setEnableWmd(Boolean)`: Sets whether or not to use WMD token distance.
    - `setEnableTfidf(Boolean)`: Sets whether or not to use TFIDF token distance.
    - `setEnableJaccard(Boolean)`: Sets whether or not to use Jaccard token distance.
    - `setEnableSorensenDice(Boolean)`: Sets whether or not to use Sorensen-Dice token distance.
    - `setEnableJaroWinkler(Boolean)`: Sets whether or not to use Jaro-Winkler character distance.
    - `setEnableLevenshtein(Boolean)`: Sets whether or not to use Levenshtein character distance.
    - `setPoolingStrategy(String)`: Sets pooling strategy to aggregate distances: `AVERAGE` or `SUM`
    - `setMissAsEmpty(Boolean)`: Setter for whether or not to return an empty annotation on unmatched chunks

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `ChunkEntityResolver` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getLabelCol()`: Column with label per each document
    - `getNormalizedCol()`: Gets column name for the original, normalized description
    - `getAllDistancesMetadata()`: Getter for whether or not to return an all distance values in the metadata.
    - `getDistanceWeights()`: Gets distance weights to apply before pooling: [`WMD`, `TFIDF`, `Jaccard`, `SorensenDice`, `JaroWinkler`, `Levenshtein`]
    - `getAlternatives()`: Gets number of results to return in the metadata after sorting by last distance calculated
    - `getThreshold()`: Gets threshold value for the aggregated distance
    - `getExtramassPenalty()`: Gets penalty for extra words in the knowledge base match during WMD calculation
    - `getEnableWmd()`: Gets whether or not to use WMD token distance.
    - `getEnableTfidf()`: Gets whether or not to use TFIDF token distance.
    - `getEnableJaccard()`: Gets whether or not to use Jaccard token distance.
    - `getEnableSorensenDice()`: Gets whether or not to use Sorensen-Dice token distance.
    - `getEnableJaroWinkler()`: Gets whether or not to use Jaro-Winkler character distance.
    - `getEnableLevenshtein()`: Gets whether or not to use Levenshtein character distance.
    - `getPoolingStrategy()`: Gets pooling strategy to aggregate distances: `AVERAGE` or `SUM`
    - `getMissAsEmpty()`: Getter for whether or not to return an empty annotation on unmatched chunks

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

* ***Parameters***
    - ` labelCol: Param[String] `: column name for the value we are trying to resolve
    - ` normalizedCol: Param[String] `: column name for the original, normalized description
    - ` distanceFunction: Param[String] `: what distance function to use for KNN: `EUCLIDEAN` or `COSINE`
    - ` neighbours: IntParam `: number of neighbours to consider in the KNN query to calculate WMD
    - ` threshold: DoubleParam `: threshold value for the aggregated distance
    - ` auxLabelCol: Param[String] `: optional column with one extra label per document. This extra label will be outputted later on in an additional column
    - ` auxLabelMap: StructFeature[Map[String, String]] `: Optional column with one extra label per document.
    - ` caseSensitive: BooleanParam `: whether to follow case sensitiveness for matching exceptions in text
    - ` returnCosineDistances: BooleanParam `: whether cosine distances should be calculated between a chunk and the k_candidates result embeddings 
    - ` missAsEmpty: BooleanParam `: whether or not to return an empty annotation on unmatched chunks
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `SentenceEntityResolver` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelCol(String)`: Sets column name for the value we are trying to resolve
    - `setNormalizedCol(String)`: Sets column name for the original, normalized description
    - `setDistanceFunction(String)`: Setter for what distance function to use for KNN: `EUCLIDEAN` or `COSINE`
    - `setNeighbours(int)`: Setter for number of neighbours to consider in the KNN query to calculate WMD
    - `setThreshold(Double)`: Setter for threshold value for the aggregated distance
    - `setAuxLabelCol(String)`: Sets optional column with one extra label per document. This extra label will be outputted later on in an additional column
    - `setAuxLabelMap(Map[String, String])`: Sets Optional column with one extra label per document.
    - `setCaseSensitive(Boolean)`: Sets whether to follow case sensitiveness for matching exceptions in text
    - `setReturnCosineDistances(Boolean)`: Set whether cosine distances should be calculated between a chunk and the k_candidates result embeddings
    - `setMissAsEmpty(Boolean)`: Setter for whether or not to return an empty annotation on unmatched chunks

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `SentenceEntityResolver` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getLabelCol()`: Gets column name for the value we are trying to resolve
    - `getNormalizedCol()`: Gets column name for the original, normalized description
    - `getDistanceFunction()`: Getter for what distance function to use for KNN: `EUCLIDEAN` or `COSINE`
    - `getNeighbours()`: Getter for number of neighbours to consider in the KNN query to calculate WMD
    - `getThreshold()`: Getter for threshold value for the aggregated distance
    - `getAuxLabelCol()`: Gets optional column with one extra label per document. This extra label will be outputted later on in an additional column
    - `getAuxLabelMap()`: Gets Optional column with one extra label per document.
    - `getCaseSensitive()`: Gets whether to follow case sensitiveness for matching exceptions in text
    - `getReturnCosineDistances()`: Gets whether cosine distances should be calculated between a chunk and the k_candidates result embeddings 
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

* ***Parameters***
    - ` labelCol: Param[String] `: Column with label per each document
    - ` maxIter: Param[Int] `: max number of iterations for algorithm
    - ` tol: Param[Double] `: Tolerence value
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `DocumentLogRegClassifier` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelCol(String)`: Column with label per each document
    - `setMaxIter(int)`: Setter for max number of iterations for algorithm
    - `setTol(Double)`: Setter for Tolerence value
    <!-- - `setFitIntercept(Boolean)`: -->

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `DocumentLogRegClassifier` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getLabelCol()`: Column with label per each document
    - `getMaxIter()`: Getter for max number of iterations for algorithm
    - `getTol()`: Getter for Tolerence value
    <!-- - `getFitIntercept()`: -->

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

* ***Parameters***
    - ` regexPatternsDictionary: ExternalResourceParam `: dictionary with regular expression patterns that match some protected entity
    - ` mode: Param[String] `: Mode for Anonymizer: `mask` or `obfuscate`
    - ` dateTag: Param[String] `: Tag representing dates in the obfuscate reference file (default: `DATE`)
    - ` obfuscateDate: BooleanParam `: When `mode=="obfuscate"` whether to obfuscate dates or not.
    - ` days: IntParam `: Number of days to obfuscate the dates by displacement.
    - ` dateToYear: BooleanParam `: `true` if dates must be converted to years, `false` otherwise
    - ` minYear: IntParam `: Minimum year to use when converting date to year
    - ` dateFormats: StringArrayParam `: Format of dates to displace
    - ` consistentObfuscation: BooleanParam `: Whether to replace very similar entities in a document with the same randomized term (default: `true`)
    - ` sameEntityThreshold: DoubleParam `: Similarity threshold [**0.0-1.0**] to consider two appearances of an entity as the same (default: **0.9**)
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `DeIdentificator` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setRegexPatternsDictionary(ExternalResource)`: Setter for dictionary with regular expression patterns that match some protected entity
    - `setMode(String)`: Setter for Mode for Anonymizer: `mask` or `obfuscate`
    - `setDateTag(String)`: Setter for Tag representing dates in the obfuscate reference file (default: `DATE`)
    - `setObfuscateDate(Boolean)`: Setter for When `mode=="obfuscate"` whether to obfuscate dates or not.
    - `setDays(Int)`: Setter for Number of days to obfuscate the dates by displacement.
    - `setDateToYear(Boolean)`: `true` if dates must be converted to years, `false` otherwise
    - `setMinYear(Int)`: Setter for Minimum year to use when converting date to year
    - `setDateFormats(Array[String])`: Format of dates to displace
    - `setConsistentObfuscation(Boolean)`: Whether to replace very similar entities in a document with the same randomized term (default: `true`)
    - `setSameEntityThreshold(Double)`: Similarity threshold [0.0-1.0] to consider two appearances of an entity as the same (default: **0.9**)

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`:  Whether `DeIdentificator` used as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`.
    - `getRegexPatternsDictionary()`: Getter for dictionary with regular expression patterns that match some protected entity
    - `getMode()`: Getter for Mode for Anonymizer: `mask` or `obfuscate`
    - `getDateTag()`: Getter for Tag representing dates in the obfuscate reference file (default: `DATE`)
    - `getObfuscateDate()`: Getter for When `mode=="obfuscate"` whether to obfuscate dates or not.
    - `getDays()`: Getter for Number of days to obfuscate the dates by displacement.
    - `getDateToYear()`: `true` if dates must be converted to years, `false` otherwise
    - `getMinYear()`: Getter for Minimum year to use when converting date to year
    - `getDateFormats()`: Format of dates to displace
    - `getConsistentObfuscation()`: Whether to replace very similar entities in a document with the same randomized term (default: `true`)
    - `getSameEntityThreshold()`: Similarity threshold [0.0-1.0] to consider two appearances of an entity as the same (default: **0.9**)

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
    .setSameEntityThreshold(0.9)\
    .setLazyAnnotator(False)   
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
        .setLazyAnnotator(False)
```

</div></div><div class="h3-box" markdown="1">

## Contextual Parser

This annotator provides Regex + Contextual Matching, based on a JSON file.

**Output type:** sentence, token

**Input types:** chunk

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.context.ContextualParserApproach">ContextualParserApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.context.ContextualParserModel">ContextualParserModel</a>

**Functions:**

* ***Parameters***
    - ` jsonPath: Param[String] `: Path to json file with rules
    - ` caseSensitive: BooleanParam `: whether to use case sensitive when matching values, default is false
    - ` prefixAndSuffixMatch: BooleanParam `: whether to force both before AND after the regex match to annotate the hit
    - ` contextMatch: BooleanParam `: whether to include prior and next context to annotate the hit
    - ` updateTokenizer: BooleanParam `: whether to update tokenizer from pipeline when detecting multiple words on dictionary values
    - ` dictionary: ExternalResourceParam `: path to dictionary file in `tsv` or `csv` format
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `Contextual Parser` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setJsonPath(String)`: Path to json file with rules
    - `setCaseSensitive(Boolean)`: Whether to use case sensitive when matching values, default is false
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
        .setJsonPath("data/Stage.json")\
        .setCaseSensitive(False)\
        .setContextMatch(False)\
        .setUpdateTokenizer(False)\
        .setLazyAnnotator(False)
```
```scala
val contextualParser = new ContextualParserApproach()
        .setInputCols(Array("sentence", "token"))
        .setOutputCol("entity_stage")
        .setJsonPath("data/Stage.json")
        .setCaseSensitive(false)
        .setContextMatch(false)
        .setUpdateTokenizer(false)
        .setLazyAnnotator(false)
```

</div></div><div class="h3-box" markdown="1">

## RelationExtraction 

Extracts and classifier instances of relations between named entities.

**Input types:** pos, ner_chunk, embeddings, dependency

**Output type:** category

**Reference:** <a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.re.RelationExtractionApproach">RelationExtractionApproach</a> | 
<a href="https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.re.RelationExtractionModel">RelationExtractionModel</a>

**Functions:**

* ***Parameters***
    - ` labelColumn: Param[String] `: Column with label per each document
    - ` epochsN: IntParam `: Maximum number of epochs to train
    - ` batchSize: IntParam `: Batch Size
    - ` dropout: FloatParam `: Dropout coefficient
    - ` learningRate: FloatParam `: Learning Rate
    - ` modelFile: Param[String] `: the model file name
    - ` fixImbalance: BooleanParam `: Fix imbalance of training set
    - ` validationSplit: FloatParam `: proportion of training dataset to be validated against the model on each Epoch. The value should be between **0.0** and **1.0** and by default it is **0.0** and off.
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setLazyAnnotator(Boolean)`: Use `RelationExtraction` as a lazy annotator or not. *LazyAnnotator* is a Param in Annotators allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`
    - `setLabelColumn(String)`: Column with label per each document
    - `setEpochsNumber(int)`: Maximum number of epochs to train
    - `setBatchSize(int)`: Setter for Batch Size
    - `setDropout(dropout: Float)`: Sets Dropout coefficient
    - `setlearningRate(lr: Float)`: Sets Learning Rate
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

**Input Types:** Document, POS

**Reference:** [NerChunker](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.ner.NerChunker)  

**Functions:**

* ***Parameters***
    -  `regexParsers: StringArrayParam `: an array of grammar based chunk parsers 
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setRegexParsers(Array[String])`: A list of regex patterns to match chunks
    - `addRegexParser(String)`: adds a pattern to the current list of chunk patterns.
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
    .setRegexParsers(["<IMAGINGFINDINGS>*<BODYPART>"])\
    .setLazyAnnotator(False)
```

```scala
ner_model = NerDLModel.pretrained("ner_radiology", "en", "clinical/models")
    .setInputCols("sentence","token","embeddings")
    .setOutputCol("ner")

ner_chunker = NerChunker().
    .setInputCols(["sentence","ner"])
    .setOutputCol("ner_chunk")
    .setRegexParsers(["<IMAGINGFINDINGS>*<BODYPART>"])
    .setLazyAnnotator(False)
```

</div></div><div class="h3-box" markdown="1">

Refer to the NerChunker Scala docs for more details on the API.

## ChunkFilterer

ChunkFilterer will allow you to filter out named entities by some conditions or predefined look-up lists, so that you can feed these entities to other annotators like `Assertion Status` or `Entity Resolvers`. It can be used with two criteria: `isin` and `regex`.

**Output Type:** Chunk  

**Input Types:** Document, Chunk  

**Reference:** [ChunkFilterer](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.chunker.ChunkFilterer)

**Functions:**

* ***Parameters***
    - ` whiteList: StringArrayParam `: List of entities to process.
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    <!-- - `setCriteria(String)`: -->
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
    .setLazyAnnotator(False)
```

```scala
chunk_filterer = ChunkFilterer()
    .setInputCols("sentence","ner_chunk")
    .setOutputCol("chunk_filtered")
    .setCriteria("isin")
    .setWhiteList(["severe fever","sore throat"])
    .setLazyAnnotator(False)
```

</div></div><div class="h3-box" markdown="1">

Refer to the ChunkFilterer Scala docs for more details on the API.

## AssertionFilterer

`AssertionFilterer` will allow you to filter out the named entities by the list of acceptable assertion statuses. This annotator would be quite handy if you want to set a white list for the acceptable assertion statuses like present or conditional; and do not want absent conditions get out of your pipeline.

**Output Type:** Assertion  

**Input Types:** Document, Chunk, Embeddings

**Reference:** [AssertionFilterer](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.chunker.AssertionFilterer)

**Functions:**

* ***Parameters***
    - ` whiteList: StringArrayParam `: List of entities to process.
    -  `regex: StringArrayParam `: list of entities to process. The rest will be ignored.
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

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
    .setLazyAnnotator(False)
```

```scala
clinical_assertion = AssertionDLModel.pretrained("assertion_dl", "en", "clinical/models") \
  .setInputCols(["sentence", "ner_chunk", "embeddings"]) \
  .setOutputCol("assertion")

assertion_filterer = AssertionFilterer()\
    .setInputCols("sentence","ner_chunk","assertion")\
    .setOutputCol("assertion_filtered")\
    .setWhiteList(["present"])
    .setLazyAnnotator(False)
```

</div></div><div class="h3-box" markdown="1">

## DrugNormalizer

Standardize units of drugs and handle abbreviations in raw text or drug chunks identified by any NER model. This normalization significantly improves performance of entity resolvers.

**Output Type:** Document  

**Input Types:** Document

**Reference:** [DrugNormalizer](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.DrugNormalizer)

**Functions:**

* ***Parameters***
    -  `policy: Param[String] `:removalPolicy to remove patterns from text with a given policy
    -  `lowercase: BooleanParam `: whether to convert strings to lowercase 
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setPolicy(String)`: Setter for removalPolicy to remove patterns from text with a given policy
    - `setLazyAnnotator(Boolean)`: Use `DrugNormalizer` as a lazy annotator or not.
    - `setLowercase(Boolean)`: Lower case tokens, default `false` 

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getLazyAnnotator()`: Whether `DrugNormalizer` used as a lazy annotator or not.
    - `getPolicy()`: Getter for removalPolicy to remove patterns from text with a given policy
    - `getLowercase()`: Lower case tokens, default `false` 

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
drug_normalizer = DrugNormalizer()\
    .setInputCols("document")\
    .setOutputCol("document_normalized")\
    .setPolicy("all") #all/abbreviations/dosages
    .setLazyAnnotator(False)
```

```scala
drug_normalizer = DrugNormalizer()
    .setInputCols("document")
    .setOutputCol("document_normalized")
    .setPolicy("all") // all/abbreviations/dosages
    .setLazyAnnotator(false)
```

</div></div><div class="h3-box" markdown="1">

##  ChunkMerge

In order to use multiple NER models in the same pipeline, Spark NLP Healthcare has ChunkMerge Annotator that is used to return entities from each NER model by overlapping. Now it has a new parameter to avoid merging overlapping entities `setMergeOverlapping()` to return all the entities regardless of char indices. It will be quite useful to analyze what every NER module returns on the same text.

**Output Type:** Chunk  

**Input Types:** Chunk, Chunk

**Reference:** [ChunkMergeApproach](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.merge.ChunkMergeApproach) | [ChunkMergeModel](https://nlp.johnsnowlabs.com/licensed/api/index.html#com.johnsnowlabs.nlp.annotators.merge.ChunkMergeModel)

**Functions:**

* ***Parameters***
    -  `mergeOverlapping: BooleanParam `: whether to merge overlapping matched chunks. Defaults to `true` 
    - `lazyAnnotator: Boolean`: *LazyAnnotator* is a Param in Annotators that allows them to stand idle in the Pipeline and do nothing. Can be called by other Annotators in a `RecursivePipeline`

* ***Parameter Setters***

    - `setInputCol(String)`: Sets required input annotator types
    - `setOutputCol(String)`: Sets expected output annotator types
    - `setMergeOverlapping(String)`: Setter for whether to merge overlapping matched chunks.
    - `setLazyAnnotator(Boolean)`: Use `ChunkMerge` as a lazy annotator or not.

* ***Parameter Getters***

    - `getInputCols()`: Input annotations columns currently used
    - `getOutputCols()`: Gets annotation column name going to generate
    - `getMergeOverlapping()`: Getter for whether to merge overlapping matched chunks.
    - `getLazyAnnotator()`: Whether `ChunkMerge` used as a lazy annotator or not.

**Example:**

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPython.html %}

```python
chunk_merger_NonOverlapped = ChunkMergeApproach()\
    .setInputCols('clinical_bionlp_ner_chunk', "jsl_ner_chunk")\
    .setOutputCol('nonOverlapped_ner_chunk')\
    .setMergeOverlapping(False)
    .setLazyAnnotator(False)
```

```scala
chunk_merger_NonOverlapped = ChunkMergeApproach()
    .setInputCols("clinical_bionlp_ner_chunk", "jsl_ner_chunk")
    .setOutputCol("nonOverlapped_ner_chunk")
    .setMergeOverlapping(false)
    .setLazyAnnotator(false)
```

</div></div><div class="h3-box" markdown="1">