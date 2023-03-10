---
layout: model
title: Detect Clinical Entities (ner_eu_clinical_case - fr)
author: John Snow Labs
name: ner_eu_clinical_case
date: 2023-02-01
tags: [fr, clinical, licensed, ner]
task: Named Entity Recognition
language: fr
edition: Healthcare NLP 4.2.8
spark_version: 3.0
supported: true
annotator: MedicalNerModel
article_header:
  type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description

Pretrained named entity recognition (NER) deep learning model for extracting clinical entities from French texts. The SparkNLP deep learning model (MedicalNerModel) is inspired by a former state of the art model for NER: Chiu & Nichols, Named Entity Recognition with Bidirectional LSTM-CNN.

The corpus used for model training is provided by European Clinical Case Corpus (E3C), a project aimed at offering a freely available multilingual corpus of semantically annotated clinical narratives.

## Predicted Entities

`clinical_event`, `bodypart`, `clinical_condition`, `units_measurements`, `patient`, `date_time`

{:.btn-box}
<button class="button button-orange" disabled>Live Demo</button>
<button class="button button-orange" disabled>Open in Colab</button>
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/clinical/models/ner_eu_clinical_case_fr_4.2.8_3.0_1675293960896.zip){:.button.button-orange}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/clinical/models/ner_eu_clinical_case_fr_4.2.8_3.0_1675293960896.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use
 


<div class="tabs-box" markdown="1">
{% include programmingLanguageSelectScalaPythonNLU.html %}
```python
document_assembler = DocumentAssembler()\
	.setInputCol("text")\
	.setOutputCol("document")
 
sentenceDetectorDL = SentenceDetectorDLModel.pretrained("sentence_detector_dl", "xx")\
	.setInputCols(["document"])\
	.setOutputCol("sentence")

tokenizer = Tokenizer()\
	.setInputCols(["sentence"])\
	.setOutputCol("token")

word_embeddings = WordEmbeddingsModel.pretrained("w2v_cc_300d","fr")\
	.setInputCols(["sentence","token"])\
	.setOutputCol("embeddings")

ner = MedicalNerModel.pretrained('ner_eu_clinical_case', "fr", "clinical/models") \
	.setInputCols(["sentence", "token", "embeddings"]) \
	.setOutputCol("ner")
 
ner_converter = NerConverterInternal()\
	.setInputCols(["sentence", "token", "ner"])\
	.setOutputCol("ner_chunk")

pipeline = Pipeline(stages=[
	document_assembler,
	sentenceDetectorDL,
	tokenizer,
	word_embeddings,
	ner,
	ner_converter])

data = spark.createDataFrame([["""Un gar??on de 3 ans atteint d'un trouble autistique ?? l'h??pital du service p??diatrique A de l'h??pital universitaire. Il n'a pas d'ant??c??dents familiaux de troubles ou de maladies du spectre autistique. Le gar??on a ??t?? diagnostiqu?? avec un trouble de communication s??v??re, avec des difficult??s d'interaction sociale et un traitement sensoriel retard??. Les tests sanguins ??taient normaux (thyr??ostimuline (TSH), h??moglobine, volume globulaire moyen (MCV) et ferritine). L'endoscopie haute a ??galement montr?? une tumeur sous-muqueuse provoquant une obstruction subtotale de la sortie gastrique. Devant la suspicion d'une tumeur stromale gastro-intestinale, une gastrectomie distale a ??t?? r??alis??e. L'examen histopathologique a r??v??l?? une prolif??ration de cellules fusiformes dans la couche sous-muqueuse."""]]).toDF("text")

result = pipeline.fit(data).transform(data)
```
```scala
val documenter = new DocumentAssembler() 
    .setInputCol("text") 
    .setOutputCol("document")

val sentenceDetector = SentenceDetectorDLModel.pretrained("sentence_detector_dl", "xx")
  .setInputCols("document")
  .setOutputCol("sentence")

val tokenizer = new Tokenizer()
  .setInputCols("sentence")
  .setOutputCol("token")

val word_embeddings = WordEmbeddingsModel.pretrained("w2v_cc_300d","fr")
	.setInputCols(Array("sentence","token"))
	.setOutputCol("embeddings")

val ner_model = MedicalNerModel.pretrained("ner_eu_clinical_case", "fr", "clinical/models")
    .setInputCols(Array("sentence", "token", "embeddings"))
    .setOutputCol("ner")

val ner_converter = new NerConverterInternal()
    .setInputCols(Array("sentence", "token", "ner"))
    .setOutputCol("ner_chunk")

val pipeline = new Pipeline().setStages(Array(documenter, sentenceDetector, tokenizer, word_embeddings, ner_model, ner_converter))

val data = Seq(Array("""Un gar??on de 3 ans atteint d'un trouble autistique ?? l'h??pital du service p??diatrique A de l'h??pital universitaire. Il n'a pas d'ant??c??dents familiaux de troubles ou de maladies du spectre autistique. Le gar??on a ??t?? diagnostiqu?? avec un trouble de communication s??v??re, avec des difficult??s d'interaction sociale et un traitement sensoriel retard??. Les tests sanguins ??taient normaux (thyr??ostimuline (TSH), h??moglobine, volume globulaire moyen (MCV) et ferritine). L'endoscopie haute a ??galement montr?? une tumeur sous-muqueuse provoquant une obstruction subtotale de la sortie gastrique. Devant la suspicion d'une tumeur stromale gastro-intestinale, une gastrectomie distale a ??t?? r??alis??e. L'examen histopathologique a r??v??l?? une prolif??ration de cellules fusiformes dans la couche sous-muqueuse.""")).toDS().toDF("text")

val result = pipeline.fit(data).transform(data)
```
</div>

