---
layout: model
title: Detect Diagnoses and Procedures (Spanish)
author: John Snow Labs
name: ner_diag_proc
date: 2021-03-31
tags: [ner, clinical, licensed, es]
task: Named Entity Recognition
language: es
edition: Healthcare NLP 3.0.0
spark_version: 3.0
supported: true
annotator: MedicalNerModel
article_header:
type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description

Named Entity recognition annotator allows for a generic model to be trained by utilizing a deep learning algorithm (Char CNNs - BiLSTM - CRF - word embeddings) inspired on a former state of the art model for NER: Chiu & Nicols, Named Entity Recognition with Bidirectional LSTM,CNN.Pretrained named entity recognition deep learning model for diagnostics and procedures in spanish

## Predicted Entities

``DIAGNOSTICO``, ``PROCEDIMIENTO``.

{:.btn-box}
[Live Demo](https://demo.johnsnowlabs.com/healthcare/NER_DIAG_PROC_ES/){:.button.button-orange}
[Open in Colab](https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_DIAG_PROC_ES.ipynb){:.button.button-orange.button-orange-trans.co.button-icon}
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/clinical/models/ner_diag_proc_es_3.0.0_3.0_1617208422892.zip){:.button.button-orange.button-orange-trans.arr.button-icon.hidden}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/clinical/models/ner_diag_proc_es_3.0.0_3.0_1617208422892.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use



<div class="tabs-box" markdown="1">
{% include programmingLanguageSelectScalaPythonNLU.html %}

```python
document_assembler = DocumentAssembler()\
    .setInputCol("text")\
    .setOutputCol("document")
         
sentence_detector = SentenceDetector()\
    .setInputCols(["document"])\
    .setOutputCol("sentence")

tokenizer = Tokenizer()\
    .setInputCols(["sentence"])\
    .setOutputCol("token")

embed = WordEmbeddingsModel.pretrained("embeddings_scielowiki_300d","es","clinical/models")\
	.setInputCols(["document","token"])\
	.setOutputCol("word_embeddings")

model = MedicalNerModel.pretrained("ner_diag_proc","es","clinical/models")\
	.setInputCols("sentence","token","word_embeddings")\
	.setOutputCol("ner")

ner_converter = NerConverter()\
 	.setInputCols(["sentence", "token", "ner"])\
 	.setOutputCol("ner_chunk")

nlpPipeline = Pipeline(stages=[document_assembler, sentence_detector, tokenizer, embed, model, ner_converter])

model = nlpPipeline.fit(spark.createDataFrame([[""]]).toDF("text"))

results = model.transform(spark.createDataFrame([['HISTORIA DE ENFERMEDAD ACTUAL: El Sr. Smith es un hombre blanco veterano de 60 a??os con m??ltiples comorbilidades, que tiene antecedentes de c??ncer de vejiga diagnosticado hace aproximadamente dos a??os por el Hospital VA. All?? se someti?? a una resecci??n. Deb??a ser ingresado en el Hospital de D??a para una cistectom??a. Fue visto en la Cl??nica de Urolog??a y Cl??nica de Radiolog??a el 02/04/2003. CURSO DE HOSPITAL: El Sr. Smith se present?? en el Hospital de D??a antes de la cirug??a de Urolog??a. En evaluaci??n, EKG, ecocardiograma fue anormal, se obtuvo una consulta de Cardiolog??a. Luego se procedi?? a una resonancia magn??tica de estr??s con adenosina card??aca, la misma fue positiva para isquemia inducible, infarto subendoc??rdico inferolateral leve a moderado con isquemia peri-infarto. Adem??s, se observa isquemia inducible en el tabique lateral inferior. El Sr. Smith se someti?? a un cateterismo del coraz??n izquierdo, que revel?? una enfermedad de las arterias coronarias de dos vasos. La RCA, proximal estaba estenosada en un 95% y la distal en un 80% estenosada. La LAD media estaba estenosada en un 85% y la LAD distal estaba estenosada en un 85%. Se colocaron cuatro stents de metal desnudo Multi-Link Vision para disminuir las cuatro lesiones al 0%. Despu??s de la intervenci??n, el Sr. Smith fue admitido en 7 Ardmore Tower bajo el Servicio de Cardiolog??a bajo la direcci??n del Dr. Hart. El Sr. Smith tuvo un curso hospitalario post-intervenci??n sin complicaciones. Se mantuvo estable para el alta hospitalaria el 07/02/2003 con instrucciones de tomar Plavix diariamente durante un mes y Urolog??a est?? al tanto de lo mismo.']], ["text"]))
```
```scala
val document_assembler = new DocumentAssembler()
    .setInputCol("text")
    .setOutputCol("document")
         
val sentence_detector = new SentenceDetector()
    .setInputCols("document")
    .setOutputCol("sentence")

val tokenizer = new Tokenizer()
    .setInputCols("sentence")
    .setOutputCol("token")

val embed = WordEmbeddingsModel.pretrained("embeddings_scielowiki_300d","es","clinical/models")
	.setInputCols(Array("document","token"))
	.setOutputCol("word_embeddings")

val model = MedicalNerModel.pretrained("ner_diag_proc","es","clinical/models")
	.setInputCols(Array("sentence","token","word_embeddings"))
	.setOutputCol("ner")

val ner_converter = new NerConverter()
 	.setInputCols(Array("sentence", "token", "ner"))
 	.setOutputCol("ner_chunk")

val pipeline = new Pipeline().setStages(Array(document_assembler, sentence_detector, tokenizer, embed, model, ner_converter))

val data = Seq("""HISTORIA DE ENFERMEDAD ACTUAL: El Sr. Smith es un hombre blanco veterano de 60 a??os con m??ltiples comorbilidades, que tiene antecedentes de c??ncer de vejiga diagnosticado hace aproximadamente dos a??os por el Hospital VA. All?? se someti?? a una resecci??n. Deb??a ser ingresado en el Hospital de D??a para una cistectom??a. Fue visto en la Cl??nica de Urolog??a y Cl??nica de Radiolog??a el 02/04/2003. CURSO DE HOSPITAL: El Sr. Smith se present?? en el Hospital de D??a antes de la cirug??a de Urolog??a. En evaluaci??n, EKG, ecocardiograma fue anormal, se obtuvo una consulta de Cardiolog??a. Luego se procedi?? a una resonancia magn??tica de estr??s con adenosina card??aca, la misma fue positiva para isquemia inducible, infarto subendoc??rdico inferolateral leve a moderado con isquemia peri-infarto. Adem??s, se observa isquemia inducible en el tabique lateral inferior. El Sr. Smith se someti?? a un cateterismo del coraz??n izquierdo, que revel?? una enfermedad de las arterias coronarias de dos vasos. La RCA, proximal estaba estenosada en un 95% y la distal en un 80% estenosada. La LAD media estaba estenosada en un 85% y la LAD distal estaba estenosada en un 85%. Se colocaron cuatro stents de metal desnudo Multi-Link Vision para disminuir las cuatro lesiones al 0%. Despu??s de la intervenci??n, el Sr. Smith fue admitido en 7 Ardmore Tower bajo el Servicio de Cardiolog??a bajo la direcci??n del Dr. Hart. El Sr. Smith tuvo un curso hospitalario post-intervenci??n sin complicaciones. Se mantuvo estable para el alta hospitalaria el 07/02/2003 con instrucciones de tomar Plavix diariamente durante un mes y Urolog??a est?? al tanto de lo mismo.""").toDS().toDF("text")

val result = pipeline.fit(data).transform(data)
```


