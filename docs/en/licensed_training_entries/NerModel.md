{%- capture title -%}
NerModel
{%- endcapture -%}

{%- capture approach -%}
approach
{%- endcapture -%}

{%- capture approach_description -%}
This Named Entity recognition annotator allows to train generic NER model based on Neural Networks.

The architecture of the neural network is a Char CNNs - BiLSTM - CRF that achieves state-of-the-art in most datasets.

For instantiated/pretrained models, see NerDLModel.

The training data should be a labeled Spark Dataset, in the format of [CoNLL](/docs/en/training#conll-dataset)
2003 IOB with `Annotation` type columns. The data should have columns of type `DOCUMENT, TOKEN, WORD_EMBEDDINGS` and an
additional label column of annotator type `NAMED_ENTITY`.
Excluding the label, this can be done with for example
- a [SentenceDetector](/docs/en/annotators#sentencedetector),
- a [Tokenizer](/docs/en/annotators#tokenizer) and
- a [WordEmbeddingsModel](/docs/en/annotators#wordembeddings) with clinical embeddings
  (any [clinical word embeddings](https://nlp.johnsnowlabs.com/models?task=Embeddings) can be chosen).
{%- endcapture -%}

{%- capture approach_input_anno -%}
DOCUMENT, TOKEN, WORD_EMBEDDINGS
{%- endcapture -%}

{%- capture approach_output_anno -%}
NAMED_ENTITY
{%- endcapture -%}

{%- capture approach_python_medical -%}
from johnsnowlabs import * 

# First extract the prerequisites for the NerDLApproach
documentAssembler = nlp.DocumentAssembler() \
.setInputCol("text") \
.setOutputCol("document")

sentence = nlp.SentenceDetector() \
.setInputCols(["document"]) \
.setOutputCol("sentence")

tokenizer = nlp.Tokenizer() \
.setInputCols(["sentence"]) \
.setOutputCol("token")

clinical_embeddings = nlp.WordEmbeddingsModel.pretrained('embeddings_clinical', "en", "clinical/models")\
.setInputCols(["sentence", "token"])\
.setOutputCol("embeddings")

# Then the training can start
nerTagger = medical.NerApproach()\
.setInputCols(["sentence", "token", "embeddings"])\
.setLabelColumn("label")\
.setOutputCol("ner")\
.setMaxEpochs(2)\
.setBatchSize(64)\
.setRandomSeed(0)\
.setVerbose(1)\
.setValidationSplit(0.2)\
.setEvaluationLogExtended(True) \
.setEnableOutputLogs(True)\
.setIncludeConfidence(True)\
.setOutputLogsPath('ner_logs')\
.setGraphFolder('medical_ner_graphs')\
.setEnableMemoryOptimizer(True) #>> if you have a limited memory and a large conll file, you can set this True to train batch by batch

pipeline = Pipeline().setStages([
documentAssembler,
sentence,
tokenizer,
clinical_embeddings,
nerTagger
])

# We use the text and labels from the CoNLL dataset
conll = CoNLL()
trainingData = conll.readDataset(spark, "src/test/resources/conll2003/eng.train")

pipelineModel = pipeline.fit(trainingData)

{%- endcapture -%}


{%- capture approach_python_legal -%}
from johnsnowlabs import * 

# First extract the prerequisites for the NerDLApproach
documentAssembler = nlp.DocumentAssembler() \
.setInputCol("text") \
.setOutputCol("document")

sentence = nlp.SentenceDetector() \
.setInputCols(["document"]) \
.setOutputCol("sentence")

tokenizer = nlp.Tokenizer() \
.setInputCols(["sentence"]) \
.setOutputCol("token")

clinical_embeddings = nlp.WordEmbeddingsModel.pretrained('embeddings_clinical', "en", "clinical/models")\
.setInputCols(["sentence", "token"])\
.setOutputCol("embeddings")

# Then the training can start
nerTagger = legal.NerApproach()\
.setInputCols(["sentence", "token", "embeddings"])\
.setLabelColumn("label")\
.setOutputCol("ner")\
.setMaxEpochs(2)\
.setBatchSize(64)\
.setRandomSeed(0)\
.setVerbose(1)\
.setValidationSplit(0.2)\
.setEvaluationLogExtended(True) \
.setEnableOutputLogs(True)\
.setIncludeConfidence(True)\
.setOutputLogsPath('ner_logs')\
.setGraphFolder('medical_ner_graphs')\
.setEnableMemoryOptimizer(True) #>> if you have a limited memory and a large conll file, you can set this True to train batch by batch

pipeline = Pipeline().setStages([
documentAssembler,
sentence,
tokenizer,
clinical_embeddings,
nerTagger
])
{%- endcapture -%}

{%- capture approach_python_finance -%}
from johnsnowlabs import * 

# First extract the prerequisites for the NerDLApproach
documentAssembler = nlp.DocumentAssembler() \
.setInputCol("text") \
.setOutputCol("document")

sentence = nlp.SentenceDetector() \
.setInputCols(["document"]) \
.setOutputCol("sentence")

tokenizer = nlp.Tokenizer() \
.setInputCols(["sentence"]) \
.setOutputCol("token")

clinical_embeddings = nlp.WordEmbeddingsModel.pretrained('embeddings_clinical', "en", "clinical/models")\
.setInputCols(["sentence", "token"])\
.setOutputCol("embeddings")

# Then the training can start
nerTagger = finance.NerApproach()\
.setInputCols(["sentence", "token", "embeddings"])\
.setLabelColumn("label")\
.setOutputCol("ner")\
.setMaxEpochs(2)\
.setBatchSize(64)\
.setRandomSeed(0)\
.setVerbose(1)\
.setValidationSplit(0.2)\
.setEvaluationLogExtended(True) \
.setEnableOutputLogs(True)\
.setIncludeConfidence(True)\
.setOutputLogsPath('ner_logs')\
.setGraphFolder('medical_ner_graphs')\
.setEnableMemoryOptimizer(True) #>> if you have a limited memory and a large conll file, you can set this True to train batch by batch

pipeline = Pipeline().setStages([
documentAssembler,
sentence,
tokenizer,
clinical_embeddings,
nerTagger
])
{%- endcapture -%}







{%- capture approach_scala_medical -%}
from johnsnowlabs import * 

// First extract the prerequisites for the NerDLApproach
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val sentence = new nlp.SentenceDetector()
  .setInputCols("document")
  .setOutputCol("sentence")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("sentence")
  .setOutputCol("token")

val embeddings = nlp.BertEmbeddings.pretrained()
  .setInputCols(Array("sentence", "token"))
  .setOutputCol("embeddings")

// Then the training can start
val nerTagger =new medical.NerApproach()
.setInputCols(Array("sentence", "token", "embeddings"))
.setLabelColumn("label")
.setOutputCol("ner")
.setMaxEpochs(5)
.setLr(0.003f)
.setBatchSize(8)
.setRandomSeed(0)
.setVerbose(1)
.setEvaluationLogExtended(false)
.setEnableOutputLogs(false)
.setIncludeConfidence(true)


val pipeline = new Pipeline().setStages(Array(
  documentAssembler,
  sentence,
  tokenizer,
  embeddings,
  nerTagger
))

// We use the text and labels from the CoNLL dataset
val conll = CoNLL()
val trainingData = conll.readDataset(spark, "src/test/resources/conll2003/eng.train")

val pipelineModel = pipeline.fit(trainingData)

{%- endcapture -%}


{%- capture approach_scala_legal -%}
from johnsnowlabs import * 
// First extract the prerequisites for the NerDLApproach
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val sentence = new nlp.SentenceDetector()
  .setInputCols("document")
  .setOutputCol("sentence")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("sentence")
  .setOutputCol("token")

val embeddings = nlp.WordEmbeddingsModel
  .pretrained('embeddings_clinical', "en", "clinical/models")
  .setInputCols(Array("sentence", "token"))
  .setOutputCol("embeddings")

// Then the training can start
val nerTagger =new legal.NerApproach()
.setInputCols(Array("sentence", "token", "embeddings"))
.setLabelColumn("label")
.setOutputCol("ner")
.setMaxEpochs(5)
.setLr(0.003f)
.setBatchSize(8)
.setRandomSeed(0)
.setVerbose(1)
.setEvaluationLogExtended(false)
.setEnableOutputLogs(false)
.setIncludeConfidence(true)

val pipeline = new Pipeline().setStages(Array(
  documentAssembler,
  sentence,
  tokenizer,
  embeddings,
  nerTagger
))
{%- endcapture -%}

{%- capture approach_scala_finance -%}
from johnsnowlabs import * 
// First extract the prerequisites for the NerDLApproach
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val sentence = new nlp.SentenceDetector()
  .setInputCols("document")
  .setOutputCol("sentence")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("sentence")
  .setOutputCol("token")

val embeddings = nlp.WordEmbeddingsModel
  .pretrained('embeddings_clinical', "en", "clinical/models")
  .setInputCols(Array("sentence", "token"))
  .setOutputCol("embeddings")

// Then the training can start
val nerTagger =new finance.NerApproach()
.setInputCols(Array("sentence", "token", "embeddings"))
.setLabelColumn("label")
.setOutputCol("ner")
.setMaxEpochs(5)
.setLr(0.003f)
.setBatchSize(8)
.setRandomSeed(0)
.setVerbose(1)
.setEvaluationLogExtended(false)
.setEnableOutputLogs(false)
.setIncludeConfidence(true)

val pipeline = new Pipeline().setStages(Array(
  documentAssembler,
  sentence,
  tokenizer,
  embeddings,
  nerTagger
))
{%- endcapture -%}


{%- capture approach_api_link -%}
[MedicalNerApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/ner/MedicalNerApproach.html)
{%- endcapture -%}

{%- capture python_api_link -%}
[MedicalNerApproach](https://nlp.johnsnowlabs.com/licensed/api/python/reference/autosummary/sparknlp_jsl.annotator.MedicalNerApproach.html)
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
