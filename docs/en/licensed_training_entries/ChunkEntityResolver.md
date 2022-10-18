{%- capture title -%}
ChunkEntityResolver
{%- endcapture -%}

{%- capture approach -%}
approach
{%- endcapture -%}

{%- capture approach_description -%}
Contains all the parameters and methods to train a ChunkEntityResolverModel.
It transform a dataset with two Input Annotations of types TOKEN and WORD_EMBEDDINGS, coming from e.g. ChunkTokenizer
and ChunkEmbeddings Annotators and returns the normalized entity for a particular trained ontology / curated dataset.
(e.g. ICD-10, RxNorm, SNOMED etc.)

To use pretrained models please use ChunkEntityResolverModel
and see the [Models Hub](https://nlp.johnsnowlabs.com/models?task=Entity+Resolution) for available models.
{%- endcapture -%}

{%- capture approach_input_anno -%}
TOKEN, WORD_EMBEDDINGS
{%- endcapture -%}

{%- capture approach_output_anno -%}
ENTITY
{%- endcapture -%}

{%- capture approach_python_medical -%}
from johnsnowlabs import *
# Training a SNOMED model
# Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data
# and their labels.
document = nlp.DocumentAssembler() \
    .setInputCol("normalized_text") \
    .setOutputCol("document")

chunk = nlp.Doc2Chunk() \
    .setInputCols(["document"]) \
    .setOutputCol("chunk")

token = nlp.Tokenizer() \
    .setInputCols(["document"]) \
    .setOutputCol("token")

embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_healthcare_100d", "en", "clinical/models") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("embeddings")

chunkEmb = nlp.ChunkEmbeddings() \
        .setInputCols(["chunk", "embeddings"]) \
        .setOutputCol("chunk_embeddings")

snomedTrainingPipeline = Pipeline().setStages([
    document,
    chunk,
    token,
    embeddings,
    chunkEmb
])

snomedTrainingModel = snomedTrainingPipeline.fit(data)

snomedData = snomedTrainingModel.transform(data).cache()

# Then the Resolver can be trained with
snomedExtractor = medical.ChunkEntityResolverApproach() \
    .setInputCols(["token", "chunk_embeddings"]) \
    .setOutputCol("recognized") \
    .setNeighbours(1000) \
    .setAlternatives(25) \
    .setNormalizedCol("normalized_text") \
    .setLabelCol("label") \
    .setEnableWmd(True).setEnableTfidf(True).setEnableJaccard(True) \
    .setEnableSorensenDice(True).setEnableJaroWinkler(True).setEnableLevenshtein(True) \
    .setDistanceWeights([1, 2, 2, 1, 1, 1]) \
    .setAllDistancesMetadata(True) \
    .setPoolingStrategy("MAX") \
    .setThreshold(1e32)
model = snomedExtractor.fit(snomedData)

{%- endcapture -%}


{%- capture approach_python_legal -%}
from johnsnowlabs import *
# Training a SNOMED model
# Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data
# and their labels.
document = nlp.DocumentAssembler() \
    .setInputCol("normalized_text") \
    .setOutputCol("document")

chunk = nlp.Doc2Chunk() \
    .setInputCols(["document"]) \
    .setOutputCol("chunk")

token = nlp.Tokenizer() \
    .setInputCols(["document"]) \
    .setOutputCol("token")

embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_healthcare_100d", "en", "clinical/models") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("embeddings")

chunkEmb = nlp.ChunkEmbeddings() \
        .setInputCols(["chunk", "embeddings"]) \
        .setOutputCol("chunk_embeddings")

snomedTrainingPipeline = Pipeline().setStages([
    document,
    chunk,
    token,
    embeddings,
    chunkEmb
])

snomedTrainingModel = snomedTrainingPipeline.fit(data)

snomedData = snomedTrainingModel.transform(data).cache()

# Then the Resolver can be trained with
snomedExtractor = legal.ChunkEntityResolverApproach() \
    .setInputCols(["token", "chunk_embeddings"]) \
    .setOutputCol("recognized") \
    .setNeighbours(1000) \
    .setAlternatives(25) \
    .setNormalizedCol("normalized_text") \
    .setLabelCol("label") \
    .setEnableWmd(True).setEnableTfidf(True).setEnableJaccard(True) \
    .setEnableSorensenDice(True).setEnableJaroWinkler(True).setEnableLevenshtein(True) \
    .setDistanceWeights([1, 2, 2, 1, 1, 1]) \
    .setAllDistancesMetadata(True) \
    .setPoolingStrategy("MAX") \
    .setThreshold(1e32)
model = snomedExtractor.fit(snomedData)

{%- endcapture -%}


{%- capture approach_python_finance -%}
from johnsnowlabs import *
# Training a SNOMED model
# Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data
# and their labels.
document = nlp.DocumentAssembler() \
    .setInputCol("normalized_text") \
    .setOutputCol("document")

chunk = nlp.Doc2Chunk() \
    .setInputCols(["document"]) \
    .setOutputCol("chunk")

token = nlp.Tokenizer() \
    .setInputCols(["document"]) \
    .setOutputCol("token")

embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_healthcare_100d", "en", "clinical/models") \
    .setInputCols(["document", "token"]) \
    .setOutputCol("embeddings")

chunkEmb = nlp.ChunkEmbeddings() \
        .setInputCols(["chunk", "embeddings"]) \
        .setOutputCol("chunk_embeddings")

snomedTrainingPipeline = Pipeline().setStages([
    document,
    chunk,
    token,
    embeddings,
    chunkEmb
])

snomedTrainingModel = snomedTrainingPipeline.fit(data)

snomedData = snomedTrainingModel.transform(data).cache()

# Then the Resolver can be trained with
snomedExtractor = finance.ChunkEntityResolverApproach() \
    .setInputCols(["token", "chunk_embeddings"]) \
    .setOutputCol("recognized") \
    .setNeighbours(1000) \
    .setAlternatives(25) \
    .setNormalizedCol("normalized_text") \
    .setLabelCol("label") \
    .setEnableWmd(True).setEnableTfidf(True).setEnableJaccard(True) \
    .setEnableSorensenDice(True).setEnableJaroWinkler(True).setEnableLevenshtein(True) \
    .setDistanceWeights([1, 2, 2, 1, 1, 1]) \
    .setAllDistancesMetadata(True) \
    .setPoolingStrategy("MAX") \
    .setThreshold(1e32)
model = snomedExtractor.fit(snomedData)

{%- endcapture -%}



{%- capture approach_scala_medical -%}
// Training a SNOMED model
// Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data
// and their labels.
val document = new nlp.DocumentAssembler()
  .setInputCol("normalized_text")
  .setOutputCol("document")

val chunk = new nlp.Doc2Chunk()
  .setInputCols("document")
  .setOutputCol("chunk")

val token = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("token")

val embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_healthcare_100d", "en", "clinical/models")
  .setInputCols(Array("document", "token"))
  .setOutputCol("embeddings")

val chunkEmb = new nlp.ChunkEmbeddings()
      .setInputCols(Array("chunk", "embeddings"))
      .setOutputCol("chunk_embeddings")

val snomedTrainingPipeline = new Pipeline().setStages(Array(
  document,
  chunk,
  token,
  embeddings,
  chunkEmb
))

val snomedTrainingModel = snomedTrainingPipeline.fit(data)

val snomedData = snomedTrainingModel.transform(data).cache()

// Then the Resolver can be trained with
val snomedExtractor = new medical.ChunkEntityResolverApproach()
  .setInputCols(Array("token", "chunk_embeddings"))
  .setOutputCol("recognized")
  .setNeighbours(1000)
  .setAlternatives(25)
  .setNormalizedCol("normalized_text")
  .setLabelCol("label")
  .setEnableWmd(true).setEnableTfidf(true).setEnableJaccard(true)
  .setEnableSorensenDice(true).setEnableJaroWinkler(true).setEnableLevenshtein(true)
  .setDistanceWeights(Array(1, 2, 2, 1, 1, 1))
  .setAllDistancesMetadata(true)
  .setPoolingStrategy("MAX")
  .setThreshold(1e32)
val model = snomedExtractor.fit(snomedData)

{%- endcapture -%}


{%- capture approach_scala_legal -%}
// Training a SNOMED model
// Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data
// and their labels.
val document = new nlp.DocumentAssembler()
  .setInputCol("normalized_text")
  .setOutputCol("document")

val chunk = new nlp.Doc2Chunk()
  .setInputCols("document")
  .setOutputCol("chunk")

val token = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("token")

val embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_healthcare_100d", "en", "clinical/models")
  .setInputCols(Array("document", "token"))
  .setOutputCol("embeddings")

val chunkEmb = new nlp.ChunkEmbeddings()
      .setInputCols(Array("chunk", "embeddings"))
      .setOutputCol("chunk_embeddings")

val snomedTrainingPipeline = new Pipeline().setStages(Array(
  document,
  chunk,
  token,
  embeddings,
  chunkEmb
))

val snomedTrainingModel = snomedTrainingPipeline.fit(data)

val snomedData = snomedTrainingModel.transform(data).cache()

// Then the Resolver can be trained with
val snomedExtractor = new legal.ChunkEntityResolverApproach()
  .setInputCols(Array("token", "chunk_embeddings"))
  .setOutputCol("recognized")
  .setNeighbours(1000)
  .setAlternatives(25)
  .setNormalizedCol("normalized_text")
  .setLabelCol("label")
  .setEnableWmd(true).setEnableTfidf(true).setEnableJaccard(true)
  .setEnableSorensenDice(true).setEnableJaroWinkler(true).setEnableLevenshtein(true)
  .setDistanceWeights(Array(1, 2, 2, 1, 1, 1))
  .setAllDistancesMetadata(true)
  .setPoolingStrategy("MAX")
  .setThreshold(1e32)
val model = snomedExtractor.fit(snomedData)

{%- endcapture -%}


{%- capture approach_scala_finance -%}
// Training a SNOMED model
// Define pre-processing pipeline for training data. It needs consists of columns for the normalized training data
// and their labels.
val document = new nlp.DocumentAssembler()
  .setInputCol("normalized_text")
  .setOutputCol("document")

val chunk = new nlp.Doc2Chunk()
  .setInputCols("document")
  .setOutputCol("chunk")

val token = new nlp.Tokenizer()
  .setInputCols("document")
  .setOutputCol("token")

val embeddings = nlp.WordEmbeddingsModel.pretrained("embeddings_healthcare_100d", "en", "clinical/models")
  .setInputCols(Array("document", "token"))
  .setOutputCol("embeddings")

val chunkEmb = new nlp.ChunkEmbeddings()
      .setInputCols(Array("chunk", "embeddings"))
      .setOutputCol("chunk_embeddings")

val snomedTrainingPipeline = new Pipeline().setStages(Array(
  document,
  chunk,
  token,
  embeddings,
  chunkEmb
))

val snomedTrainingModel = snomedTrainingPipeline.fit(data)

val snomedData = snomedTrainingModel.transform(data).cache()

// Then the Resolver can be trained with
val snomedExtractor = new finance.ChunkEntityResolverApproach()
  .setInputCols(Array("token", "chunk_embeddings"))
  .setOutputCol("recognized")
  .setNeighbours(1000)
  .setAlternatives(25)
  .setNormalizedCol("normalized_text")
  .setLabelCol("label")
  .setEnableWmd(true).setEnableTfidf(true).setEnableJaccard(true)
  .setEnableSorensenDice(true).setEnableJaroWinkler(true).setEnableLevenshtein(true)
  .setDistanceWeights(Array(1, 2, 2, 1, 1, 1))
  .setAllDistancesMetadata(true)
  .setPoolingStrategy("MAX")
  .setThreshold(1e32)
val model = snomedExtractor.fit(snomedData)

{%- endcapture -%}

{%- capture approach_api_link -%}
[ChunkEntityResolverApproach](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/resolution/ChunkEntityResolverApproach)
{%- endcapture -%}


{%- capture python_api_link -%}
[ChunkEntityResolverApproach](https://nlp.johnsnowlabs.comlicensed/api/python/reference/autosummary/sparknlp_jsl.annotator.ChunkEntityResolverApproach.html)
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
