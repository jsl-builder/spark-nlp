---
layout: model
title: Clinical Deidentification (Spanish, augmented)
author: John Snow Labs
name: clinical_deidentification_augmented
date: 2022-03-03
tags: [deid, es, licensed]
task: De-identification
language: es
edition: Healthcare NLP 3.4.1
spark_version: 2.4
supported: true
recommended: true
annotator: PipelineModel
article_header:
  type: cover
use_language_switcher: "Python-Scala-Java"
---

## Description

This pipeline is trained with sciwiki_300d embeddings and can be used to deidentify PHI information from medical texts in Spanish. It differs from the previous `clinical_deidentificaiton` pipeline in that it includes the `ner_deid_subentity_augmented` NER model and some improvements in ContextualParsers and RegexMatchers.

The PHI information will be masked and obfuscated in the resulting text. The pipeline can mask, fake or obfuscate the following entities: `AGE`, `DATE`, `PROFESSION`, `EMAIL`, `USERNAME`, `STREET`, `COUNTRY`, `CITY`, `DOCTOR`, `HOSPITAL`, `PATIENT`, `URL`, `MEDICALRECORD`, `IDNUM`, `ORGANIZATION`, `PHONE`, `ZIP`, `ACCOUNT`, `SSN`, `PLATE`, `SEX` and `IPADDR`

