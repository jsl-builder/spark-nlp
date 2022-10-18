{%- capture title -%}
RelationExtractionApproach
{%- endcapture -%}

{%- capture approach -%}
approach
{%- endcapture -%}

{%- capture approach_description -%}
Trains a Relation Extraction Model to predict attributes and relations for entities in a sentence.

Relation Extraction is the key component for building relation knowledge graphs, and it is of crucial significance to natural language 
processing applications such as structured search, sentiment analysis, question answering, and summarization.

The dataset will be a csv with the following that contains the following columns (`sentence`,`chunk1`,`firstCharEnt1`,`lastCharEnt1`,`label1`,`chunk2`,`firstCharEnt2`,`lastCharEnt2`,`label2`,`rel`),

This annotator can be don with for example:
Excluding the rel, this can be done with for example
- a [SentenceDetector](/docs/en/annotators#sentencedetector),
- a [Tokenizer](/docs/en/annotators#tokenizer) and
- a [WordEmbeddingsModel](/docs/en/annotators#wordembeddings)
  (any word embeddings can be chosen, e.g. [BertEmbeddings](/docs/en/transformers#bertembeddings) for BERT based embeddings).
- a Chunk can be created using the `firstCharEnt1`, `lastCharEnt1`,`chunk1`, `label1` columns and `firstCharEnt2`, `lastCharEnt2`, `chunk2`,  `label2` `columns`
- 


An example of that dataset can be found in the following link [i2b2_clinical_dataset](https://raw.githubusercontent.com/JohnSnowLabs/spark-nlp-workshop/master/tutorials/Certification_Trainings/Healthcare/data/i2b2_clinical_rel_dataset.csv)

```
sentence,chunk1,firstCharEnt1,lastCharEnt1,label1,chunk2,firstCharEnt2,lastCharEnt2,label2,rel						
Previous studies have reported the association of prodynorphin (PDYN) promoter polymorphism with temporal lobe epilepsy (TLE) susceptibility, but the results remain inconclusive.,PDYN,64,67,GENE,epilepsy,111,118,PHENOTYPE,0						
The remaining cases, clinically similar to XLA, are autosomal recessive agammaglobulinemia (ARA).,XLA,43,45,GENE,autosomal recessive,52,70,PHENOTYPE,0						
YAP/TAZ have been reported to be highly expressed in malignant tumors.,YAP,19,21,GENE,tumors,82,87,PHENOTYPE,0						

```
Apart from that, no additional training data is needed.
{%- endcapture -%}

{%- capture approach_input_anno -%}
WORD_EMBEDDINGS, POS, CHUNK, DEPENDENCY
{%- endcapture -%}

{%- capture approach_output_anno -%}
CATEGORY
{%- endcapture -%}

{%- capture approach_python_medical -%}


from johnsnowlabs import *


annotationType = T.StructType([
T.StructField('annotatorType', T.StringType(), False),
T.StructField('begin', T.IntegerType(), False),
T.StructField('end', T.IntegerType(), False),
T.StructField('result', T.StringType(), False),
T.StructField('metadata', T.MapType(T.StringType(), T.StringType()), False),
T.StructField('embeddings', T.ArrayType(T.FloatType()), False)
])


@F.udf(T.ArrayType(annotationType))
def createTrainAnnotations(begin1, end1, begin2, end2, chunk1, chunk2, label1, label2):
    entity1 = sparknlp.annotation.Annotation("chunk", begin1, end1, chunk1, {'entity': label1.upper(), 'sentence': '0'}, [])
    entity2 = sparknlp.annotation.Annotation("chunk", begin2, end2, chunk2, {'entity': label2.upper(), 'sentence': '0'}, [])    
        
    entity1.annotatorType = "chunk"
    entity2.annotatorType = "chunk"
    return [entity1, entity2]

data = spark.read.option("header","true").format("csv").load("i2b2_clinical_rel_dataset.csv")


data = data
    .withColumn("begin1i", F.expr("cast(firstCharEnt1 AS Int)"))
    .withColumn("end1i", F.expr("cast(lastCharEnt1 AS Int)"))
    .withColumn("begin2i", F.expr("cast(firstCharEnt2 AS Int)"))
    .withColumn("end2i", F.expr("cast(lastCharEnt2 AS Int)"))
    .where("begin1i IS NOT NULL")
    .where("end1i IS NOT NULL")
    .where("begin2i IS NOT NULL")
    .where("end2i IS NOT NULL")
    .withColumn(
    "train_ner_chunks",
    createTrainAnnotations(
        "begin1i", "end1i", "begin2i", "end2i", "chunk1", "chunk2", "label1", "label2"
    ).alias("train_ner_chunks", metadata={'annotatorType': "chunk"}))



documentAssembler = nlp.DocumentAssembler() \
    .setInputCol("sentence") \
    .setOutputCol("sentences")


tokenizer = nlp.Tokenizer() \
    .setInputCols("sentences") \
    .setOutputCol("token")

words_embedder = nlp.WordEmbeddingsModel()
    .pretrained("embeddings_clinical", "en", "clinical/models")
    .setInputCols(["sentences", "tokens"])
    .setOutputCol("embeddings")

pos_tagger = nlp.PerceptronModel()
    .pretrained("pos_clinical", "en", "clinical/models")
    .setInputCols(["sentences", "tokens"])
    .setOutputCol("pos_tags")

dependency_parser = nlp.DependencyParserModel()
    .pretrained("dependency_conllu", "en")
    .setInputCols(["sentences", "pos_tags", "tokens"])
    .setOutputCol("dependencies")

reApproach = medical.RelationExtractionApproach()
    .setInputCols(["embeddings", "pos_tags", "train_ner_chunks", "dependencies"])
    .setOutputCol("relations")
    .setLabelColumn("rel")
    .setEpochsNumber(70)
    .setBatchSize(200)
    .setDropout(0.5)
    .setLearningRate(0.001)
    .setModelFile("/content/RE_in1200D_out20.pb")
    .setFixImbalance(True)
    .setFromEntity("begin1i", "end1i", "label1")
    .setToEntity("begin2i", "end2i", "label2")
    .setOutputLogsPath('/content')

train_pipeline = Pipeline(stages=[
            documenter,
            tokenizer,
            words_embedder,
            pos_tagger,
            dependency_parser,
            reApproach
])
rel_model = train_pipeline.fit(data)

{%- endcapture -%}

{%- capture approach_python_legal -%}
from johnsnowlabs import *
# Defining pipeline stages to extract entities first
documentAssembler = nlp.DocumentAssembler() \
  .setInputCol("text") \
  .setOutputCol("document")

tokenizer = nlp.Tokenizer() \
  .setInputCols(["document"]) \
  .setOutputCol("tokens")

embedder = nlp.WordEmbeddingsModel \
  .pretrained("embeddings_clinical", "en", "clinical/models") \
  .setInputCols(["document", "tokens"]) \
  .setOutputCol("embeddings")

posTagger = nlp.PerceptronModel \
  .pretrained("pos_clinical", "en", "clinical/models") \
  .setInputCols(["document", "tokens"]) \
  .setOutputCol("posTags")

nerTagger = medical.NerModel \
  .pretrained("ner_events_clinical", "en", "clinical/models") \
  .setInputCols(["document", "tokens", "embeddings"]) \
  .setOutputCol("ner_tags")

nerConverter = nlp.NerConverter() \
  .setInputCols(["document", "tokens", "ner_tags"]) \
  .setOutputCol("nerChunks")

depencyParser = nlp.DependencyParserModel \
  .pretrained("dependency_conllu", "en") \
  .setInputCols(["document", "posTags", "tokens"]) \
  .setOutputCol("dependencies")

# Then define `RelationExtractionApproach` and training parameters
re = legal.RelationExtractionApproach() \
  .setInputCols(["embeddings", "posTags", "train_ner_chunks", "dependencies"]) \
  .setOutputCol("relations_t") \
  .setLabelColumn("target_rel") \
  .setEpochsNumber(300) \
  .setBatchSize(200) \
  .setLearningRate(0.001) \
  .setModelFile("path/to/graph_file.pb") \
  .setFixImbalance(True) \
  .setValidationSplit(0.05) \
  .setFromEntity("from_begin", "from_end", "from_label") \
  .setToEntity("to_begin", "to_end", "to_label")

finisher = nlp.Finisher() \
  .setInputCols(["relations_t"]) \
  .setOutputCols(["relations"]) \
  .setCleanAnnotations(False) \
  .setValueSplitSymbol(",") \
  .setAnnotationSplitSymbol(",") \
  .setOutputAsArray(False)

# Define complete pipeline and start training
pipeline = Pipeline(stages=[
    documentAssembler,
    tokenizer,
    embedder,
    posTagger,
    nerTagger,
    nerConverter,
    depencyParser,
    re,
    finisher])

model = pipeline.fit(trainData)

{%- endcapture -%}


{%- capture approach_python_finance -%}
from johnsnowlabs import *
# Defining pipeline stages to extract entities first
documentAssembler = nlp.DocumentAssembler() \
  .setInputCol("text") \
  .setOutputCol("document")

tokenizer = nlp.Tokenizer() \
  .setInputCols(["document"]) \
  .setOutputCol("tokens")

embedder = nlp.WordEmbeddingsModel \
  .pretrained("embeddings_clinical", "en", "clinical/models") \
  .setInputCols(["document", "tokens"]) \
  .setOutputCol("embeddings")

posTagger = nlp.PerceptronModel \
  .pretrained("pos_clinical", "en", "clinical/models") \
  .setInputCols(["document", "tokens"]) \
  .setOutputCol("posTags")

nerTagger = medical.NerModel \
  .pretrained("ner_events_clinical", "en", "clinical/models") \
  .setInputCols(["document", "tokens", "embeddings"]) \
  .setOutputCol("ner_tags")

nerConverter = nlp.NerConverter() \
  .setInputCols(["document", "tokens", "ner_tags"]) \
  .setOutputCol("nerChunks")

depencyParser = nlp.DependencyParserModel \
  .pretrained("dependency_conllu", "en") \
  .setInputCols(["document", "posTags", "tokens"]) \
  .setOutputCol("dependencies")

# Then define `RelationExtractionApproach` and training parameters
re = finance.RelationExtractionApproach() \
  .setInputCols(["embeddings", "posTags", "train_ner_chunks", "dependencies"]) \
  .setOutputCol("relations_t") \
  .setLabelColumn("target_rel") \
  .setEpochsNumber(300) \
  .setBatchSize(200) \
  .setLearningRate(0.001) \
  .setModelFile("path/to/graph_file.pb") \
  .setFixImbalance(True) \
  .setValidationSplit(0.05) \
  .setFromEntity("from_begin", "from_end", "from_label") \
  .setToEntity("to_begin", "to_end", "to_label")

finisher = nlp.Finisher() \
  .setInputCols(["relations_t"]) \
  .setOutputCols(["relations"]) \
  .setCleanAnnotations(False) \
  .setValueSplitSymbol(",") \
  .setAnnotationSplitSymbol(",") \
  .setOutputAsArray(False)

# Define complete pipeline and start training
pipeline = Pipeline(stages=[
    documentAssembler,
    tokenizer,
    embedder,
    posTagger,
    nerTagger,
    nerConverter,
    depencyParser,
    re,
    finisher])

model = pipeline.fit(trainData)

{%- endcapture -%}




{%- capture approach_scala_medical -%}

from johnsnowlabs import *


val data = spark.read.option("header",true).csv("src/test/resources/re/gene_hpi.csv").limit(10)



def createTrainAnnotations = udf {
 ( begin1:Int, end1:Int, begin2:Int, end2:Int, chunk1:String, chunk2:String, label1:String, label2:String) => {

    val an1 =   Annotation(CHUNK,begin1,end1,chunk1,Map("entity" -> label1.toUpperCase,"sentence" -> "0"))
    val an2 =   Annotation(CHUNK,begin2,end2,chunk2,Map("entity" -> label2.toUpperCase,"sentence" -> "0"))
    Seq(an1,an2)
 }

}
val metadataBuilder: MetadataBuilder = new MetadataBuilder()
val meta = metadataBuilder.putString("annotatorType", CHUNK).build()

val dataEncoded =  data
.withColumn("begin1i", expr("cast(firstCharEnt1 AS Int)"))
.withColumn("end1i", expr("cast(lastCharEnt1 AS Int)"))
.withColumn("begin2i", expr("cast(firstCharEnt2 AS Int)"))
.withColumn("end2i", expr("cast(lastCharEnt2 AS Int)"))
.where("begin1i IS NOT NULL")
.where("end1i IS NOT NULL")
.where("begin2i IS NOT NULL")
.where("end2i IS NOT NULL")
.withColumn(
  "train_ner_chunks",
  createTrainAnnotations(
    col("begin1i"), col("end1i"), col("begin2i"), col("end2i"), col("chunk1"), col("chunk2"), col("label1"), col("label2")
  ).as("train_ner_chunks",meta))

val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("sentence")
  .setOutputCol("sentences")

val tokenizer = new nlp.Tokenizer()
  .setInputCols(Array("sentences"))
  .setOutputCol("tokens")

val embedder = ParallelDownload(nlp.WordEmbeddingsModel
  .pretrained("embeddings_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens"))
  .setOutputCol("embeddings"))

val posTagger = ParallelDownload(nlp.PerceptronModel
  .pretrained("pos_clinical", "en", "clinical/models")
  .setInputCols(Array("sentences", "tokens"))
  .setOutputCol("posTags"))

val nerTagger = ParallelDownload(medical.NerModel
  .pretrained("ner_events_clinical", "en", "clinical/models")
  .setInputCols(Array("sentences", "tokens", "embeddings"))
  .setOutputCol("ner_tags"))

val nerConverter = new nlp.NerConverter()
  .setInputCols(Array("sentences", "tokens", "ner_tags"))
  .setOutputCol("nerChunks")

val depencyParser = ParallelDownload(nlp.DependencyParserModel
  .pretrained("dependency_conllu", "en")
  .setInputCols(Array("sentences", "posTags", "tokens"))
  .setOutputCol("dependencies"))

val re = new nlp.RelationExtractionApproach()
  .setInputCols(Array("embeddings", "posTags", "train_ner_chunks", "dependencies"))
  .setOutputCol("rel")
  .setLabelColumn("target_rel")
  .setEpochsNumber(30)
  .setBatchSize(200)
  .setlearningRate(0.001f)
  .setValidationSplit(0.05f)
  .setFromEntity("begin1i", "end1i", "label1")
  .setToEntity("end2i", "end2i", "label2")



val pipeline = new Pipeline()
  .setStages(Array(
    documentAssembler,
    tokenizer,
    embedder,
    posTagger,
    nerTagger,
    nerConverter,
    depencyParser,
    re).parallelDownload)

    val model = pipeline.fit(dataEncoded)

{%- endcapture -%}


{%- capture approach_scala_legal -%}
from johnsnowlabs import * 
// Defining pipeline stages to extract entities first
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("tokens")

val embedder = nlp.WordEmbeddingsModel
  .pretrained("embeddings_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens"))
  .setOutputCol("embeddings")

val posTagger = nlp.PerceptronModel
  .pretrained("pos_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens"))
  .setOutputCol("posTags")

val nerTagger = medical.NerModel
  .pretrained("ner_events_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens", "embeddings"))
  .setOutputCol("ner_tags")

val nerConverter = new nlp.NerConverter()
  .setInputCols(Array("document", "tokens", "ner_tags"))
  .setOutputCol("nerChunks")

val depencyParser = nlp.DependencyParserModel
  .pretrained("dependency_conllu", "en")
  .setInputCols(Array("document", "posTags", "tokens"))
  .setOutputCol("dependencies")

// Then define `RelationExtractionApproach` and training parameters
val re = new legal.RelationExtractionApproach()
  .setInputCols(Array("embeddings", "posTags", "train_ner_chunks", "dependencies"))
  .setOutputCol("relations_t")
  .setLabelColumn("target_rel")
  .setEpochsNumber(300)
  .setBatchSize(200)
  .setlearningRate(0.001f)
  .setModelFile("path/to/graph_file.pb")
  .setFixImbalance(true)
  .setValidationSplit(0.05f)
  .setFromEntity("from_begin", "from_end", "from_label")
  .setToEntity("to_begin", "to_end", "to_label")

val finisher = new nlp.Finisher()
  .setInputCols(Array("relations_t"))
  .setOutputCols(Array("relations"))
  .setCleanAnnotations(false)
  .setValueSplitSymbol(",")
  .setAnnotationSplitSymbol(",")
  .setOutputAsArray(false)

// Define complete pipeline and start training
val pipeline = new Pipeline()
  .setStages(Array(
    documentAssembler,
    tokenizer,
    embedder,
    posTagger,
    nerTagger,
    nerConverter,
    depencyParser,
    re,
    finisher))

val model = pipeline.fit(trainData)

{%- endcapture -%}


{%- capture approach_scala_finance -%}
from johnsnowlabs import * 
// Defining pipeline stages to extract entities first
val documentAssembler = new nlp.DocumentAssembler()
  .setInputCol("text")
  .setOutputCol("document")

val tokenizer = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("tokens")

val embedder = nlp.WordEmbeddingsModel
  .pretrained("embeddings_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens"))
  .setOutputCol("embeddings")

val posTagger = nlp.PerceptronModel
  .pretrained("pos_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens"))
  .setOutputCol("posTags")

