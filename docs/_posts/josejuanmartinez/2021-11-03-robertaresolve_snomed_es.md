---
layout: model
title: SNOMED Sentence Resolver (Spanish)
author: John Snow Labs
name: robertaresolve_snomed
date: 2021-11-03
tags: [embeddings, es, snomed, entity_resolution, clinical, licensed]
task: Entity Resolution
language: es
edition: Healthcare NLP 3.3.0
spark_version: 3.0
supported: true
annotator: SentenceEntityResolverModel
article_header:
type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description

This model maps predetected ner chunks (from a `MedicalNerModel`, a `ChunkConverter` and a `Chunk2Doc`) to SNOMED terms and codes for the Spanish version of SNOMED. It requires Roberta Clinical Word Embeddings (`roberta_base_biomedical_es`) averaged with `SentenceEmbeddings`.

## Predicted Entities

`SNOMED codes`

{:.btn-box}
<button class="button button-orange" disabled>Live Demo</button>
<button class="button button-orange" disabled>Open in Colab</button>
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/clinical/models/robertaresolve_snomed_es_3.3.0_3.0_1635933551478.zip){:.button.button-orange.button-orange-trans.arr.button-icon.hidden}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/clinical/models/robertaresolve_snomed_es_3.3.0_3.0_1635933551478.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use

Use any `MedicalNer` Model from our ModelsHub that detects, for example, diagnosis, for Spanish. Then, use a `NerConverter` (in case your model has B-I-O notation). Create documents using `Chunk2Doc`. Then use a `Tokenizer` the split the chunk, and finally use the `roberta_base_biomedical_es` Roberta Embeddings model and a `SentenceEmbeddings` annotator with an average pooling strategy, as in the example.

