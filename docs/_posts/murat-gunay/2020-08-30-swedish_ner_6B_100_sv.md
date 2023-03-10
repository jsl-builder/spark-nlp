---
layout: model
title: Named Entity Recognition (NER) Model in Swedish (GloVe 6B 100)
author: John Snow Labs
name: swedish_ner_6B_100
date: 2020-08-30
task: Named Entity Recognition
language: sv
edition: Spark NLP 2.6.0
spark_version: 2.4
tags: [ner, sv, open_source]
supported: true
annotator: NerDLModel
article_header:
type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description
Swedish NER is a Named Entity Recognition (or NER) model, meaning it annotates text to find features like the names of people, places, and organizations. This NER model does not read words directly but instead reads word embeddings, which represent words as points such that more semantically similar words are closer together. The model is trained with GloVe 6B 100 word embeddings, so be sure to use the same embeddings in the pipeline.

{:.h2_title}
## Predicted Entities 
Persons-`PER`, Locations-`LOC`, Organizations-`ORG`, Product-`PRO`, Date-`DATE`, Event-`EVENT`.


{:.btn-box}
[Live Demo](https://demo.johnsnowlabs.com/public/NER_SV/){:.button.button-orange}{:target="_blank"}
[Open in Colab](https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/NER.ipynb){:.button.button-orange.button-orange-trans.co.button-icon}{:target="_blank"}
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/public/models/swedish_ner_6B_100_sv_2.6.0_2.4_1598810268071.zip){:.button.button-orange.button-orange-trans.arr.button-icon}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/public/models/swedish_ner_6B_100_sv_2.6.0_2.4_1598810268071.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use 

<div class="tabs-box" markdown="1">

{% include programmingLanguageSelectScalaPythonNLU.html %}

```python
...
embeddings = WordEmbeddingsModel.pretrained("glove_100d") \
.setInputCols(["document", "token"]) \
.setOutputCol("embeddings")
ner_model = NerDLModel.pretrained("swedish_ner_6B_100", "sv") \
.setInputCols(["document", "token", "embeddings"]) \
.setOutputCol("ner")
...        
nlp_pipeline = Pipeline(stages=[document_assembler, sentence_detector, tokenizer, embeddings, ner_model, ner_converter])
pipeline_model = nlp_pipeline.fit(spark.createDataFrame([['']]).toDF('text'))

result = pipeline_model.transform(spark.createDataFrame([['William Henry Gates III (f??dd 28 oktober 1955) ??r en amerikansk aff??rsmagnat, mjukvaruutvecklare, investerare och filantrop. Han ??r mest k??nd som medgrundare av Microsoft Corporation. Under sin karri??r p?? Microsoft innehade Gates befattningar som styrelseordf??rande, verkst??llande direkt??r (VD), VD och programvaruarkitekt samtidigt som han var den st??rsta enskilda aktie??garen fram till maj 2014. Han ??r en av de mest k??nda f??retagarna och pionj??rerna inom mikrodatorrevolutionen p?? 1970- och 1980-talet. F??dd och uppvuxen i Seattle, Washington, grundade Gates Microsoft tillsammans med barndomsv??n Paul Allen 1975 i Albuquerque, New Mexico; det blev vidare v??rldens st??rsta datorprogramf??retag. Gates ledde f??retaget som styrelseordf??rande och VD tills han avgick som VD i januari 2000, men han f??rblev ordf??rande och blev chef f??r programvaruarkitekt. Under slutet av 1990-talet hade Gates kritiserats f??r sin aff??rstaktik, som har ansetts konkurrensbegr??nsande. Detta yttrande har uppr??tth??llits genom m??nga domstolsbeslut. I juni 2006 meddelade Gates att han skulle g?? ??ver till en deltidsroll p?? Microsoft och heltid p?? Bill & Melinda Gates Foundation, den privata v??lg??renhetsstiftelsen som han och hans fru, Melinda Gates, grundade 2000. Han ??verf??rde gradvis sina uppgifter till Ray Ozzie och Craig Mundie. Han avgick som styrelseordf??rande i Microsoft i februari 2014 och tilltr??dde en ny tj??nst som teknologr??dgivare f??r att st??dja den nyutn??mnda VD Satya Nadella.']], ["text"]))
```