{:.nlu-block}
```python
import nlu
nlu.load("es.med_ner").predict("""HISTORIA DE ENFERMEDAD ACTUAL: El Sr. Smith es un hombre blanco veterano de 60 a??os con m??ltiples comorbilidades, que tiene antecedentes de c??ncer de vejiga diagnosticado hace aproximadamente dos a??os por el Hospital VA. All?? se someti?? a una resecci??n. Deb??a ser ingresado en el Hospital de D??a para una cistectom??a. Fue visto en la Cl??nica de Urolog??a y Cl??nica de Radiolog??a el 02/04/2003. CURSO DE HOSPITAL: El Sr. Smith se present?? en el Hospital de D??a antes de la cirug??a de Urolog??a. En evaluaci??n, EKG, ecocardiograma fue anormal, se obtuvo una consulta de Cardiolog??a. Luego se procedi?? a una resonancia magn??tica de estr??s con adenosina card??aca, la misma fue positiva para isquemia inducible, infarto subendoc??rdico inferolateral leve a moderado con isquemia peri-infarto. Adem??s, se observa isquemia inducible en el tabique lateral inferior. El Sr. Smith se someti?? a un cateterismo del coraz??n izquierdo, que revel?? una enfermedad de las arterias coronarias de dos vasos. La RCA, proximal estaba estenosada en un 95% y la distal en un 80% estenosada. La LAD media estaba estenosada en un 85% y la LAD distal estaba estenosada en un 85%. Se colocaron cuatro stents de metal desnudo Multi-Link Vision para disminuir las cuatro lesiones al 0%. Despu??s de la intervenci??n, el Sr. Smith fue admitido en 7 Ardmore Tower bajo el Servicio de Cardiolog??a bajo la direcci??n del Dr. Hart. El Sr. Smith tuvo un curso hospitalario post-intervenci??n sin complicaciones. Se mantuvo estable para el alta hospitalaria el 07/02/2003 con instrucciones de tomar Plavix diariamente durante un mes y Urolog??a est?? al tanto de lo mismo.""")
```

</div>

## Results

```bash
+----------------------+-------------+
|chunk                 |ner_label    |
+----------------------+-------------+
|ENFERMEDAD            |DIAGNOSTICO  |
|c??ncer de vejiga      |DIAGNOSTICO  |
|resecci??n             |PROCEDIMIENTO|
|cistectom??a           |PROCEDIMIENTO|
|estr??s                |DIAGNOSTICO  |
|infarto subendoc??rdico|DIAGNOSTICO  |
|enfermedad            |DIAGNOSTICO  |
|arterias coronarias   |DIAGNOSTICO  |
+----------------------+-------------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|ner_diag_proc|
|Compatibility:|Healthcare NLP 3.0.0+|
|License:|Licensed|
|Edition:|Official|
|Input Labels:|[sentence, token, embeddings]|
|Output Labels:|[ner]|
|Language:|es|



## Benchmarking
```bash
+-------------+------+------+------+------+---------+------+------+
|       entity|    tp|    fp|    fn| total|precision|recall|    f1|
+-------------+------+------+------+------+---------+------+------+
|PROCEDIMIENTO|2299.0|1103.0| 860.0|3159.0|   0.6758|0.7278|0.7008|
|  DIAGNOSTICO|6623.0|1364.0|2974.0|9597.0|   0.8292|0.6901|0.7533|
+-------------+------+------+------+------+---------+------+------+

+------------------+
|             macro|
+------------------+
|0.7270531284138397|
+------------------+


+------------------+
|             micro|
+------------------+
|0.7402992400932049|
+------------------+
```