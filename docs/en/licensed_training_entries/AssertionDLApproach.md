{%- capture title -%}
AssertionDLApproach
{%- endcapture -%}

{%- capture approach -%}
approach
{%- endcapture -%}

{%- capture approach_description -%}
Train a Assertion Model algorithm using deep learning. 

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

assertion_status = medical.AssertionDLApproach() \
.setInputCols("sentence", "chunk", "embeddings") \
.setOutputCol("assertion") \
.setStartCol("start") \
.setEndCol("end") \
.setLabelCol("label") \
.setLearningRate(0.01) \
.setDropout(0.15) \
.setBatchSize(16) \
.setEpochs(3) \
.setValidationSplit(0.2) \
.setIncludeConfidence(True)

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
# First, pipeline stages for pre-processing the dataset (containing columns for text and label) are defined.
document = nlp.DocumentAssembler()\
    .setInputCol("sentence")\
    .setOutputCol("document")
chunk = nlp.Doc2Chunk()\
    .setInputCols("document")\
    .setOutputCol("doc_chunk")\
    .setChunkCol("chunk")\
    .setStartCol("tkn_start")\
    .setStartColByTokenIndex(True)\
    .setFailOnMissing(False)\
    .setLowerCase(False)
token = nlp.Tokenizer()\
    .setInputCols(['document'])\
    .setOutputCol('token')
roberta_embeddings = nlp.RoBertaEmbeddings.pretrained("roberta_embeddings_legal_roberta_base","en") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("embeddings") \
    .setMaxSentenceLength(512)

# Define AssertionDLApproach with parameters and start training
assertionStatus = legal.AssertionDLApproach()\
    .setLabelCol("assertion_label")\
    .setInputCols("document", "doc_chunk", "embeddings")\
    .setOutputCol("assertion")\
    .setBatchSize(128)\
    .setLearningRate(0.001)\
    .setEpochs(2)\
    .setStartCol("tkn_start")\
    .setEndCol("tkn_end")\
    .setMaxSentLen(1200)\
    .setEnableOutputLogs(True)\
    .setOutputLogsPath('training_logs/')\
    .setGraphFolder(graph_folder)\
    .setGraphFile(f"{graph_folder}/assertion_graph.pb")\
    .setTestDataset(path="test_data.parquet", read_as='SPARK', options={'format': 'parquet'})\
    .setScopeWindow(scope_window)
    #.setValidationSplit(0.2)\    
    #.setDropout(0.1)\    

trainingPipeline = Pipeline().setStages([
    document,
    chunk,
    token,
    roberta_embeddings,
    assertionStatus
])

assertionModel = trainingPipeline.fit(data)
assertionResults = assertionModel.transform(data).cache()
{%- endcapture -%}

{%- capture approach_python_finance -%}
from johnsnowlabs import * 
# First, pipeline stages for pre-processing the dataset (containing columns for text and label) are defined.
document = nlp.DocumentAssembler() \
    .setInputCol("text") \
    .setOutputCol("document")
chunk = nlp.Doc2Chunk() \
    .setInputCols(["document"]) \
    .setOutputCol("chunk")
token = nlp.Tokenizer() \
    .setInputCols(["document"]) \
    .setOutputCol("token")
embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("embeddings")

# Define AssertionDLApproach with parameters and start training
assertionStatus = finance.AssertionDLApproach() \
    .setLabelCol("label") \
    .setInputCols(["document", "chunk", "embeddings"]) \
    .setOutputCol("assertion") \
    .setBatchSize(128) \
    .setDropout(0.012) \
    .setLearningRate(0.015) \
    .setEpochs(1) \
    .setStartCol("start") \
    .setEndCol("end") \
    .setMaxSentLen(250)

trainingPipeline = Pipeline().setStages([
    document,
    chunk,
    token,
    embeddings,
    assertionStatus
])

assertionModel = trainingPipeline.fit(data)
assertionResults = assertionModel.transform(data).cache()
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

val assertionStatus = new medical.AssertionDLApproach()
      .setInputCols(Array("sentence", "chunk", "embeddings"))
      .setOutputCol("assertion")
      .setStartCol("start")
      .setEndCol("end")
      .setLabelCol("label")
      .setLearningRate(0.01f)
      .setDropout(0.15f)
      .setBatchSize(16)
      .setEpochs(3)
      .setValidationSplit(0.2f)

