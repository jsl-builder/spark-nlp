---
layout: model
title: Detect Living Species(roberta_base_biomedical)
author: John Snow Labs
name: ner_living_species_roberta
date: 2022-06-22
tags: [es, ner, clinical, licensed, roberta]
task: Named Entity Recognition
language: es
edition: Healthcare NLP 3.5.3
spark_version: 3.0
supported: true
annotator: MedicalNerModel
article_header:
type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description

Extract living species from clinical texts in Spanish which is critical to scientific disciplines like medicine, biology, ecology/biodiversity, nutrition and agriculture. This model is trained using `roberta_base_biomedical` embeddings.

It is trained on the [LivingNER](https://temu.bsc.es/livingner/) corpus that is composed of clinical case reports extracted from miscellaneous medical specialties including COVID, oncology, infectious diseases, tropical medicine, urology, pediatrics, and others.

## Predicted Entities

`HUMAN`, `SPECIES`

{:.btn-box}
<button class="button button-orange" disabled>Live Demo</button>
[Open in Colab](https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/1.Clinical_Named_Entity_Recognition_Model.ipynb){:.button.button-orange.button-orange-trans.co.button-icon}
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/clinical/models/ner_living_species_roberta_es_3.5.3_3.0_1655906938288.zip){:.button.button-orange.button-orange-trans.arr.button-icon.hidden}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/clinical/models/ner_living_species_roberta_es_3.5.3_3.0_1655906938288.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use



<div class="tabs-box" markdown="1">
{% include programmingLanguageSelectScalaPythonNLU.html %}

```python
document_assembler = DocumentAssembler()\
.setInputCol("text")\
.setOutputCol("document")

sentence_detector = SentenceDetectorDLModel.pretrained("sentence_detector_dl", "xx")\
.setInputCols(["document"])\
.setOutputCol("sentence")

tokenizer = Tokenizer()\
.setInputCols(["sentence"])\
.setOutputCol("token")

embeddings = RoBertaEmbeddings.pretrained("roberta_base_biomedical", "es")\
.setInputCols(["sentence", "token"])\
.setOutputCol("embeddings")

ner_model = MedicalNerModel.pretrained("ner_living_species_roberta", "es", "clinical/models")\
.setInputCols(["sentence", "token", "embeddings"])\
.setOutputCol("ner")

ner_converter = NerConverter()\
.setInputCols(["sentence", "token", "ner"])\
.setOutputCol("ner_chunk")

pipeline = Pipeline(stages=[
document_assembler, 
sentence_detector,
tokenizer,
embeddings,
ner_model,
ner_converter   
])

data = spark.createDataFrame([["""Lactante var??n de dos a??os. Antecedentes familiares sin inter??s. Antecedentes personales: Embarazo, parto y periodo neonatal normal. En seguimiento por alergia a legumbres, diagnosticado con diez meses por reacci??n urticarial generalizada con lentejas y garbanzos, con dieta de exclusi??n a legumbres desde entonces. En ??sta visita la madre describe episodios de eritema en zona maxilar derecha con afectaci??n ocular ipsilateral que se resuelve en horas tras la administraci??n de corticoides. Le ha ocurrido en 5-6 ocasiones, en relaci??n con la ingesta de alimentos previamente tolerados. Exploraci??n complementaria: Cacahuete, ac(ige)19.2 Ku.arb/l. Resultados: Ante la sospecha cl??nica de S??ndrome de Frey, se tranquiliza a los padres, explic??ndoles la naturaleza del cuadro y se cita para revisi??n anual."""]]).toDF("text")

result = pipeline.fit(data).transform(data)
```
```scala
val document_assembler = new DocumentAssembler()
.setInputCol("text")
.setOutputCol("document")

val sentence_detector = SentenceDetectorDLModel.pretrained("sentence_detector_dl", "xx")
.setInputCols(Array("document"))
.setOutputCol("sentence")

val tokenizer = new Tokenizer()
.setInputCols(Array("sentence"))
.setOutputCol("token")

val embeddings = RoBertaEmbeddings.pretrained("roberta_base_biomedical", "es")
.setInputCols(Array("sentence", "token"))
.setOutputCol("embeddings")

val ner_model = MedicalNerModel.pretrained("ner_living_species_roberta", "es", "clinical/models")
.setInputCols(Array("sentence", "token", "embeddings"))
.setOutputCol("ner")

val ner_converter = new NerConverter()
.setInputCols(Array("sentence", "token", "ner"))
.setOutputCol("ner_chunk")

val pipeline = new Pipeline().setStages(Array(document_assembler, 
sentence_detector,
tokenizer,
embeddings,
ner_model,
ner_converter))

val data = Seq("""Lactante var??n de dos a??os. Antecedentes familiares sin inter??s. Antecedentes personales: Embarazo, parto y periodo neonatal normal. En seguimiento por alergia a legumbres, diagnosticado con diez meses por reacci??n urticarial generalizada con lentejas y garbanzos, con dieta de exclusi??n a legumbres desde entonces. En ??sta visita la madre describe episodios de eritema en zona maxilar derecha con afectaci??n ocular ipsilateral que se resuelve en horas tras la administraci??n de corticoides. Le ha ocurrido en 5-6 ocasiones, en relaci??n con la ingesta de alimentos previamente tolerados. Exploraci??n complementaria: Cacahuete, ac(ige)19.2 Ku.arb/l. Resultados: Ante la sospecha cl??nica de S??ndrome de Frey, se tranquiliza a los padres, explic??ndoles la naturaleza del cuadro y se cita para revisi??n anual.""").toDS.toDF("text")

val result = pipeline.fit(data).transform(data)
```