{:.btn-box}
[Live Demo](https://demo.johnsnowlabs.com/healthcare/DEID_PHI_TEXT_MULTI/){:.button.button-orange}
[Open in Colab](https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/DEID_PHI_TEXT_MULTI.ipynb){:.button.button-orange.button-orange-trans.co.button-icon}
[Download](https://s3.amazonaws.com/auxdata.johnsnowlabs.com/clinical/models/clinical_deidentification_augmented_es_3.4.1_2.4_1646331074905.zip){:.button.button-orange.button-orange-trans.arr.button-icon.hidden}
[Copy S3 URI](s3://auxdata.johnsnowlabs.com/clinical/models/clinical_deidentification_augmented_es_3.4.1_2.4_1646331074905.zip){:.button.button-orange.button-orange-trans.button-icon.button-copy-s3}

## How to use



<div class="tabs-box" markdown="1">
{% include programmingLanguageSelectScalaPythonNLU.html %}
```python
from johnsnowlabs import *

deid_pipeline = PretrainedPipeline("clinical_deidentification_augmented", "es", "clinical/models")

sample = """Datos del paciente.
Nombre:  Jose .
Apellidos: Aranda Martinez.
NHC: 2748903.
NASS: 26 37482910 04.
Domicilio: Calle Losada Mart?? 23, 5 B..
Localidad/ Provincia: Madrid.
CP: 28016.
Datos asistenciales.
Fecha de nacimiento: 15/04/1977.
Pa??s: Espa??a.
Edad: 37 a??os Sexo: F.
Fecha de Ingreso: 05/06/2018.
M??dico: Mar??a Merino Viveros  N??Col: 28 28 35489.
Informe cl??nico del paciente: var??n de 37 a??os con vida previa activa que refiere dolores osteoarticulares de localizaci??n variable en el ??ltimo mes y fiebre en la ??ltima semana con picos (matutino y vespertino) de 40 C las ??ltimas 24-48 horas, por lo que acude al Servicio de Urgencias. Antes de comenzar el cuadro estuvo en Extremadura en una regi??n end??mica de brucella, ingiriendo leche de cabra sin pasteurizar y queso de dicho ganado. Entre los comensales aparecieron varios casos de brucelosis. Durante el ingreso para estudio del s??ndrome febril con antecedentes epidemiol??gicos de posible exposici??n a Brucella presenta un cuadro de orquiepididimitis derecha.
La exploraci??n f??sica revela: T?? 40,2 C; T.A: 109/68 mmHg; Fc: 105 lpm. Se encuentra consciente, orientado, sudoroso, eupneico, con buen estado de nutrici??n e hidrataci??n. En cabeza y cuello no se palpan adenopat??as, ni bocio ni ingurgitaci??n de vena yugular, con pulsos carot??deos sim??tricos. Auscultaci??n card??aca r??tmica, sin soplos, roces ni extratonos. Auscultaci??n pulmonar con conservaci??n del murmullo vesicular. Abdomen blando, depresible, sin masas ni megalias. En la exploraci??n neurol??gica no se detectan signos men??ngeos ni datos de focalidad. Extremidades sin varices ni edemas. Pulsos perif??ricos presentes y sim??tricos. En la exploraci??n urol??gica se aprecia el teste derecho aumentado de tama??o, no adherido a piel, con zonas de fluctuaci??n e intensamente doloroso a la palpaci??n, con p??rdida del l??mite epid??dimo-testicular y transiluminaci??n positiva.
Los datos anal??ticos muestran los siguentes resultados: Hemograma: Hb 13,7 g/dl; leucocitos 14.610/mm3 (neutr??filos 77%); plaquetas 206.000/ mm3. VSG: 40 mm 1?? hora. Coagulaci??n: TQ 87%; TTPA 25,8 seg. Bioqu??mica: Glucosa 117 mg/dl; urea 29 mg/dl; creatinina 0,9 mg/dl; sodio 136 mEq/l; potasio 3,6 mEq/l; GOT 11 U/l; GPT 24 U/l; GGT 34 U/l; fosfatasa alcalina 136 U/l; calcio 8,3 mg/dl. Orina: sedimento normal.
Durante el ingreso se solicitan Hemocultivos: positivo para Brucella y Serolog??as espec??ficas para Brucella: Rosa de Bengala +++; Test de Coombs > 1/1280; Brucellacapt > 1/5120. Las pruebas de imagen solicitadas ( Rx t??rax, Ecograf??a abdominal, TAC craneal, Ecocardiograma transtor??cico) no evidencian patolog??a significativa, excepto la Ecograf??a testicular, que muestra engrosamiento de la bolsa escrotal con peque??a cantidad de l??quido con septos y test??culo aumentado de tama??o con peque??as zonas hipoecoicas en su interior que pueden representar microabscesos.
Con el diagn??stico de orquiepididimitis secundaria a Brucella se instaura tratamiento sintom??tico (antit??rmicos, antiinflamatorios, reposo y elevaci??n testicular) as?? como tratamiento antibi??tico espec??fico: Doxiciclina 100 mg v??a oral cada 12 horas (durante 6 semanas) y Estreptomicina 1 gramo intramuscular cada 24 horas (durante 3 semanas). El paciente mejora significativamente de su cuadro tras una semana de ingreso, decidi??ndose el alta a su domicilio donde complet?? la pauta de tratamiento antibi??tico. En revisiones sucesivas en consultas se constat?? la completa remisi??n del cuadro.
Remitido por: Dra. Mar??a Merino Viveros Hospital Universitario de Getafe Servicio de Endocrinolog??a y Nutrici??n Carretera de Toledo km 12,500 28905 Getafe - Madrid (Espa??a) Correo electr??nico: marietta84@hotmail.com"""

result = deid_pipeline .annotate(sample)
```
```scala
import com.johnsnowlabs.nlp.pretrained.PretrainedPipeline
val deid_pipeline = new PretrainedPipeline("clinical_deidentification_augmented", "es", "clinical/models")

sample = "Datos del paciente.
Nombre:  Jose .
Apellidos: Aranda Martinez.
NHC: 2748903.
NASS: 26 37482910 04.
Domicilio: Calle Losada Mart?? 23, 5 B..
Localidad/ Provincia: Madrid.
CP: 28016.
Datos asistenciales.
Fecha de nacimiento: 15/04/1977.
Pa??s: Espa??a.
Edad: 37 a??os Sexo: F.
Fecha de Ingreso: 05/06/2018.
M??dico: Mar??a Merino Viveros  N??Col: 28 28 35489.
Informe cl??nico del paciente: var??n de 37 a??os con vida previa activa que refiere dolores osteoarticulares de localizaci??n variable en el ??ltimo mes y fiebre en la ??ltima semana con picos (matutino y vespertino) de 40 C las ??ltimas 24-48 horas, por lo que acude al Servicio de Urgencias. Antes de comenzar el cuadro estuvo en Extremadura en una regi??n end??mica de brucella, ingiriendo leche de cabra sin pasteurizar y queso de dicho ganado. Entre los comensales aparecieron varios casos de brucelosis. Durante el ingreso para estudio del s??ndrome febril con antecedentes epidemiol??gicos de posible exposici??n a Brucella presenta un cuadro de orquiepididimitis derecha.
La exploraci??n f??sica revela: T?? 40,2 C; T.A: 109/68 mmHg; Fc: 105 lpm. Se encuentra consciente, orientado, sudoroso, eupneico, con buen estado de nutrici??n e hidrataci??n. En cabeza y cuello no se palpan adenopat??as, ni bocio ni ingurgitaci??n de vena yugular, con pulsos carot??deos sim??tricos. Auscultaci??n card??aca r??tmica, sin soplos, roces ni extratonos. Auscultaci??n pulmonar con conservaci??n del murmullo vesicular. Abdomen blando, depresible, sin masas ni megalias. En la exploraci??n neurol??gica no se detectan signos men??ngeos ni datos de focalidad. Extremidades sin varices ni edemas. Pulsos perif??ricos presentes y sim??tricos. En la exploraci??n urol??gica se aprecia el teste derecho aumentado de tama??o, no adherido a piel, con zonas de fluctuaci??n e intensamente doloroso a la palpaci??n, con p??rdida del l??mite epid??dimo-testicular y transiluminaci??n positiva.
Los datos anal??ticos muestran los siguentes resultados: Hemograma: Hb 13,7 g/dl; leucocitos 14.610/mm3 (neutr??filos 77%); plaquetas 206.000/ mm3. VSG: 40 mm 1?? hora. Coagulaci??n: TQ 87%; TTPA 25,8 seg. Bioqu??mica: Glucosa 117 mg/dl; urea 29 mg/dl; creatinina 0,9 mg/dl; sodio 136 mEq/l; potasio 3,6 mEq/l; GOT 11 U/l; GPT 24 U/l; GGT 34 U/l; fosfatasa alcalina 136 U/l; calcio 8,3 mg/dl. Orina: sedimento normal.
Durante el ingreso se solicitan Hemocultivos: positivo para Brucella y Serolog??as espec??ficas para Brucella: Rosa de Bengala +++; Test de Coombs > 1/1280; Brucellacapt > 1/5120. Las pruebas de imagen solicitadas ( Rx t??rax, Ecograf??a abdominal, TAC craneal, Ecocardiograma transtor??cico) no evidencian patolog??a significativa, excepto la Ecograf??a testicular, que muestra engrosamiento de la bolsa escrotal con peque??a cantidad de l??quido con septos y test??culo aumentado de tama??o con peque??as zonas hipoecoicas en su interior que pueden representar microabscesos.
Con el diagn??stico de orquiepididimitis secundaria a Brucella se instaura tratamiento sintom??tico (antit??rmicos, antiinflamatorios, reposo y elevaci??n testicular) as?? como tratamiento antibi??tico espec??fico: Doxiciclina 100 mg v??a oral cada 12 horas (durante 6 semanas) y Estreptomicina 1 gramo intramuscular cada 24 horas (durante 3 semanas). El paciente mejora significativamente de su cuadro tras una semana de ingreso, decidi??ndose el alta a su domicilio donde complet?? la pauta de tratamiento antibi??tico. En revisiones sucesivas en consultas se constat?? la completa remisi??n del cuadro.
Remitido por: Dra. Mar??a Merino Viveros Hospital Universitario de Getafe Servicio de Endocrinolog??a y Nutrici??n Carretera de Toledo km 12,500 28905 Getafe - Madrid (Espa??a) Correo electr??nico: marietta84@hotmail.com
"

val result = deid_pipeline.annotate(sample)
```
</div>

## Results

```bash
Masked with entity labels
------------------------------
Datos <SEX>.
Nombre:  <PATIENT> .
Apellidos: <PATIENT>.
NHC: <SSN>.
NASS: <ID>.
Domicilio: <STREET>, <AGE> B..
Localidad/ Provincia: <CITY>.
CP: <ZIP>.
Datos asistenciales.
Fecha de nacimiento: <DATE>.
Pa??s: <COUNTRY>.
Edad: <AGE> a??os Sexo: <SEX>.
Fecha de Ingreso: <DATE>.
M??dico: <DOCTOR>  N??Col: <ID>.
Informe cl??nico <SEX>: <SSN> de <AGE> a??os con vida previa activa que refiere dolores osteoarticulares de localizaci??n variable en el ??ltimo mes y fiebre en la ??ltima semana con picos (matutino y vespertino) de 40 C las ??ltimas 24-48 horas, por lo que acude al Servicio de Urgencias.
Antes de comenzar el cuadro estuvo en <CITY> en una regi??n end??mica de brucella, ingiriendo leche de cabra sin pasteurizar y queso de dicho ganado.
Entre los comensales aparecieron varios casos de brucelosis.
Durante el ingreso para estudio del s??ndrome febril con antecedentes epidemiol??gicos de posible exposici??n a Brucella presenta un cuadro de orquiepididimitis derecha.
La exploraci??n f??sica revela: T?? 40,2 C; T.A: 109/68 mmHg; Fc: 105 lpm. Se encuentra consciente, orientado, sudoroso, eupneico, con buen estado de nutrici??n e hidrataci??n.
En cabeza y cuello no se palpan adenopat??as, ni bocio ni ingurgitaci??n de vena yugular, con pulsos carot??deos sim??tricos.
Auscultaci??n card??aca r??tmica, sin soplos, roces ni extratonos.
Auscultaci??n pulmonar con conservaci??n del murmullo vesicular.
Abdomen blando, depresible, sin masas ni megalias.
En la exploraci??n neurol??gica no se detectan signos men??ngeos ni datos de focalidad.
Extremidades sin varices ni edemas.
Pulsos perif??ricos presentes y sim??tricos.
En la exploraci??n urol??gica se aprecia el teste derecho aumentado de tama??o, no adherido a piel, con zonas de fluctuaci??n e intensamente doloroso a la palpaci??n, con p??rdida del l??mite epid??dimo-testicular y transiluminaci??n positiva.
Los datos anal??ticos muestran los siguentes resultados: Hemograma: Hb 13,7 g/dl; leucocitos 14.610/mm3 (neutr??filos 77%); plaquetas 206.000/ mm3.
VSG: 40 mm 1?? hora.
Coagulaci??n: TQ 87%;
<ORGANIZATION> 25,8 seg.
Bioqu??mica: Glucosa 117 mg/dl; urea 29 mg/dl; creatinina 0,9 mg/dl; sodio 136 mEq/l; potasio 3,6 mEq/l; GOT 11 U/l; GPT 24 U/l; GGT 34 U/l; fosfatasa alcalina 136 U/l; calcio 8,3 mg/dl.
Orina: sedimento normal.
Durante el ingreso se solicitan Hemocultivos: positivo para Brucella y Serolog??as espec??ficas para Brucella: <PATIENT> +++;
Test de Coombs > <DATE>; Brucellacapt > 1/5120.
Las pruebas de imagen solicitadas ( Rx t??rax, Ecograf??a abdominal, TAC craneal, Ecocardiograma transtor??cico) no evidencian patolog??a significativa, excepto la Ecograf??a testicular, que muestra engrosamiento de la bolsa escrotal con peque??a cantidad de l??quido con septos y test??culo aumentado de tama??o con peque??as zonas hipoecoicas en su interior que pueden representar microabscesos.
Con el diagn??stico de orquiepididimitis secundaria a Brucella se instaura tratamiento sintom??tico (antit??rmicos, antiinflamatorios, reposo y elevaci??n testicular) as?? como tratamiento antibi??tico espec??fico: Doxiciclina 100 mg v??a oral cada 12 horas (durante 6 semanas) y Estreptomicina 1 gramo intramuscular cada 24 horas (durante 3 semanas).
<SEX> <SSN> significativamente de su cuadro tras una semana de ingreso, decidi??ndose el alta a su domicilio donde complet?? la pauta de tratamiento antibi??tico. En revisiones sucesivas en consultas se constat?? la completa remisi??n del cuadro.
Remitido por: Dra.
<DOCTOR> <HOSPITAL> Servicio de Endocrinolog??a y Nutrici??n <STREET> <ZIP> <CITY> - <CITY> (<COUNTRY>) Correo electr??nico: <EMAIL>

Masked with chars
------------------------------
Datos [**********].
Nombre:  [**] .
Apellidos: [*************].
NHC: [*****].
NASS: [************].
Domicilio: [*******************], * B..
Localidad/ Provincia: [****].
CP: [***].
Datos asistenciales.
Fecha de nacimiento: [********].
Pa??s: [****].
Edad: ** a??os Sexo: *.
Fecha de Ingreso: [********].
M??dico: [******************]  N??Col: [*********].
Informe cl??nico [**********]: [***] de ** a??os con vida previa activa que refiere dolores osteoarticulares de localizaci??n variable en el ??ltimo mes y fiebre en la ??ltima semana con picos (matutino y vespertino) de 40 C las ??ltimas 24-48 horas, por lo que acude al Servicio de Urgencias.
Antes de comenzar el cuadro estuvo en [*********] en una regi??n end??mica de brucella, ingiriendo leche de cabra sin pasteurizar y queso de dicho ganado.
Entre los comensales aparecieron varios casos de brucelosis.
Durante el ingreso para estudio del s??ndrome febril con antecedentes epidemiol??gicos de posible exposici??n a Brucella presenta un cuadro de orquiepididimitis derecha.
La exploraci??n f??sica revela: T?? 40,2 C; T.A: 109/68 mmHg; Fc: 105 lpm. Se encuentra consciente, orientado, sudoroso, eupneico, con buen estado de nutrici??n e hidrataci??n.
En cabeza y cuello no se palpan adenopat??as, ni bocio ni ingurgitaci??n de vena yugular, con pulsos carot??deos sim??tricos.
Auscultaci??n card??aca r??tmica, sin soplos, roces ni extratonos.
Auscultaci??n pulmonar con conservaci??n del murmullo vesicular.
Abdomen blando, depresible, sin masas ni megalias.
En la exploraci??n neurol??gica no se detectan signos men??ngeos ni datos de focalidad.
Extremidades sin varices ni edemas.
Pulsos perif??ricos presentes y sim??tricos.
En la exploraci??n urol??gica se aprecia el teste derecho aumentado de tama??o, no adherido a piel, con zonas de fluctuaci??n e intensamente doloroso a la palpaci??n, con p??rdida del l??mite epid??dimo-testicular y transiluminaci??n positiva.
Los datos anal??ticos muestran los siguentes resultados: Hemograma: Hb 13,7 g/dl; leucocitos 14.610/mm3 (neutr??filos 77%); plaquetas 206.000/ mm3.
VSG: 40 mm 1?? hora.
Coagulaci??n: TQ 87%;
[**] 25,8 seg.
Bioqu??mica: Glucosa 117 mg/dl; urea 29 mg/dl; creatinina 0,9 mg/dl; sodio 136 mEq/l; potasio 3,6 mEq/l; GOT 11 U/l; GPT 24 U/l; GGT 34 U/l; fosfatasa alcalina 136 U/l; calcio 8,3 mg/dl.
Orina: sedimento normal.
Durante el ingreso se solicitan Hemocultivos: positivo para Brucella y Serolog??as espec??ficas para Brucella: [*************] +++;
Test de Coombs > [****]; Brucellacapt > 1/5120.
Las pruebas de imagen solicitadas ( Rx t??rax, Ecograf??a abdominal, TAC craneal, Ecocardiograma transtor??cico) no evidencian patolog??a significativa, excepto la Ecograf??a testicular, que muestra engrosamiento de la bolsa escrotal con peque??a cantidad de l??quido con septos y test??culo aumentado de tama??o con peque??as zonas hipoecoicas en su interior que pueden representar microabscesos.
Con el diagn??stico de orquiepididimitis secundaria a Brucella se instaura tratamiento sintom??tico (antit??rmicos, antiinflamatorios, reposo y elevaci??n testicular) as?? como tratamiento antibi??tico espec??fico: Doxiciclina 100 mg v??a oral cada 12 horas (durante 6 semanas) y Estreptomicina 1 gramo intramuscular cada 24 horas (durante 3 semanas).
[*********] [****] significativamente de su cuadro tras una semana de ingreso, decidi??ndose el alta a su domicilio donde complet?? la pauta de tratamiento antibi??tico. En revisiones sucesivas en consultas se constat?? la completa remisi??n del cuadro.
Remitido por: Dra.
[******************] [******************************] Servicio de Endocrinolog??a y Nutrici??n [***************************] [***] [****] - [****] ([****]) Correo electr??nico: [********************]

Masked with fixed length chars
------------------------------
Datos ****.
Nombre:  **** .
Apellidos: ****.
NHC: ****.
NASS: ****.
Domicilio: ****, **** B..
Localidad/ Provincia: ****.
CP: ****.
Datos asistenciales.
Fecha de nacimiento: ****.
Pa??s: ****.
Edad: **** a??os Sexo: ****.
Fecha de Ingreso: ****.
M??dico: ****  N??Col: ****.
Informe cl??nico ****: **** de **** a??os con vida previa activa que refiere dolores osteoarticulares de localizaci??n variable en el ??ltimo mes y fiebre en la ??ltima semana con picos (matutino y vespertino) de 40 C las ??ltimas 24-48 horas, por lo que acude al Servicio de Urgencias.
Antes de comenzar el cuadro estuvo en **** en una regi??n end??mica de brucella, ingiriendo leche de cabra sin pasteurizar y queso de dicho ganado.
Entre los comensales aparecieron varios casos de brucelosis.
Durante el ingreso para estudio del s??ndrome febril con antecedentes epidemiol??gicos de posible exposici??n a Brucella presenta un cuadro de orquiepididimitis derecha.
La exploraci??n f??sica revela: T?? 40,2 C; T.A: 109/68 mmHg; Fc: 105 lpm. Se encuentra consciente, orientado, sudoroso, eupneico, con buen estado de nutrici??n e hidrataci??n.
En cabeza y cuello no se palpan adenopat??as, ni bocio ni ingurgitaci??n de vena yugular, con pulsos carot??deos sim??tricos.
Auscultaci??n card??aca r??tmica, sin soplos, roces ni extratonos.
Auscultaci??n pulmonar con conservaci??n del murmullo vesicular.
Abdomen blando, depresible, sin masas ni megalias.
En la exploraci??n neurol??gica no se detectan signos men??ngeos ni datos de focalidad.
Extremidades sin varices ni edemas.
Pulsos perif??ricos presentes y sim??tricos.
En la exploraci??n urol??gica se aprecia el teste derecho aumentado de tama??o, no adherido a piel, con zonas de fluctuaci??n e intensamente doloroso a la palpaci??n, con p??rdida del l??mite epid??dimo-testicular y transiluminaci??n positiva.
Los datos anal??ticos muestran los siguentes resultados: Hemograma: Hb 13,7 g/dl; leucocitos 14.610/mm3 (neutr??filos 77%); plaquetas 206.000/ mm3.
VSG: 40 mm 1?? hora.
Coagulaci??n: TQ 87%;
**** 25,8 seg.
Bioqu??mica: Glucosa 117 mg/dl; urea 29 mg/dl; creatinina 0,9 mg/dl; sodio 136 mEq/l; potasio 3,6 mEq/l; GOT 11 U/l; GPT 24 U/l; GGT 34 U/l; fosfatasa alcalina 136 U/l; calcio 8,3 mg/dl.
Orina: sedimento normal.
Durante el ingreso se solicitan Hemocultivos: positivo para Brucella y Serolog??as espec??ficas para Brucella: **** +++;
Test de Coombs > ****; Brucellacapt > 1/5120.
Las pruebas de imagen solicitadas ( Rx t??rax, Ecograf??a abdominal, TAC craneal, Ecocardiograma transtor??cico) no evidencian patolog??a significativa, excepto la Ecograf??a testicular, que muestra engrosamiento de la bolsa escrotal con peque??a cantidad de l??quido con septos y test??culo aumentado de tama??o con peque??as zonas hipoecoicas en su interior que pueden representar microabscesos.
Con el diagn??stico de orquiepididimitis secundaria a Brucella se instaura tratamiento sintom??tico (antit??rmicos, antiinflamatorios, reposo y elevaci??n testicular) as?? como tratamiento antibi??tico espec??fico: Doxiciclina 100 mg v??a oral cada 12 horas (durante 6 semanas) y Estreptomicina 1 gramo intramuscular cada 24 horas (durante 3 semanas).
**** **** significativamente de su cuadro tras una semana de ingreso, decidi??ndose el alta a su domicilio donde complet?? la pauta de tratamiento antibi??tico. En revisiones sucesivas en consultas se constat?? la completa remisi??n del cuadro.
Remitido por: Dra.
**** **** Servicio de Endocrinolog??a y Nutrici??n **** **** **** - **** (****) Correo electr??nico: ****

Obfuscated
------------------------------
Datos Hombre.
Nombre:  Aurora Garrido Paez .
Apellidos: Aurora Garrido Paez.
NHC: BBBBBBBBQR648597.
NASS: 48127833R.
Domicilio: C/ Rambla, 246, 5 B..
Localidad/ Provincia: Alicante.
CP: 24202.
Datos asistenciales.
Fecha de nacimiento: 21/04/1977.
Pa??s: Portugal.
Edad: 56 a??os Sexo: Hombre.
Fecha de Ingreso: 10/07/2018.
M??dico: Francisco Jos?? Roca Berm??dez  N??Col: 21344083-P.
Informe cl??nico Hombre: 041010000011 de 56 a??os con vida previa activa que refiere dolores osteoarticulares de localizaci??n variable en el ??ltimo mes y fiebre en la ??ltima semana con picos (matutino y vespertino) de 40 C las ??ltimas 24-48 horas, por lo que acude al Servicio de Urgencias.
Antes de comenzar el cuadro estuvo en Zaragoza en una regi??n end??mica de brucella, ingiriendo leche de cabra sin pasteurizar y queso de dicho ganado.
Entre los comensales aparecieron varios casos de brucelosis.
Durante el ingreso para estudio del s??ndrome febril con antecedentes epidemiol??gicos de posible exposici??n a Brucella presenta un cuadro de orquiepididimitis derecha.
La exploraci??n f??sica revela: T?? 40,2 C; T.A: 109/68 mmHg; Fc: 105 lpm. Se encuentra consciente, orientado, sudoroso, eupneico, con buen estado de nutrici??n e hidrataci??n.
En cabeza y cuello no se palpan adenopat??as, ni bocio ni ingurgitaci??n de vena yugular, con pulsos carot??deos sim??tricos.
Auscultaci??n card??aca r??tmica, sin soplos, roces ni extratonos.
Auscultaci??n pulmonar con conservaci??n del murmullo vesicular.
Abdomen blando, depresible, sin masas ni megalias.
En la exploraci??n neurol??gica no se detectan signos men??ngeos ni datos de focalidad.
Extremidades sin varices ni edemas.
Pulsos perif??ricos presentes y sim??tricos.
En la exploraci??n urol??gica se aprecia el teste derecho aumentado de tama??o, no adherido a piel, con zonas de fluctuaci??n e intensamente doloroso a la palpaci??n, con p??rdida del l??mite epid??dimo-testicular y transiluminaci??n positiva.
Los datos anal??ticos muestran los siguentes resultados: Hemograma: Hb 13,7 g/dl; leucocitos 14.610/mm3 (neutr??filos 77%); plaquetas 206.000/ mm3.
VSG: 40 mm 1?? hora.
Coagulaci??n: TQ 87%;
Tecnogroup SL 25,8 seg.
Bioqu??mica: Glucosa 117 mg/dl; urea 29 mg/dl; creatinina 0,9 mg/dl; sodio 136 mEq/l; potasio 3,6 mEq/l; GOT 11 U/l; GPT 24 U/l; GGT 34 U/l; fosfatasa alcalina 136 U/l; calcio 8,3 mg/dl.
Orina: sedimento normal.
Durante el ingreso se solicitan Hemocultivos: positivo para Brucella y Serolog??as espec??ficas para Brucella: Mar??a Migu??lez Sanz +++;
Test de Coombs > 07-25-1974; Brucellacapt > 1/5120.
Las pruebas de imagen solicitadas ( Rx t??rax, Ecograf??a abdominal, TAC craneal, Ecocardiograma transtor??cico) no evidencian patolog??a significativa, excepto la Ecograf??a testicular, que muestra engrosamiento de la bolsa escrotal con peque??a cantidad de l??quido con septos y test??culo aumentado de tama??o con peque??as zonas hipoecoicas en su interior que pueden representar microabscesos.
Con el diagn??stico de orquiepididimitis secundaria a Brucella se instaura tratamiento sintom??tico (antit??rmicos, antiinflamatorios, reposo y elevaci??n testicular) as?? como tratamiento antibi??tico espec??fico: Doxiciclina 100 mg v??a oral cada 12 horas (durante 6 semanas) y Estreptomicina 1 gramo intramuscular cada 24 horas (durante 3 semanas).
F. 041010000011 significativamente de su cuadro tras una semana de ingreso, decidi??ndose el alta a su domicilio donde complet?? la pauta de tratamiento antibi??tico. En revisiones sucesivas en consultas se constat?? la completa remisi??n del cuadro.
Remitido por: Dra.
Francisco Jos?? Roca Berm??dez Hospital 12 de Octubre Servicio de Endocrinolog??a y Nutrici??n Calle Ram??n y Cajal s/n 03129 Zaragoza - Alicante (Portugal) Correo electr??nico: richard@company.it
```

{:.model-param}
## Model Information

{:.table-model}
|---|---|
|Model Name:|clinical_deidentification_augmented|
|Type:|pipeline|
|Compatibility:|Healthcare NLP 3.4.1+|
|License:|Licensed|
|Edition:|Official|
|Language:|es|
|Size:|281.2 MB|

## Included Models

- nlp.DocumentAssembler
- nlp.SentenceDetectorDLModel
- nlp.TokenizerModel
- nlp.WordEmbeddingsModel
- medical.NerModel
- nlp.NerConverter
- ContextualParserModel
- ContextualParserModel
- ContextualParserModel
- ContextualParserModel
- nlp.RegexMatcherModel
- ChunkMergeModel
- medical.DeIdentificationModel
- medical.DeIdentificationModel
- medical.DeIdentificationModel
- medical.DeIdentificationModel
- Finisher