val pipeline = new Pipeline().setStages(Array(
documentAssembler, 
sentenceDetector, 
tokenizer, 
POSTag, 
chunker, 
pubmed,
assertionStatus
))


datasetPath = "/../src/test/resources/rsAnnotations-1-120-random.csv"
train_data = SparkContextForTest.spark.read.option("header", "true").csv(path="file:///" + os.getcwd() + datasetPath)

val pipelineModel = pipeline.fit(trainingData)

{%- endcapture -%}

{%- capture approach_scala_legal -%}
from johnsnowlabs import * 

val document = new nlp.DocumentAssembler()
    .setInputCol("sentence")
    .setOutputCol("document")
val chunk = new nlp.Doc2Chunk()
    .setInputCols("document")
    .setOutputCol("doc_chunk")
    .setChunkCol("chunk")
    .setStartCol("tkn_start")
    .setStartColByTokenIndex(True)
    .setFailOnMissing(False)
    .setLowerCase(False)
val token = new nlp.Tokenizer()
    .setInputCols('document')
    .setOutputCol('token')
val roberta_embeddings = nlp.RoBertaEmbeddings.pretrained("roberta_embeddings_legal_roberta_base","en")
    .setInputCols(Array("document", "token"))
    .setOutputCol("embeddings")
    .setMaxSentenceLength(512)

# Define AssertionDLApproach with parameters and start training
val assertionStatus = new legal.AssertionDLApproach()
    .setLabelCol("assertion_label")
    .setInputCols(Array("document", "doc_chunk", "embeddings"))
    .setOutputCol("assertion")
    .setBatchSize(128)
    .setLearningRate(0.001)
    .setEpochs(2)
    .setStartCol("tkn_start")
    .setEndCol("tkn_end")
    .setMaxSentLen(1200)
    .setEnableOutputLogs(True)
    .setOutputLogsPath('training_logs/')
    .setGraphFolder(graph_folder)
    .setGraphFile(f"{graph_folder}/assertion_graph.pb")
    .setTestDataset(path="test_data.parquet", read_as='SPARK', options={'format': 'parquet'})
    .setScopeWindow(scope_window)
    #.setValidationSplit(0.2)  
    #.setDropout(0.1)

val trainingPipeline = new Pipeline().setStages(Array(
  document,
  chunk,
  token,
  roberta_embeddings,
  assertionStatus
))

val assertionModel = trainingPipeline.fit(data)
val assertionResults = assertionModel.transform(data).cache()
{%- endcapture -%}

{%- capture approach_scala_finance -%}
from johnsnowlabs import * 

// First, pipeline stages for pre-processing the dataset (containing columns for text and label) are defined.
val document = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")
val chunk = new nlp.Doc2Chunk()
  .setInputCols("document")
  .setOutputCol("chunk")
val token = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("token")
val embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "token"))
  .setOutputCol("embeddings")

// Define AssertionDLApproach with parameters and start training
val assertionStatus = new finance.AssertionDLApproach()
  .setLabelCol("label")
  .setInputCols(Array("document", "chunk", "embeddings"))
  .setOutputCol("assertion")
  .setBatchSize(128)
  .setDropout(0.012f)
  .setLearningRate(0.015f)
  .setEpochs(1)
  .setStartCol("start")
  .setEndCol("end")
  .setMaxSentLen(250)

val trainingPipeline = new Pipeline().setStages(Array(
  document,
  chunk,
  token,
  embeddings,
  assertionStatus
))

val assertionModel = trainingPipeline.fit(data)
val assertionResults = assertionModel.transform(data).cache()
{%- endcapture -%}



{%- capture approach_api_link -%}
[AssertionDLApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/assertion/dl/AssertionDLApproach.html)
{%- endcapture -%}

{%- capture python_api_link -%}
[AssertionDLApproach](https://nlp.johnsnowlabs.com/licensed/api/python/reference/autosummary/sparknlp_jsl.annotator.AssertionDLApproach.html)
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
