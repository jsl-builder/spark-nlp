{%- capture title -%}
AssertionLogRegApproach
{%- endcapture -%}

{%- capture approach -%}
approach
{%- endcapture -%}

{%- capture approach_description -%}
Train a Assertion Model algorithm using a regression log model. 

The training data should have annotations columns of type `DOCUMENT`, `CHUNK`, `WORD_EMBEDDINGS`, the `label`column (The assertion status that you want to predict), the `start` (the start index for the term that has the assertion status),
the `end` column (the end index for the term that has the assertion status).This model use a deep learning to predict the entity.

Excluding the label, this can be done with for example
- a [SentenceDetector](/docs/en/annotators#sentencedetector),
- a [Chunk](https://nlp.johnsnowlabs.com/docs/en/annotators#chunker) ,
- a [WordEmbeddingsModel](/docs/en/annotators#wordembeddings)
  (any word embeddings can be chosen, e.g. [BertEmbeddings](/docs/en/transformers#bertembeddings) for BERT based embeddings).

{%- endcapture -%}

{%- capture approach_input_anno -%}
DOCUMENT, CHUNK, WORD_EMBEDDINGS
{%- endcapture -%}

{%- capture approach_output_anno -%}
ASSERTION
{%- endcapture -%}

{%- capture approach_python_medical -%}

from johnsnowlabs import *

document_assembler = nlp.DocumentAssembler().setInputCol('text').setOutputCol('document')

sentence_detector = nlp.SentenceDetector().setInputCols(["document"]).setOutputCol("sentence")

tokenizer = nlp.Tokenizer().setInputCols("sentence").setOutputCol("token")

POSTag = nlp.PerceptronModel.pretrained() \
.setInputCols("sentence", "token") \
.setOutputCol("pos")

chunker = nlp.Chunker() \
.setInputCols(["pos", "sentence"]) \
.setOutputCol("chunk") \
.setRegexParsers(["(<NN>)+"])

pubmed = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical","en","clinical/models") \
.setInputCols("sentence", "token") \
.setOutputCol("embeddings") \
.setCaseSensitive(False)

assertion_status = medical.AssertionLogRegApproach() \
.setInputCols("sentence", "chunk", "embeddings") \
.setOutputCol("assertion") \
.setStartCol("start") \
.setEndCol("end") \
.setLabelCol("label") \
.setReg(0.01) \
.setBefore(11) \
.setAfter(13) \
.setEpochs(3) 


pipeline = Pipeline().setStages([
document_assembler,
sentence_detector,
tokenizer,
POSTag,
chunker,
pubmed,
assertion_status
])


conll = CoNLL()
trainingData = conll.readDataset(spark, "src/test/resources/conll2003/eng.train")

pipelineModel = pipeline.fit(trainingData)

{%- endcapture -%}


{%- capture approach_python_legal -%}
from johnsnowlabs import *
# Training with Glove Embeddings
# First define pipeline stages to extract embeddings and text chunks
documentAssembler = nlp.DocumentAssembler() \
    .setInputCol("text") \
    .setOutputCol("document")

tokenizer = nlp.Tokenizer() \
    .setInputCols(["document"]) \
    .setOutputCol("token")

glove = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("word_embeddings") \
    .setCaseSensitive(False)

chunkAssembler = nlp.Doc2Chunk() \
    .setInputCols(["document"]) \
    .setChunkCol("target") \
    .setOutputCol("chunk")

# Then the AssertionLogRegApproach model is defined. Label column is needed in the dataset for training.
assertion = legal.AssertionLogRegApproach() \
    .setLabelCol("label") \
    .setInputCols(["document", "chunk", "word_embeddings"]) \
    .setOutputCol("assertion") \
    .setReg(0.01) \
    .setBefore(11) \
    .setAfter(13) \
    .setStartCol("start") \
    .setEndCol("end")

assertionPipeline = Pipeline(stages=[
    documentAssembler,
    sentenceDetector,
    tokenizer,
    embeddings,
    nerModel,
    nerConverter,
    assertion
])

assertionModel = assertionPipeline.fit(dataset)
{%- endcapture -%}


{%- capture approach_python_finance -%}
from johnsnowlabs import *
# Training with Glove Embeddings
# First define pipeline stages to extract embeddings and text chunks
documentAssembler = nlp.DocumentAssembler() \
    .setInputCol("text") \
    .setOutputCol("document")

tokenizer = nlp.Tokenizer() \
    .setInputCols(["document"]) \
    .setOutputCol("token")

glove = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("word_embeddings") \
    .setCaseSensitive(False)

chunkAssembler = nlp.Doc2Chunk() \
    .setInputCols(["document"]) \
    .setChunkCol("target") \
    .setOutputCol("chunk")

# Then the AssertionLogRegApproach model is defined. Label column is needed in the dataset for training.
assertion = finance.AssertionLogRegApproach() \
    .setLabelCol("label") \
    .setInputCols(["document", "chunk", "word_embeddings"]) \
    .setOutputCol("assertion") \
    .setReg(0.01) \
    .setBefore(11) \
    .setAfter(13) \
    .setStartCol("start") \
    .setEndCol("end")

assertionPipeline = Pipeline(stages=[
    documentAssembler,
    sentenceDetector,
    tokenizer,
    embeddings,
    nerModel,
    nerConverter,
    assertion
])

assertionModel = assertionPipeline.fit(dataset)
{%- endcapture -%}



{%- capture approach_scala_medical -%}
// This CoNLL dataset already includes the sentence, token, pos and label column with their respective annotator types.
// If a custom dataset is used, these need to be defined.

from johnsnowlabs import *

val documentAssembler = new nlp.DocumentAssembler()
.setInputCol("text")
.setOutputCol("document")

val sentenceDetector = new nlp.SentenceDetector()
.setInputCols("document")
.setOutputCol("sentence")

val tokenizer = new nlp.Tokenizer()
.setInputCols("sentence")
.setOutputCol("token")

val POSTag = nlp.PerceptronModel
.pretrained()
.setInputCols(Array("sentence", "token"))
.setOutputCol("pos")

val chunker = new nlp.Chunker()
.setInputCols(Array("pos", "sentence"))
.setOutputCol("chunk")
.setRegexParsers(Array("(<NN>)+"))

val pubmed = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical","en","clinical/models")
.setInputCols(Array("sentence", "token"))
.setOutputCol("embeddings")
.setCaseSensitive(false)

val assertion = new medical.AssertionLogRegApproach()
.setLabelCol("label")
.setInputCols(Array("document", "chunk", "embeddings"))
.setOutputCol("assertion")
.setReg(0.01)
.setBefore(11)
.setAfter(13)
.setStartCol("start")
.setEndCol("end")

val pipeline = new Pipeline().setStages(Array(
documentAssembler,
sentenceDetector,
tokenizer,
POSTag,
chunker,
pubmed,
assertion
))

datasetPath = "/../src/test/resources/rsAnnotations-1-120-random.csv"
train_data = SparkContextForTest.spark.read.option("header", "true").csv(path="file:///" + os.getcwd() + datasetPath)

val pipelineModel = pipeline.fit(trainingData)

{%- endcapture -%}

{%- capture approach_scala_legal -%}
from johnsnowlabs import * 

// Training with Glove Embeddings
// First define pipeline stages to extract embeddings and text chunks
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("token")

val glove = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "token"))
  .setOutputCol("word_embeddings")
  .setCaseSensitive(false)

val chunkAssembler = new nlp.Doc2Chunk()
  .setInputCols("document")
  .setChunkCol("target")
  .setOutputCol("chunk")

// Then the AssertionLogRegApproach model is defined. Label column is needed in the dataset for training.
val assertion = new legal.AssertionLogRegApproach()
  .setLabelCol("label")
  .setInputCols(Array("document", "chunk", "word_embeddings"))
  .setOutputCol("assertion")
  .setReg(0.01)
  .setBefore(11)
  .setAfter(13)
  .setStartCol("start")
  .setEndCol("end")

val assertionPipeline = new Pipeline().setStages(Array(
  documentAssembler,
  sentenceDetector,
  tokenizer,
  embeddings,
  nerModel,
  nerConverter,
  assertion
))

val assertionModel = assertionPipeline.fit(dataset)
{%- endcapture -%}

{%- capture approach_scala_finance -%}
from johnsnowlabs import * 

// Training with Glove Embeddings
// First define pipeline stages to extract embeddings and text chunks
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("token")

val glove = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "token"))
  .setOutputCol("word_embeddings")
  .setCaseSensitive(false)

val chunkAssembler = new nlp.Doc2Chunk()
  .setInputCols("document")
  .setChunkCol("target")
  .setOutputCol("chunk")

// Then the AssertionLogRegApproach model is defined. Label column is needed in the dataset for training.
val assertion = new finance.AssertionLogRegApproach()
  .setLabelCol("label")
  .setInputCols(Array("document", "chunk", "word_embeddings"))
  .setOutputCol("assertion")
  .setReg(0.01)
  .setBefore(11)
  .setAfter(13)
  .setStartCol("start")
  .setEndCol("end")

val assertionPipeline = new Pipeline().setStages(Array(
  documentAssembler,
  sentenceDetector,
  tokenizer,
  embeddings,
  nerModel,
  nerConverter,
  assertion
))

val assertionModel = assertionPipeline.fit(dataset)
{%- endcapture -%}


{%- capture approach_api_link -%}
[AssertionLogRegApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/assertion/logreg/AssertionLogRegApproach.html)
{%- endcapture -%}

{%- capture python_api_link -%}
[AssertionLogRegApproach](https://nlp.johnsnowlabs.comlicensed/api/python/reference/autosummary/sparknlp_jsl.annotator.AssertionLogRegApproach.html)

{%- endcapture -%}



{% include templates/licensed_approach_model_medical_fin_leg_template.md
title=title
approach=approach
approach_description=approach_description
approach_input_anno=approach_input_anno
approach_output_anno=approach_output_anno
approach_python_medical=approach_python_medical
approach_python_legal=approach_python_legal
approach_python_finance=approach_python_finance
approach_scala_medical=approach_scala_medical
approach_scala_legal=approach_scala_legal
approach_scala_finance=approach_scala_finance
python_api_link=python_api_link
approach_api_link=approach_api_link
%}