<div class="tabs-box" markdown="1">
{% include programmingLanguageSelectScalaPythonNLU.html %}
```python
...
c2doc = nlp.Chunk2Doc() \
.setInputCols("ner_chunk") \
.setOutputCol("sentence")

chunk_tokenizer = nlp.Tokenizer()\
.setInputCols("sentence")\
.setOutputCol("token")

chunk_word_embeddings = nlp.RoBertaEmbeddings.pretrained("roberta_base_biomedical", "es")\
.setInputCols(["sentence", "token"])\
.setOutputCol("ner_chunk_word_embeddings")

chunk_embeddings = nlp.SentenceEmbeddings() \
.setInputCols(["sentence", "ner_chunk_word_embeddings"]) \
.setOutputCol("ner_chunk_embeddings") \
.setPoolingStrategy("AVERAGE")

er = medical.SentenceEntityResolverModel.pretrained("robertaresolve_snomed", "es", "clinical/models")\
.setInputCols(["sentence", "ner_chunk_embeddings"]) \
.setOutputCol("snomed_code") \
.setDistanceFunction("EUCLIDEAN")

snomed_resolve_pipeline = Pipeline(stages = [
c2doc,
chunk_tokenizer,
chunk_word_embeddings,
chunk_embeddings,
er
])

empty = spark.createDataFrame([['']]).toDF("text")

p_model = snomed_resolve_pipeline.fit(empty)

test_sentence = "Mujer de 28 a??os con antecedentes de diabetes mellitus gestacional diagnosticada ocho a??os antes de la presentaci??n y posterior diabetes mellitus tipo dos (DM2), un episodio previo de pancreatitis inducida por HTG tres a??os antes de la presentaci??n, asociado con una hepatitis aguda, y obesidad con un ??ndice de masa corporal (IMC) de 33,5 kg / m2, que se present?? con antecedentes de una semana de poliuria, polidipsia, falta de apetito y v??mitos. Dos semanas antes de la presentaci??n, fue tratada con un ciclo de cinco d??as de amoxicilina por una infecci??n del tracto respiratorio. Estaba tomando metformina, glipizida y dapagliflozina para la DM2 y atorvastatina y gemfibrozil para la HTG. Hab??a estado tomando dapagliflozina durante seis meses en el momento de la presentaci??n. El examen f??sico al momento de la presentaci??n fue significativo para la mucosa oral seca; significativamente, su examen abdominal fue benigno sin dolor a la palpaci??n, protecci??n o rigidez. Los hallazgos de laboratorio pertinentes al ingreso fueron: glucosa s??rica 111 mg / dl, bicarbonato 18 mmol / l, ani??n gap 20, creatinina 0,4 mg / dl, triglic??ridos 508 mg / dl, colesterol total 122 mg / dl, hemoglobina glucosilada (HbA1c) 10%. y pH venoso 7,27. La lipasa s??rica fue normal a 43 U / L. Los niveles s??ricos de acetona no pudieron evaluarse ya que las muestras de sangre se mantuvieron hemolizadas debido a una lipemia significativa. La paciente ingres?? inicialmente por cetosis por inanici??n, ya que refiri?? una ingesta oral deficiente durante los tres d??as previos a la admisi??n. Sin embargo, la qu??mica s??rica obtenida seis horas despu??s de la presentaci??n revel?? que su glucosa era de 186 mg / dL, la brecha ani??nica todav??a estaba elevada a 21, el bicarbonato s??rico era de 16 mmol / L, el nivel de triglic??ridos alcanz?? un m??ximo de 2050 mg / dL y la lipasa fue de 52 U / L. Se obtuvo el nivel de ??-hidroxibutirato y se encontr?? que estaba elevado a 5,29 mmol / L; la muestra original se centrifug?? y la capa de quilomicrones se elimin?? antes del an??lisis debido a la interferencia de la turbidez causada por la lipemia nuevamente. El paciente fue tratado con un goteo de insulina para euDKA y HTG con una reducci??n de la brecha ani??nica a 13 y triglic??ridos a 1400 mg / dL, dentro de las 24 horas. Se pens?? que su euDKA fue precipitada por su infecci??n del tracto respiratorio en el contexto del uso del inhibidor de SGLT2. La paciente fue atendida por el servicio de endocrinolog??a y fue dada de alta con 40 unidades de insulina glargina por la noche, 12 unidades de insulina lispro con las comidas y metformina 1000 mg dos veces al d??a. Se determin?? que todos los inhibidores de SGLT2 deben suspenderse indefinidamente. Tuvo un seguimiento estrecho con endocrinolog??a post alta."

result = p_model.transform(spark.createDataFrame(pd.DataFrame({'text': [test_sentence]})))
```

