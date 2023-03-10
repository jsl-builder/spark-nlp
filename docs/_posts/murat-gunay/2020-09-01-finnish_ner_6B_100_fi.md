---
layout: model
title: Named Entity Recognition (NER) Model in Finnish (GloVe 6B 100)
author: John Snow Labs
name: finnish_ner_6B_100
date: 2020-09-01
task: Named Entity Recognition
language: fi
edition: Spark NLP 2.6.0
spark_version: 2.4
tags: [ner, fi, open_source]
supported: true
annotator: NerDLModel
article_header:
type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description
Finnish NER is a Named Entity Recognition (or NER) model, meaning it annotates text to find features like the names of people, places, and organizations. This NER model does not read words directly but instead reads word embeddings, which represent words as points such that more semantically similar words are closer together. The model is trained with GloVe 6B 100 word embeddings, so be sure to use the same embeddings in the pipeline.

{:.h2_title}
## Predicted Entities 
Persons-`PER`, Locations-`LOC`, Organizations-`ORG`, Product-`PRO`, Date-`DATE`, Event-`EVENT`.


{:.btn-box}
[Live Demo](https://demo.johnsnowlabs.com/public/PP_EXPLAIN_DOCUMENT_FI/){:.button.button-orange}{:target="_blank"}
[Open in Colab](https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/PP_EXPLAIN_DOCUMENT.ipynb){:.button.button-orange.button-orange-trans.co.button-icon}{:target="_blank"}
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/public/models/finnish_ner_6B_100_fi_2.6.0_2.4_1598965807300.zip){:.button.button-orange.button-orange-trans.arr.button-icon}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/public/models/finnish_ner_6B_100_fi_2.6.0_2.4_1598965807300.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use 

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPythonNLU.html %}

```python
...
embeddings = WordEmbeddingsModel.pretrained("glove_100d") \
.setInputCols(["document", "token"]) \
.setOutputCol("embeddings")
ner_model = NerDLModel.pretrained("finnish_ner_6B_100", "fi") \
.setInputCols(["document", "token", "embeddings"]) \
.setOutputCol("ner")
...        
nlp_pipeline = Pipeline(stages=[document_assembler, sentence_detector, tokenizer, embeddings, ner_model, ner_converter])
pipeline_model = nlp_pipeline.fit(spark.createDataFrame([['']]).toDF('text'))

result = pipeline_model.transform(spark.createDataFrame([["William Henry Gates III (28. lokakuuta 1955) on amerikkalaisia \u200b\u200bulkoministeri??it??, ohjelmistoja, sijoittajia ja filantroppeja. Microsoft on toiminut Microsoft Corporationin v??litt??j??n??. I l??bet af sin karriere hos Microsoft havde Gates stillinger som formand, administrerende direkt??r (administratorerende??????r), pr??sident og chefsoftwarearkitekt, samtidig med at han var den st??rste individualelle aktion??r indtil maj 2014. mikrotietokonevoluutioille i 1970'erne 1980 1980erne. F??dt and opvokset i Seattle, Washington, var Gates grundl??gger af Microsoft sammen med barndomsvennen Paul Allen i 1975 i Albuquerque, New Mexico; Det fortsatte med at blive verdens st??rste virksomhed inden for personlig tietokoneohjelmistot. Gates f??rte virksomheden som formand and administratorer direkt??r, indtil han tr??dte tilbage som administrerende direkt??r tammikuu 2000, miehet han forblev formand blev chefsoftwarearkitekt. Olen slutningen 1990'erne var Gates blevet kritiseret for syn forretningstaktik, der er blevet betragtet som konkurrencebegr??nsende. Denne udtalelse er blevet opretholdt ved adskillige retsafg??relser. Kes??kuun 2006 Meddelte Gates, at han ville overg?? til en deltidsrolle i Microsoft og fuldtidsarbejde i Bill & Melinda Gates Foundation, det private velg??rende fundament, som han og hans kone, Melinda Gates, oprettede i 2000. Han overf??rte gradvist sine pligter Tilaaja Ray Ozzie ja Craig Mundie. Han tr??dte tilbage som formand for Microsoft helmikuussa 2014 ja tiltr??dte en ny stilling som teknologiatietojen antaja at st??tte den nyudn??vnte adminerende direkt??r Satya Nadella."]], ["text"]))
```

