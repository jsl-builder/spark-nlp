{%- capture title -%}
SentenceEntityResolver
{%- endcapture -%}

{%- capture approach -%}
approach
{%- endcapture -%}


{%- capture approach_description -%}
Contains all the parameters and methods to train a SentenceEntityResolverModel.
The model transforms a dataset with Input Annotation type SENTENCE_EMBEDDINGS, coming from e.g.
[BertSentenceEmbeddings](/docs/en/transformers#bertsentenceembeddings)
and returns the normalized entity for a particular trained ontology / curated dataset.
(e.g. ICD-10, RxNorm, SNOMED etc.)

To use pretrained models please use SentenceEntityResolverModel
and see the [Models Hub](https://nlp.johnsnowlabs.com/models?task=Entity+Resolution) for available models.
{%- endcapture -%}

{%- capture approach_input_anno -%}
SENTENCE_EMBEDDINGS
{%- endcapture -%}

{%- capture approach_output_anno -%}
ENTITY
{%- endcapture -%}

{%- capture approach_python_medical -%}
from johnsnowlabs import *
# Training a SNOMED resolution model using BERT sentence embeddings
# Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data and their labels.
documentAssembler = nlp.DocumentAssembler() \
  .setInputCol("normalized_text") \
  .setOutputCol("document")

sentenceDetector = nlp.SentenceDetector()\
  .setInputCols(["document"])\
  .setOutputCol("sentence")

bertEmbeddings = nlp.BertSentenceEmbeddings.pretrained("sent_biobert_pubmed_base_cased") \
  .setInputCols(["sentence"]) \
  .setOutputCol("bert_embeddings")

snomedTrainingPipeline = Pipeline(stages=[
  documentAssembler,
  sentenceDetector,
  bertEmbeddings
])
snomedTrainingModel = snomedTrainingPipeline.fit(data)
snomedData = snomedTrainingModel.transform(data).cache()

# Then the Resolver can be trained with
bertExtractor = medical.SentenceEntityResolverApproach() \
  .setNeighbours(25) \
  .setThreshold(1000) \
  .setInputCols(["bert_embeddings"]) \
  .setNormalizedCol("normalized_text") \
  .setLabelCol("label") \
  .setOutputCol("snomed_code") \
  .setDistanceFunction("EUCLIDIAN") \
  .setCaseSensitive(False)

snomedModel = bertExtractor.fit(snomedData)

{%- endcapture -%}

{%- capture approach_python_legal -%}
from johnsnowlabs import * 

# Training a SNOMED resolution model using BERT sentence embeddings
# Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data and their labels.
documentAssembler = nlp.DocumentAssembler() \
  .setInputCol("normalized_text") \
  .setOutputCol("document")

sentenceDetector = nlp.SentenceDetector()\
  .setInputCols(["document"])\
  .setOutputCol("sentence")

bertEmbeddings = nlp.BertSentenceEmbeddings.pretrained("sent_biobert_pubmed_base_cased") \
  .setInputCols(["sentence"]) \
  .setOutputCol("bert_embeddings")

snomedTrainingPipeline = Pipeline(stages=[
  documentAssembler,
  sentenceDetector,
  bertEmbeddings
])
snomedTrainingModel = snomedTrainingPipeline.fit(data)
snomedData = snomedTrainingModel.transform(data).cache()

# Then the Resolver can be trained with
bertExtractor = legal.SentenceEntityResolverApproach() \
  .setNeighbours(25) \
  .setThreshold(1000) \
  .setInputCols(["bert_embeddings"]) \
  .setNormalizedCol("normalized_text") \
  .setLabelCol("label") \
  .setOutputCol("snomed_code") \
  .setDistanceFunction("EUCLIDIAN") \
  .setCaseSensitive(False)

snomedModel = bertExtractor.fit(snomedData)

{%- endcapture -%}

{%- capture approach_python_finance -%}
from johnsnowlabs import * 

# Training a SNOMED resolution model using BERT sentence embeddings
# Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data and their labels.
documentAssembler = nlp.DocumentAssembler() \
  .setInputCol("normalized_text") \
  .setOutputCol("document")

sentenceDetector = nlp.SentenceDetector()\
  .setInputCols(["document"])\
  .setOutputCol("sentence")

bertEmbeddings = nlp.BertSentenceEmbeddings.pretrained("sent_biobert_pubmed_base_cased") \
  .setInputCols(["sentence"]) \
  .setOutputCol("bert_embeddings")

snomedTrainingPipeline = Pipeline(stages=[
  documentAssembler,
  sentenceDetector,
  bertEmbeddings
])
snomedTrainingModel = snomedTrainingPipeline.fit(data)
snomedData = snomedTrainingModel.transform(data).cache()

# Then the Resolver can be trained with
bertExtractor = finance.SentenceEntityResolverApproach() \
  .setNeighbours(25) \
  .setThreshold(1000) \
  .setInputCols(["bert_embeddings"]) \
  .setNormalizedCol("normalized_text") \
  .setLabelCol("label") \
  .setOutputCol("snomed_code") \
  .setDistanceFunction("EUCLIDIAN") \
  .setCaseSensitive(False)

snomedModel = bertExtractor.fit(snomedData)

{%- endcapture -%}




{%- capture approach_scala_medical -%}
from johnsnowlabs import * 
// Training a SNOMED resolution model using BERT sentence embeddings
// Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data and their labels.
val documentAssembler = new nlp.DocumentAssembler()
   .setInputCol("normalized_text")
   .setOutputCol("document")

val sentenceDetector = new nlp.SentenceDetector()
  .setInputCols("document")
  .setOutputCol("sentence")

 val bertEmbeddings = nlp.BertSentenceEmbeddings.pretrained("sent_biobert_pubmed_base_cased")
   .setInputCols("sentence")
   .setOutputCol("bert_embeddings")

 val snomedTrainingPipeline = new Pipeline().setStages(Array(
   documentAssembler,
   sentenceDetector,
   bertEmbeddings
 ))
 val snomedTrainingModel = snomedTrainingPipeline.fit(data)
 val snomedData = snomedTrainingModel.transform(data).cache()

// Then the Resolver can be trained with
val bertExtractor = new medical.SentenceEntityResolverApproach()
  .setNeighbours(25)
  .setThreshold(1000)
  .setInputCols("bert_embeddings")
  .setNormalizedCol("normalized_text")
  .setLabelCol("label")
  .setOutputCol("snomed_code")
  .setDistanceFunction("EUCLIDIAN")
  .setCaseSensitive(false)

val snomedModel = bertExtractor.fit(snomedData)

{%- endcapture -%}

{%- capture approach_scala_legal -%}
from johnsnowlabs import * 
// Training a SNOMED resolution model using BERT sentence embeddings
// Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data and their labels.
val documentAssembler = new nlp.DocumentAssembler()
   .setInputCol("normalized_text")
   .setOutputCol("document")

val sentenceDetector = nlp.SentenceDetector()
  .setInputCols("document")
  .setOutputCol("sentence")

 val bertEmbeddings = nlp.BertSentenceEmbeddings.pretrained("sent_biobert_pubmed_base_cased")
   .setInputCols("sentence")
   .setOutputCol("bert_embeddings")
 val snomedTrainingPipeline = new Pipeline().setStages(Array(
   documentAssembler,
   sentenceDetector,
   bertEmbeddings
 ))
 val snomedTrainingModel = snomedTrainingPipeline.fit(data)
 val snomedData = snomedTrainingModel.transform(data).cache()

// Then the Resolver can be trained with
val bertExtractor = new legal.SentenceEntityResolverApproach()
  .setNeighbours(25)
  .setThreshold(1000)
  .setInputCols("bert_embeddings")
  .setNormalizedCol("normalized_text")
  .setLabelCol("label")
  .setOutputCol("snomed_code")
  .setDistanceFunction("EUCLIDIAN")
  .setCaseSensitive(false)

val snomedModel = bertExtractor.fit(snomedData)

{%- endcapture -%}

{%- capture approach_scala_finance -%}
from johnsnowlabs import * 
// Training a SNOMED resolution model using BERT sentence embeddings
// Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data and their labels.
val documentAssembler = new nlp.DocumentAssembler()
   .setInputCol("normalized_text")
   .setOutputCol("document")

val sentenceDetector = nlp.SentenceDetector()
  .setInputCols("document")
  .setOutputCol("sentence")

 val bertEmbeddings = nlp.BertSentenceEmbeddings.pretrained("sent_biobert_pubmed_base_cased")
   .setInputCols("sentence")
   .setOutputCol("bert_embeddings")
 val snomedTrainingPipeline = new Pipeline().setStages(Array(
   documentAssembler,
   sentenceDetector,
   bertEmbeddings
 ))
 val snomedTrainingModel = snomedTrainingPipeline.fit(data)
 val snomedData = snomedTrainingModel.transform(data).cache()

// Then the Resolver can be trained with
val bertExtractor = new finance.SentenceEntityResolverApproach()
  .setNeighbours(25)
  .setThreshold(1000)
  .setInputCols("bert_embeddings")
  .setNormalizedCol("normalized_text")
  .setLabelCol("label")
  .setOutputCol("snomed_code")
  .setDistanceFunction("EUCLIDIAN")
  .setCaseSensitive(false)

val snomedModel = bertExtractor.fit(snomedData)

{%- endcapture -%}





{%- capture approach_api_link -%}
[SentenceEntityResolverApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/resolution/SentenceEntityResolverApproach)
{%- endcapture -%}

{%- capture python_api_link -%}
[SentenceEntityResolverApproach](https://nlp.johnsnowlabs.com/licensed/api/python/reference/autosummary/sparknlp_jsl.annotator.SentenceEntityResolverApproach.html)
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