val nerTagger = medical.NerModel
  .pretrained("ner_events_clinical", "en", "clinical/models")
  .setInputCols(Array("document", "tokens", "embeddings"))
  .setOutputCol("ner_tags")

val nerConverter = new nlp.NerConverter()
  .setInputCols(Array("document", "tokens", "ner_tags"))
  .setOutputCol("nerChunks")

val depencyParser = nlp.DependencyParserModel
  .pretrained("dependency_conllu", "en")
  .setInputCols(Array("document", "posTags", "tokens"))
  .setOutputCol("dependencies")

// Then define `RelationExtractionApproach` and training parameters
val re = new finance.RelationExtractionApproach()
  .setInputCols(Array("embeddings", "posTags", "train_ner_chunks", "dependencies"))
  .setOutputCol("relations_t")
  .setLabelColumn("target_rel")
  .setEpochsNumber(300)
  .setBatchSize(200)
  .setlearningRate(0.001f)
  .setModelFile("path/to/graph_file.pb")
  .setFixImbalance(true)
  .setValidationSplit(0.05f)
  .setFromEntity("from_begin", "from_end", "from_label")
  .setToEntity("to_begin", "to_end", "to_label")

val finisher = new nlp.Finisher()
  .setInputCols(Array("relations_t"))
  .setOutputCols(Array("relations"))
  .setCleanAnnotations(false)
  .setValueSplitSymbol(",")
  .setAnnotationSplitSymbol(",")
  .setOutputAsArray(false)

// Define complete pipeline and start training
val pipeline = new Pipeline()
  .setStages(Array(
    documentAssembler,
    tokenizer,
    embedder,
    posTagger,
    nerTagger,
    nerConverter,
    depencyParser,
    re,
    finisher))

val model = pipeline.fit(trainData)

{%- endcapture -%}



{%- capture approach_api_link -%}
[RelationExtractionApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/re/RelationExtractionApproach.html)
{%- endcapture -%}

{%- capture python_api_link -%}
[RelationExtractionApproach](https://nlp.johnsnowlabs.com/licensed/api/python/reference/autosummary/sparknlp_jsl.annotator.RelationExtractionApproach.html)
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
