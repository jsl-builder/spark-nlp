---
layout: demopage
title: Spark NLP in Action
full_width: true
permalink: /resolve_entities_codes
key: demo
license: false
show_edit_on_github: false
show_date: false
data:
  sections:  
    - title: Spark NLP for Healthcare 
      excerpt: Resolve Entities to Codes 
      secheader: yes
      secheader:
        - title: Spark NLP for Healthcare
          subtitle: Resolve Entities to Codes 
          activemenu: resolve_entities_codes
      source: yes
      source: 
        - title: SNOMED coding
          hide: yes
          id: snomed_coding
          image: 
              src: /assets/images/Detect_signs_and_symptoms.svg
          image2: 
              src: /assets/images/Detect_signs_and_symptoms_f.svg
          excerpt: Automatically resolve the SNOMED code corresponding to the diseases and conditions mentioned in your health record using Spark NLP for Healthcare out of the box.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ER_SNOMED
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/ER_SNOMED.ipynb
        - title: ICDO coding
          hide: yes
          id: icdo_coding
          image: 
              src: /assets/images/Detect_diagnosis_and_procedures.svg
          image2: 
              src: /assets/images/Detect_diagnosis_and_procedures_f.svg
          excerpt: Automatically detect the tumor in your healthcare records and link it to the corresponding ICDO code using Spark NLP for Healthcare out of the box.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ER_ICDO
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/ER_ICDO.ipynb
        - title: Sentence Entity Resolver for ICD10-CM coding
          hide: yes
          id: icd10-cm_coding
          image: 
              src: /assets/images/Detect_risk_factors.svg
          image2: 
              src: /assets/images/Detect_risk_factors_f.svg
          excerpt: This model maps clinical entities and concepts to ICD10 CM codes using sentence biobert embeddings.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ER_ICD10_CM/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/ER_ICD10_CM.ipynb
        - title: Sentence Entity Resolver for RxNorm
          hide: yes
          id: rxnorm_coding
          image: 
              src: /assets/images/Detect_drugs_and_prescriptions.svg
          image2: 
              src: /assets/images/Detect_drugs_and_prescriptions_f.svg
          excerpt: This model maps extracted medical entities to RxNorm codes using sentence embeddings, and has faster load time, with a speedup of about 6X when compared to previous versions.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ER_RXNORM/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/ER_RXNORM.ipynb
        - title: Logical Observation Identifiers Names and Codes (LOINC)
          hide: yes
          id: logical-observation-identifiers-names-and-codes
          image: 
              src: /assets/images/Detect_drugs_and_prescriptions.svg
          image2: 
              src: /assets/images/Detect_drugs_and_prescriptions_f.svg
          excerpt: Map clinical NER entities to Logical Observation Identifiers Names and Codes (LOINC) using our pre-trained model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ER_LOINC/
          - text: Colab Netbook
            type: blue_btn
            url: https://githubtocolab.com/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/24.Improved_Entity_Resolvers_in_SparkNLP_with_sBert.ipynb
        - title: Map Healthcare Codes
          id: logical-observation-identifiers-names-and-codes
          image: 
              src: /assets/images/Detect_traffic_information_in_text.svg
          image2: 
              src: /assets/images/Detect_traffic_information_in_text_f.svg
          excerpt: These pretrained pipelines map various codes (e.g., ICD10CM codes to SNOMED codes) without using any text data.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ER_CODE_MAPPING/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/11.1.Healthcare_Code_Mapping.ipynb#scrollTo=e5qYdIEv4JPL              
---