## Results

```bash
+-----------------------------------------------------+------------------+
|chunk                                                |ner_label         |
+-----------------------------------------------------+------------------+
|Un gar??on de 3 ans                                   |patient           |
|trouble autistique ?? l'h??pital du service p??diatrique|clinical_condition|
|l'h??pital                                            |clinical_event    |
|Il n'a                                               |patient           |
|d'ant??c??dents                                        |clinical_event    |
|troubles                                             |clinical_condition|
|maladies                                             |clinical_condition|
|du spectre autistique                                |bodypart          |
|Le gar??on                                            |patient           |
|diagnostiqu??                                         |clinical_event    |
|trouble                                              |clinical_condition|
|difficult??s                                          |clinical_event    |
|traitement                                           |clinical_event    |
|tests                                                |clinical_event    |
|normaux                                              |units_measurements|
|thyr??ostimuline                                      |clinical_event    |
|TSH                                                  |clinical_event    |
|ferritine                                            |clinical_event    |
|L'endoscopie                                         |clinical_event    |
|montr??                                               |clinical_event    |
|tumeur sous-muqueuse                                 |clinical_condition|
|provoquant                                           |clinical_event    |
|obstruction                                          |clinical_condition|
|la sortie gastrique                                  |bodypart          |
|suspicion                                            |clinical_event    |
|tumeur stromale gastro-intestinale                   |clinical_condition|
|gastrectomie                                         |clinical_event    |
|L'examen                                             |clinical_event    |
|r??v??l??                                               |clinical_event    |
|prolif??ration                                        |clinical_event    |
|cellules fusiformes                                  |bodypart          |
|la couche sous-muqueuse                              |bodypart          |
+-----------------------------------------------------+------------------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|ner_eu_clinical_case|
|Compatibility:|Healthcare NLP 4.2.8+|
|License:|Licensed|
|Edition:|Official|
|Input Labels:|[sentence, token, embeddings]|
|Output Labels:|[ner]|
|Language:|fr|
|Size:|895.0 KB|

## References

The corpus used for model training is provided by European Clinical Case Corpus (E3C), a project aimed at offering a freely available multilingual corpus of semantically annotated clinical narratives.

## Benchmarking

```bash
             label     tp     fp    fn  total  precision  recall      f1
         date_time   49.0   14.0  70.0  104.0     0.7778  0.7000  0.7368
units_measurements   92.0   19.0   6.0   48.0     0.8288  0.9388  0.8804
clinical_condition  178.0   74.0  73.0  120.0     0.7063  0.7092  0.7078
           patient  114.0    6.0  15.0   87.0     0.9500  0.8837  0.9157
    clinical_event  265.0   81.0  71.0  478.0     0.7659  0.7887  0.7771
          bodypart  243.0   34.0  64.0  166.0     0.8773  0.7915  0.8322
            macro     -      -      -     -         -       -     0.8083
            micro     -      -      -     -         -       -     0.7978
```