```scala
...
val c2doc = new Chunk2Doc()
.setInputCols(Array("ner_chunk"))
.setOutputCol("sentence")    

val chunk_tokenizer = new Tokenizer()
.setInputCols("sentence")
.setOutputCol("token")

val chunk_word_embeddings = RoBertaEmbeddings.pretrained("roberta_base_biomedical", "es")
.setInputCols(Array("sentence", "token"))
.setOutputCol("ner_chunk_word_embeddings")

val chunk_embeddings = new SentenceEmbeddings()
.setInputCols(Array("sentence", "ner_chunk_word_embeddings"))
.setOutputCol("ner_chunk_embeddings")
.setPoolingStrategy("AVERAGE")

val er = SentenceEntityResolverModel.pretrained("robertaresolve_snomed", "es", "clinical/models")
.setInputCols(Array("sentence", "ner_chunk_embeddings"))
.setOutputCol("snomed_code")
.setDistanceFunction("EUCLIDEAN")

val snomed_pipeline = new Pipeline().setStages(Array(
c2doc,
chunk_tokenizer,
chunk_word_embeddings,
chunk_embeddings,
er))

val test_sentence = 'Mujer de 28 a??os con antecedentes de diabetes mellitus gestacional diagnosticada ocho a??os antes de la presentaci??n y posterior diabetes mellitus tipo dos (DM2), un episodio previo de pancreatitis inducida por HTG tres a??os antes de la presentaci??n, asociado con una hepatitis aguda, y obesidad con un ??ndice de masa corporal (IMC) de 33,5 kg / m2, que se present?? con antecedentes de una semana de poliuria, polidipsia, falta de apetito y v??mitos. Dos semanas antes de la presentaci??n, fue tratada con un ciclo de cinco d??as de amoxicilina por una infecci??n del tracto respiratorio. Estaba tomando metformina, glipizida y dapagliflozina para la DM2 y atorvastatina y gemfibrozil para la HTG. Hab??a estado tomando dapagliflozina durante seis meses en el momento de la presentaci??n. El examen f??sico al momento de la presentaci??n fue significativo para la mucosa oral seca; significativamente, su examen abdominal fue benigno sin dolor a la palpaci??n, protecci??n o rigidez. Los hallazgos de laboratorio pertinentes al ingreso fueron: glucosa s??rica 111 mg / dl, bicarbonato 18 mmol / l, ani??n gap 20, creatinina 0,4 mg / dl, triglic??ridos 508 mg / dl, colesterol total 122 mg / dl, hemoglobina glucosilada (HbA1c) 10%. y pH venoso 7,27. La lipasa s??rica fue normal a 43 U / L. Los niveles s??ricos de acetona no pudieron evaluarse ya que las muestras de sangre se mantuvieron hemolizadas debido a una lipemia significativa. La paciente ingres?? inicialmente por cetosis por inanici??n, ya que refiri?? una ingesta oral deficiente durante los tres d??as previos a la admisi??n. Sin embargo, la qu??mica s??rica obtenida seis horas despu??s de la presentaci??n revel?? que su glucosa era de 186 mg / dL, la brecha ani??nica todav??a estaba elevada a 21, el bicarbonato s??rico era de 16 mmol / L, el nivel de triglic??ridos alcanz?? un m??ximo de 2050 mg / dL y la lipasa fue de 52 U / L. Se obtuvo el nivel de ??-hidroxibutirato y se encontr?? que estaba elevado a 5,29 mmol / L; la muestra original se centrifug?? y la capa de quilomicrones se elimin?? antes del an??lisis debido a la interferencia de la turbidez causada por la lipemia nuevamente. El paciente fue tratado con un goteo de insulina para euDKA y HTG con una reducci??n de la brecha ani??nica a 13 y triglic??ridos a 1400 mg / dL, dentro de las 24 horas. Se pens?? que su euDKA fue precipitada por su infecci??n del tracto respiratorio en el contexto del uso del inhibidor de SGLT2. La paciente fue atendida por el servicio de endocrinolog??a y fue dada de alta con 40 unidades de insulina glargina por la noche, 12 unidades de insulina lispro con las comidas y metformina 1000 mg dos veces al d??a. Se determin?? que todos los inhibidores de SGLT2 deben suspenderse indefinidamente. Tuvo un seguimiento estrecho con endocrinolog??a post alta.'

val data = Seq(test_sentence).toDF("text")

val result = snomed_pipeline.fit(data).transform(data)
```