```scala
...
val embeddings = WordEmbeddingsModel.pretrained("glove_100d")
.setInputCols(Array("document", "token"))
.setOutputCol("embeddings")
val ner_model = NerDLModel.pretrained("finnish_ner_6B_100", "fi")
.setInputCols(Array("document", "token", "embeddings"))
.setOutputCol("ner")
...
val pipeline = new Pipeline().setStages(Array(document_assembler, sentence_detector, tokenizer, embeddings, ner_model, ner_converter))

val data = Seq("William Henry Gates III (28. lokakuuta 1955) on amerikkalaisia ??????ulkoministeri??it??, ohjelmistoja, sijoittajia ja filantroppeja. Microsoft on toiminut Microsoft Corporationin v??litt??j??n??. I l??bet af sin karriere hos Microsoft havde Gates stillinger som formand, administrerende direkt??r (administratorerende??????r), pr??sident og chefsoftwarearkitekt, samtidig med at han var den st??rste individualelle aktion??r indtil maj 2014. mikrotietokonevoluutioille i 1970"erne 1980 1980erne. F??dt and opvokset i Seattle, Washington, var Gates grundl??gger af Microsoft sammen med barndomsvennen Paul Allen i 1975 i Albuquerque, New Mexico; Det fortsatte med at blive verdens st??rste virksomhed inden for personlig tietokoneohjelmistot. Gates f??rte virksomheden som formand and administratorer direkt??r, indtil han tr??dte tilbage som administrerende direkt??r tammikuu 2000, miehet han forblev formand blev chefsoftwarearkitekt. Olen slutningen 1990"erne var Gates blevet kritiseret for syn forretningstaktik, der er blevet betragtet som konkurrencebegr??nsende. Denne udtalelse er blevet opretholdt ved adskillige retsafg??relser. Kes??kuun 2006 Meddelte Gates, at han ville overg?? til en deltidsrolle i Microsoft og fuldtidsarbejde i Bill & Melinda Gates Foundation, det private velg??rende fundament, som han og hans kone, Melinda Gates, oprettede i 2000. [9] Han overf??rte gradvist sine pligter Tilaaja Ray Ozzie ja Craig Mundie. Han tr??dte tilbage som formand for Microsoft helmikuussa 2014 ja tiltr??dte en ny stilling som teknologiatietojen antaja at st??tte den nyudn??vnte adminerende direkt??r Satya Nadella.").toDF("text")
val result = pipeline.fit(data).transform(data)
```

{:.nlu-block}
```python
import nlu
text = ["""William Henry Gates III (28. lokakuuta 1955) on amerikkalaisia ??????ulkoministeri??it??, ohjelmistoja, sijoittajia ja filantroppeja. Microsoft on toiminut Microsoft Corporationin v??litt??j??n??. I l??bet af sin karriere hos Microsoft havde Gates stillinger som formand, administrerende direkt??r (administratorerende??????r), pr??sident og chefsoftwarearkitekt, samtidig med at han var den st??rste individualelle aktion??r indtil maj 2014. mikrotietokonevoluutioille i 1970'erne 1980 1980erne. F??dt and opvokset i Seattle, Washington, var Gates grundl??gger af Microsoft sammen med barndomsvennen Paul Allen i 1975 i Albuquerque, New Mexico; Det fortsatte med at blive verdens st??rste virksomhed inden for personlig tietokoneohjelmistot. Gates f??rte virksomheden som formand and administratorer direkt??r, indtil han tr??dte tilbage som administrerende direkt??r tammikuu 2000, miehet han forblev formand blev chefsoftwarearkitekt. Olen slutningen 1990'erne var Gates blevet kritiseret for syn forretningstaktik, der er blevet betragtet som konkurrencebegr??nsende. Denne udtalelse er blevet opretholdt ved adskillige retsafg??relser. Kes??kuun 2006 Meddelte Gates, at han ville overg?? til en deltidsrolle i Microsoft og fuldtidsarbejde i Bill & Melinda Gates Foundation, det private velg??rende fundament, som han og hans kone, Melinda Gates, oprettede i 2000. [9] Han overf??rte gradvist sine pligter Tilaaja Ray Ozzie ja Craig Mundie. Han tr??dte tilbage som formand for Microsoft helmikuussa 2014 ja tiltr??dte en ny stilling som teknologiatietojen antaja at st??tte den nyudn??vnte adminerende direkt??r Satya Nadella."""]

ner_df = nlu.load('fi.ner.6B_100d').predict(text, output_level = "chunk")
ner_df[["entities", "entities_confidence"]]
```
</div>

{:.h2_title}
## Results

```bash
+-----------------------+---------+
|chunk                  |ner_label|
+-----------------------+---------+
|William Henry Gates III|PER      |
|lokakuuta 1955         |DATE     |
|??????ulkoministeri??it??    |ORG      |
|Microsoft              |ORG      |
|Microsoft Corporationin|ORG      |
|Microsoft              |ORG      |
|Gates                  |PER      |
|2014                   |DATE     |
|Seattle                |LOC      |
|Washington             |LOC      |
|Gates                  |PER      |
|Microsoft              |ORG      |
|Paul Allen             |PER      |
|Albuquerque            |ORG      |
|New Mexico             |ORG      |
|Det                    |ORG      |
|verdens                |ORG      |
|Gates                  |PER      |
|tammikuu 2000          |DATE     |
|Gates                  |PER      |
+-----------------------+---------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|finnish_ner_6B_100|
|Type:|ner|
|Compatibility:| Spark NLP 2.6.0+|
|Edition:|Official|
|License:|Open Source|
|Input Labels:|[sentence, token, embeddings]|
|Output Labels:|[ner]|
|Language:|fi|
|Case sensitive:|false|

{:.h2_title}
## Data Source
The detailed information can be found from [https://www.aclweb.org/anthology/2020.lrec-1.567.pdf](https://www.aclweb.org/anthology/2020.lrec-1.567.pdf)