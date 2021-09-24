{%- capture title -%}
MedicalNerApproach
{%- endcapture -%}

{%- capture description -%}
This Named Entity recognition annotator allows to train a medical NER model based on Neural Networks.

The architecture of the neural network is a Char CNNs - BiLSTM - CRF that achieves state-of-the-art in most datasets.

The training data should be a labeled Spark Dataset, in the format of [CoNLL](/docs/en/training#conll-dataset)
2003 IOB with `Annotation` type columns. The data should have columns of type `DOCUMENT, TOKEN, WORD_EMBEDDINGS` and an
additional label column of annotator type `NAMED_ENTITY`.
Excluding the label, this can be done with for example
  - a [SentenceDetector](/docs/en/annotators#sentencedetector),
  - a [Tokenizer](/docs/en/annotators#tokenizer) and
  - a [WordEmbeddingsModel](/docs/en/annotators#wordembeddings) with clinical embeddings
  (any [clinical word embeddings](https://nlp.johnsnowlabs.com/models?task=Embeddings) can be chosen).

For extended examples of usage, see the [Spark NLP Workshop](https://github.com/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/1.Clinical_Named_Entity_Recognition_Model.ipynb)
(sections starting with `Training a Clinical NER`)
{%- endcapture -%}

{%- capture input_anno -%}
DOCUMENT, TOKEN, WORD_EMBEDDINGS
{%- endcapture -%}

{%- capture output_anno -%}
NAMED_ENTITY
{%- endcapture -%}

{%- capture python_example -%}
from sparknlp.annotator import *
from sparknlp_jsl.annotator import *
from sparknlp.base import *
import sparknlp_jsl
import sparknlp
from pyspark.ml import Pipeline

# First extract the prerequisites for the NerDLApproach
documentAssembler = DocumentAssembler() \
    .setInputCol("text") \
    .setOutputCol("document")

sentence = SentenceDetector() \
    .setInputCols(["document"]) \
    .setOutputCol("sentence")

tokenizer = Tokenizer() \
    .setInputCols(["sentence"]) \
    .setOutputCol("token")

clinical_embeddings = WordEmbeddingsModel.pretrained('embeddings_clinical', "en", "clinical/models")\
    .setInputCols(["sentence", "token"])\
    .setOutputCol("embeddings")

# Then the training can start
nerTagger = MedicalNerApproach()\
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
conll_data = CoNLL().readDataset(spark, 'NER_NCBIconlltrain.txt')

ner_model = ner_pipeline.fit(conll_data)

{%- endcapture -%}

{%- capture api_link -%}
[MedicalNERApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/ner/MedicalNerApproach.html)
{%- endcapture -%}

{% include templates/training_anno_template.md
title=title
description=description
input_anno=input_anno
output_anno=output_anno
python_example=python_example
api_link=api_link
%}