{:.nlu-block}
```python
import nlu
nlu.load("es.resolve.snomed").predict("""Mujer de 28 a??os con antecedentes de diabetes mellitus gestacional diagnosticada ocho a??os antes de la presentaci??n y posterior diabetes mellitus tipo dos (DM2), un episodio previo de pancreatitis inducida por HTG tres a??os antes de la presentaci??n, asociado con una hepatitis aguda, y obesidad con un ??ndice de masa corporal (IMC) de 33,5 kg / m2, que se present?? con antecedentes de una semana de poliuria, polidipsia, falta de apetito y v??mitos. Dos semanas antes de la presentaci??n, fue tratada con un ciclo de cinco d??as de amoxicilina por una infecci??n del tracto respiratorio. Estaba tomando metformina, glipizida y dapagliflozina para la DM2 y atorvastatina y gemfibrozil para la HTG. Hab??a estado tomando dapagliflozina durante seis meses en el momento de la presentaci??n. El examen f??sico al momento de la presentaci??n fue significativo para la mucosa oral seca; significativamente, su examen abdominal fue benigno sin dolor a la palpaci??n, protecci??n o rigidez. Los hallazgos de laboratorio pertinentes al ingreso fueron: glucosa s??rica 111 mg / dl, bicarbonato 18 mmol / l, ani??n gap 20, creatinina 0,4 mg / dl, triglic??ridos 508 mg / dl, colesterol total 122 mg / dl, hemoglobina glucosilada (HbA1c) 10%. y pH venoso 7,27. La lipasa s??rica fue normal a 43 U / L. Los niveles s??ricos de acetona no pudieron evaluarse ya que las muestras de sangre se mantuvieron hemolizadas debido a una lipemia significativa. La paciente ingres?? inicialmente por cetosis por inanici??n, ya que refiri?? una ingesta oral deficiente durante los tres d??as previos a la admisi??n. Sin embargo, la qu??mica s??rica obtenida seis horas despu??s de la presentaci??n revel?? que su glucosa era de 186 mg / dL, la brecha ani??nica todav??a estaba elevada a 21, el bicarbonato s??rico era de 16 mmol / L, el nivel de triglic??ridos alcanz?? un m??ximo de 2050 mg / dL y la lipasa fue de 52 U / L. Se obtuvo el nivel de ??-hidroxibutirato y se encontr?? que estaba elevado a 5,29 mmol / L; la muestra original se centrifug?? y la capa de quilomicrones se elimin?? antes del an??lisis debido a la interferencia de la turbidez causada por la lipemia nuevamente. El paciente fue tratado con un goteo de insulina para euDKA y HTG con una reducci??n de la brecha ani??nica a 13 y triglic??ridos a 1400 mg / dL, dentro de las 24 horas. Se pens?? que su euDKA fue precipitada por su infecci??n del tracto respiratorio en el contexto del uso del inhibidor de SGLT2. La paciente fue atendida por el servicio de endocrinolog??a y fue dada de alta con 40 unidades de insulina glargina por la noche, 12 unidades de insulina lispro con las comidas y metformina 1000 mg dos veces al d??a. Se determin?? que todos los inhibidores de SGLT2 deben suspenderse indefinidamente. Tuvo un seguimiento estrecho con endocrinolog??a post alta.""")
```

</div>

## Results

```bash
+----+-------------------------------+-------------+--------------+
|    | ner_chunk                     | entity      |   snomed_code|
|----+-------------------------------+-------------+--------------|
|  0 | diabetes mellitus gestacional | DIAGNOSTICO |     11687002 |
|  1 | diabetes mellitus tipo dos    | DIAGNOSTICO |     44054006 |
|  2 | pancreatitis                  | DIAGNOSTICO |     75694006 |
|  3 | HTG                           | DIAGNOSTICO |    266569009 |
|  4 | hepatitis aguda               | DIAGNOSTICO |     37871000 |
|  5 | obesidad                      | DIAGNOSTICO |      5476005 |
|  6 | ??ndice de masa corporal       | DIAGNOSTICO |    162859006 |
|  7 | poliuria                      | DIAGNOSTICO |     56574000 |
|  8 | polidipsia                    | DIAGNOSTICO |     17173007 |
|  9 | falta de apetito              | DIAGNOSTICO |     49233005 |
| 10 | v??mitos                       | DIAGNOSTICO |    422400008 |
| 11 | infecci??n                     | DIAGNOSTICO |     40733004 |
| 12 | HTG                           | DIAGNOSTICO |    266569009 |
| 13 | dolor                         | DIAGNOSTICO |     22253000 |
| 14 | rigidez                       | DIAGNOSTICO |    271587009 |
| 15 | cetosis                       | DIAGNOSTICO |      2538008 |
| 16 | infecci??n                     | DIAGNOSTICO |     40733004 |
+----+-------------------------------+-------------+--------------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|robertaresolve_snomed|
|Compatibility:|Healthcare NLP 3.3.0+|
|License:|Licensed|
|Edition:|Official|
|Input Labels:|[ner_chunk_doc, sentence_embeddings]|
|Output Labels:|[snomed_code]|
|Language:|es|
|Case sensitive:|false|
|Dependencies:|roberta_base_biomedical_es|