{:.nlu-block}
```python
import nlu
nlu.load("es.med_ner.living_species.roberta").predict("""Lactante var??n de dos a??os. Antecedentes familiares sin inter??s. Antecedentes personales: Embarazo, parto y periodo neonatal normal. En seguimiento por alergia a legumbres, diagnosticado con diez meses por reacci??n urticarial generalizada con lentejas y garbanzos, con dieta de exclusi??n a legumbres desde entonces. En ??sta visita la madre describe episodios de eritema en zona maxilar derecha con afectaci??n ocular ipsilateral que se resuelve en horas tras la administraci??n de corticoides. Le ha ocurrido en 5-6 ocasiones, en relaci??n con la ingesta de alimentos previamente tolerados. Exploraci??n complementaria: Cacahuete, ac(ige)19.2 Ku.arb/l. Resultados: Ante la sospecha cl??nica de S??ndrome de Frey, se tranquiliza a los padres, explic??ndoles la naturaleza del cuadro y se cita para revisi??n anual.""")
```

</div>

## Results

```bash
+--------------+-------+
|ner_chunk     |label  |
+--------------+-------+
|Lactante var??n|HUMAN  |
|familiares    |HUMAN  |
|personales    |HUMAN  |
|neonatal      |HUMAN  |
|legumbres     |SPECIES|
|lentejas      |SPECIES|
|garbanzos     |SPECIES|
|legumbres     |SPECIES|
|madre         |HUMAN  |
|Cacahuete     |SPECIES|
|padres        |HUMAN  |
+--------------+-------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|ner_living_species_roberta|
|Compatibility:|Healthcare NLP 3.5.3+|
|License:|Licensed|
|Edition:|Official|
|Input Labels:|[sentence, token, embeddings]|
|Output Labels:|[ner]|
|Language:|es|
|Size:|16.4 MB|

## References

[https://temu.bsc.es/livingner/](https://temu.bsc.es/livingner/)

## Benchmarking

```bash
label         precision  recall  f1-score  support 
B-HUMAN       0.99       0.99    0.99      3268    
B-SPECIES     0.98       0.98    0.98      3688    
I-HUMAN       0.94       0.97    0.95      297     
I-SPECIES     0.98       0.90    0.94      1720    
micro-avg     0.98       0.97    0.97      8973    
macro-avg     0.97       0.96    0.96      8973    
weighted-avg  0.98       0.97    0.97      8973 
```