```scala
...
val embeddings = WordEmbeddingsModel.pretrained("glove_100d")
.setInputCols(Array("document", "token"))
.setOutputCol("embeddings")
val ner_model = NerDLModel.pretrained("swedish_ner_6B_100", "sv")
.setInputCols(Array("document", "token", "embeddings"))
.setOutputCol("ner")
...
val pipeline = new Pipeline().setStages(Array(document_assembler, sentence_detector, tokenizer, embeddings, ner_model, ner_converter))

val data = Seq("William Henry Gates III (f??dd 28 oktober 1955) ??r en amerikansk aff??rsmagnat, mjukvaruutvecklare, investerare och filantrop. Han ??r mest k??nd som medgrundare av Microsoft Corporation. Under sin karri??r p?? Microsoft innehade Gates befattningar som styrelseordf??rande, verkst??llande direkt??r (VD), VD och programvaruarkitekt samtidigt som han var den st??rsta enskilda aktie??garen fram till maj 2014. Han ??r en av de mest k??nda f??retagarna och pionj??rerna inom mikrodatorrevolutionen p?? 1970- och 1980-talet. F??dd och uppvuxen i Seattle, Washington, grundade Gates Microsoft tillsammans med barndomsv??n Paul Allen 1975 i Albuquerque, New Mexico; det blev vidare v??rldens st??rsta datorprogramf??retag. Gates ledde f??retaget som styrelseordf??rande och VD tills han avgick som VD i januari 2000, men han f??rblev ordf??rande och blev chef f??r programvaruarkitekt. Under slutet av 1990-talet hade Gates kritiserats f??r sin aff??rstaktik, som har ansetts konkurrensbegr??nsande. Detta yttrande har uppr??tth??llits genom m??nga domstolsbeslut. I juni 2006 meddelade Gates att han skulle g?? ??ver till en deltidsroll p?? Microsoft och heltid p?? Bill & Melinda Gates Foundation, den privata v??lg??renhetsstiftelsen som han och hans fru, Melinda Gates, grundade 2000. Han ??verf??rde gradvis sina uppgifter till Ray Ozzie och Craig Mundie. Han avgick som styrelseordf??rande i Microsoft i februari 2014 och tilltr??dde en ny tj??nst som teknologr??dgivare f??r att st??dja den nyutn??mnda VD Satya Nadella.").toDF("text")
val result = pipeline.fit(data).transform(data)
```

{:.nlu-block}
```python
import nlu
text = ["""William Henry Gates III (f??dd 28 oktober 1955) ??r en amerikansk aff??rsmagnat, mjukvaruutvecklare, investerare och filantrop. Han ??r mest k??nd som medgrundare av Microsoft Corporation. Under sin karri??r p?? Microsoft innehade Gates befattningar som styrelseordf??rande, verkst??llande direkt??r (VD), VD och programvaruarkitekt samtidigt som han var den st??rsta enskilda aktie??garen fram till maj 2014. Han ??r en av de mest k??nda f??retagarna och pionj??rerna inom mikrodatorrevolutionen p?? 1970- och 1980-talet. F??dd och uppvuxen i Seattle, Washington, grundade Gates Microsoft tillsammans med barndomsv??n Paul Allen 1975 i Albuquerque, New Mexico; det blev vidare v??rldens st??rsta datorprogramf??retag. Gates ledde f??retaget som styrelseordf??rande och VD tills han avgick som VD i januari 2000, men han f??rblev ordf??rande och blev chef f??r programvaruarkitekt. Under slutet av 1990-talet hade Gates kritiserats f??r sin aff??rstaktik, som har ansetts konkurrensbegr??nsande. Detta yttrande har uppr??tth??llits genom m??nga domstolsbeslut. I juni 2006 meddelade Gates att han skulle g?? ??ver till en deltidsroll p?? Microsoft och heltid p?? Bill & Melinda Gates Foundation, den privata v??lg??renhetsstiftelsen som han och hans fru, Melinda Gates, grundade 2000. Han ??verf??rde gradvis sina uppgifter till Ray Ozzie och Craig Mundie. Han avgick som styrelseordf??rande i Microsoft i februari 2014 och tilltr??dde en ny tj??nst som teknologr??dgivare f??r att st??dja den nyutn??mnda VD Satya Nadella."""]

ner_df = nlu.load('sv.ner.6B_100').predict(text, output_level = "chunk")
ner_df[["entities", "entities_confidence"]]
```
</div>

{:.h2_title}
## Results

```bash
+---------------------+---------+
|chunk                |ner_label|
+---------------------+---------+
|William Henry Gates  |PER      |
|Microsoft Corporation|ORG      |
|Microsoft            |ORG      |
|Gates                |PER      |
|VD                   |MISC     |
|Seattle              |LOC      |
|Washington           |LOC      |
|Gates Microsoft      |PER      |
|Paul Allen           |PER      |
|Albuquerque          |LOC      |
|New Mexico           |ORG      |
|Gates                |PER      |
|Gates                |PER      |
|Microsoft            |ORG      |
|Melinda Gates        |PER      |
|Melinda Gates        |PER      |
|Ray Ozzie            |PER      |
|Craig Mundie         |PER      |
|Microsoft            |ORG      |
|VD Satya Nadella     |MISC     |
+---------------------+---------+
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|swedish_ner_6B_100|
|Type:|ner|
|Compatibility:| Spark NLP 2.6.0+|
|Edition:|Official|
|License:|Open Source|
|Input Labels:|[sentence, token, embeddings]|
|Output Labels:|[ner]|
|Language:|sv|
|Case sensitive:|false|

{:.h2_title}
## Data Source
Trained on a custom dataset with multi-lingual GloVe Embeddings ``glove_6B_100``.