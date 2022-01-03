---
layout: demopage
title: Spark NLP in Action
full_width: true
permalink: /recognize_clinical_entities
key: demo
license: false
show_edit_on_github: false
show_date: false
data:
  sections:  
    - title: Spark NLP for Healthcare 
      excerpt: Recognize Clinical Entities
      secheader: yes
      secheader:
        - title: Spark NLP for Healthcare
          subtitle: Recognize Clinical Entities
          activemenu: recognize_clinical_entities
      source: yes
      source: 
        - title: Detect signs and symptoms
          id: detect_signs_and_symptoms
          image: 
              src: /assets/images/Detect_signs_and_symptoms.svg
          image2: 
              src: /assets/images/Detect_signs_and_symptoms_f.svg
          excerpt: Automatically identify <b>Signs</b> and <b>Symptoms</b> in clinical documents using two of our pretrained Spark NLP clinical models.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_SIGN_SYMP/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_SIGN_SYMP.ipynb
        - title: Detect diagnosis and procedures
          id: detect_diagnosis_and_procedures
          image: 
              src: /assets/images/Detect_diagnosis_and_procedures.svg
          image2: 
              src: /assets/images/Detect_diagnosis_and_procedures_f.svg
          excerpt: Automatically identify diagnoses and procedures in clinical documents using the pretrained Spark NLP clinical model <b>ner_clinical.</b>
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_DIAG_PROC/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_DIAG_PROC.ipynb
        - title: Detect drugs and prescriptions
          id: detect_drugs_and_prescriptions
          image: 
              src: /assets/images/Detect_drugs_and_prescriptions.svg
          image2: 
              src: /assets/images/Detect_drugs_and_prescriptions_f.svg
          excerpt: Automatically identify <b>Drug, Dosage, Duration, Form, Frequency, Route,</b> and <b>Strength</b> details in clinical documents using three of our pretrained Spark NLP clinical models.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_POSOLOGY/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_POSOLOGY.ipynb
        - title: Identify diagnosis and symptoms assertion status
          id: identify_diagnosis_and_symptoms_assertion_status
          image: 
              src: /assets/images/Identify_diagnosis_and_symptoms_assertion_status.svg
          image2: 
              src: /assets/images/Identify_diagnosis_and_symptoms_assertion_status_f.svg
          excerpt: Automatically detect if a diagnosis or a symptom is present, absent, uncertain or associated to other persons (e.g. family members).
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/ASSERTION/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/2.Clinical_Assertion_Model.ipynb
        - title: Adverse drug events tagger
          id: adverse_drug_events_tagger
          image: 
              src: /assets/images/Adverse_drug_events_tagger.svg
          image2: 
              src: /assets/images/Adverse_drug_events_tagger_f.svg
          excerpt: Automatic pipeline that tags documents as containing or not containing adverse events description, then identifies those events.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/PP_ADE/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/16.Adverse_Drug_Event_ADE_NER_and_Classifier.ipynb
        - title: Detect anatomical references
          id: detect_anatomical_references
          image: 
              src: /assets/images/Detect_anatomical_references.svg
          image2: 
              src: /assets/images/Detect_anatomical_references_f.svg
          excerpt: Automatically identify <b>Anatomical System, Cell, Cellular Component, Anatomical Structure, Immaterial Anatomical Entity, Multi-tissue Structure, Organ, Organism Subdivision, Organism Substance, Pathological Formation</b> in clinical documents using our pretrained Spark NLP model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_ANATOMY/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_ANATOMY.ipynb
        - title: Detect clinical events
          id: detect_clinical_events
          image: 
              src: /assets/images/Detect_clinical_events.svg
          image2: 
              src: /assets/images/Detect_clinical_events_f.svg
          excerpt: Automatically identify a variety of clinical events such as <b>Problems, Tests, Treatments, Admissions</b> or <b>Discharges</b>, in clinical documents using two of our pretrained Spark NLP models.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_EVENTS_CLINICAL
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_EVENTS_CLINICAL.ipynb
        - title: Detect lab results
          id: detect_lab_results
          image: 
              src: /assets/images/Detect_lab_results.svg
          image2: 
              src: /assets/images/Detect_lab_results_f.svg
          excerpt: Automatically identify <b>Lab test names</b> and <b>Lab results</b> from clinical documents using our pretrained Spark NLP model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_LAB/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_LAB.ipynb        
        - title: Detect clinical entities in text
          id: detect_clinical_entities_in_text
          image: 
              src: /assets/images/Detect_risk_factors.svg
          image2: 
              src: /assets/images/Detect_risk_factors_f.svg
          excerpt: Automatically detect more than 50 clinical entities using our NER deep learning model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_CLINICAL
          - text: Colab Netbook
            type: blue_btn
            url: https://githubtocolab.com/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/1.Clinical_Named_Entity_Recognition_Model.ipynb
        - title: Detect risk factors
          id: detect_risk_factors
          image: 
              src: /assets/images/Detect_risk_factors.svg
          image2: 
              src: /assets/images/Detect_risk_factors_f.svg
          excerpt: Automatically identify risk factors such as <b>Coronary artery disease, Diabetes, Family history, Hyperlipidemia, Hypertension, Medications, Obesity, PHI, Smoking habits</b> in clinical documents using our pretrained Spark NLP model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_RISK_FACTORS/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_RISK_FACTORS.ipynb 
        - title: Detect Drug Chemicals (Bert For Token Classification) 
          hide: yes
          id: detect_drug_chemicals
          image: 
              src: /assets/images/Detect_Drug_Chemicals.svg
          image2: 
              src: /assets/images/Detect_Drug_Chemicals_f.svg
          excerpt: This demo shows how drug chemicals can be extracted from medical texts using Spark NLP model which trained with BertForTokenClassifier.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_BERT_TOKEN_CLASSIFIER/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_BERT_TOKEN_CLASSIFIER.ipynb
        - title: Detect Wide Range of Clinical Entities (Bert For Token Classification) 
          hide: yes
          id: detect_wide_range_clinical_entities
          image: 
              src: /assets/images/Detect_Wide_Range_of_Clinical_Entities.svg
          image2: 
              src: /assets/images/Detect_Wide_Range_of_Clinical_Entities_f.svg
          excerpt: This demo shows how clinical terminology can be extracted from medical texts using Spark NLP model which trained with BertForTokenClassifier. 
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_BERT_TOKEN_CLASSIFIER/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_BERT_TOKEN_CLASSIFIER.ipynb
        - title: Detect Wide Range of More Generalized Clinical Entities (Bert For Token Classification) 
          hide: yes
          id: detect_wide_range_clinical_entities_more
          image: 
              src: /assets/images/Detect_Wide_Range.svg
          image2: 
              src: /assets/images/Detect_Wide_Range_f.svg
          excerpt: This demo shows how clinical terminology can be extracted from medical texts using Spark NLP model which trained with BertForTokenClassifier. 
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_BERT_TOKEN_CLASSIFIER/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/streamlit_notebooks/healthcare/NER_BERT_TOKEN_CLASSIFIER.ipynb  
        - title: Detect Covid-related clinical terminology 
          id: detect_covid_related_clinical_terminology
          image: 
              src: /assets/images/Detect_Covid-related_clinical_terminology.svg
          image2: 
              src: /assets/images/Detect_Covid-related_clinical_terminology_f.svg
          excerpt: This demo shows how Covid-related clinical terminology can be detected using a Spark NLP Healthcare NER model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_COVID/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/1.Clinical_Named_Entity_Recognition_Model.ipynb
        - title: Find available models for your clinical entities 
          id: ner_model_finder
          image: 
              src: /assets/images/NER_Model_Finder.svg
          image2: 
              src: /assets/images/NER_Model_Finder_f.svg
          excerpt: This demo shows how to use a pretrained pipeline to find the best NER model given an entity name.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_MODEL_FINDER/
          - text: Colab Netbook
            type: blue_btn
            url: https://github.com/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/11.Pretrained_Clinical_Pipelines.ipynb
        - title: Extract Drugs and Chemicals
          id: extract_names_of_drugs_chemicals 
          image: 
              src: /assets/images/Extract_the_Names_of_Drugs_Chemicals.svg
          image2: 
              src: /assets/images/Extract_the_Names_of_Drugs_Chemicals_f.svg
          excerpt: This demo shows how Names of Drugs & Chemicals can be detected using a Spark NLP Healthcare NER model.
          actions:
          - text: Live Demo
            type: normal
            url: https://demo.johnsnowlabs.com/healthcare/NER_CHEMD/
          - text: Colab Netbook
            type: blue_btn
            url: https://colab.research.google.com/github/JohnSnowLabs/spark-nlp-workshop/blob/master/tutorials/Certification_Trainings/Healthcare/1.Clinical_Named_Entity_Recognition_Model.ipynb
---