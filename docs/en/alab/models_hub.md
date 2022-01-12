---
layout: docs
comment: no
header: true
seotitle: Annotation Lab | John Snow Labs
title: NLP Models Hub
permalink: /docs/en/alab/models_hub
key: docs-training
modify_date: "2021-06-23"
use_language_switcher: "Python-Scala"
show_nav: true
sidebar:
    nav: annotation-lab
---

The Annotation Lab 1.8.0 offers a tight integration with [NLP Models Hub](https://nlp.johnsnowlabs.com/models). Any compatible NER model and Embeddings can be downloaded and made available to the Annotation Lab users for preannotations either from within the application or via manual upload. 

Models Hub page can be accessed via the left navigation menu by users in the UserAdmins group.

<img class="image image--xl" src="/assets/images/annotation_lab/1.8.0/models_hub.png" style="width:100%; align:center; box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);"/>


The Models Hub page has three tabs that hold information about models and embeddings.

## Models Hub Tab
This tab lists all pretrained NER models and embeddings from NLP Models Hub which are compatible with Spark NLP 2.7.5 and which are defined for English language. By selecting one or multiple items from the grid view, users can download them to the Annotation Lab. The licensed/Healthcare models and embeddings are available to download only when a valid license is uploaded.

One restriction that we impose on models download/upload relates to the available disk space. Any model download requires that the double of its size is available on the local storage. If not enough space is available the download cannot proceed.  

Disk usage view, search, and filter features are available on the upper section of the Models Hub page.

<img class="image image--xl" src="/assets/images/annotation_lab/1.8.0/storage.png" style="width:100%; align:center; box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);"/>


## Available Models Tab
All the models available in the Annotation Lab are listed in this tab. The models are either trained within the Annotation Lab, uploaded to Annotation Lab by admin users, or downloaded from NLP Models Hub. General information about the models like labels/categories and the source (downloaded or trained or uploaded) of the model can be viewed. It is possible to delete any model or redownload failed ones by using the overflow menu on the top right corner of each model.

<img class="image image--xl" src="/assets/images/annotation_lab/1.8.0/re_download.png" style="width:60%; align:center; box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);"/>

## Available Embeddings Tab
This tab lists all embeddings available to the Annotation Lab together with information on their source and date of upload/download. Like models, any compatible embeddings can be downloaded from NLP Models Hub. By default, glove_100d embeddings are included in the deployment.

## Available Rules Tab

Spark NLP for Healthcare supports rule-based annotations via the ContextualParser Annotator. In this release, Annotationlab adds support for creating and using ContextualParser rules in NER project. 

Any user with admin privileges can see and edit the available rules under the `Available Rules` tab on the `Models Hub` page. Users can create new rules using the `+ Add Rules` button.

<img class="image image--xl" src="/assets/images/annotation_lab/2.6.0/rules_tab.png" style="width:100%; align:center; box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);"/>

There are two types of rules supported:

- **`Regex Based:`** Users can define a regex that will be used to label all possible hit chunks and label them as being the target entity. For example, for labeling height entities the following regex can be used `[0-7]'((0?[0-9])|(1(0|1)))`. All hits found in the task text that match the regex, are pre-annotated as heights.

- **`Dictionary Based:`** Users can define and upload a CSV dictionary of keywords that cover the list of chunks that should be annotated as a target entity. For example, for the label female: woman, lady, girl, all occurrences of stings woman, lady, and girl within the text of a given task will be perannotated as female.   

<img class="image image--xl" src="/assets/images/annotation_lab/2.6.0/types_of_rules.jpg" style="width:100%; align:center; box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);"/>

After adding a rule on `Models Hub` page, the `Project Owner` or `Manager` can add the rule to the configuration of the project where he wants to use it. This can be done via the `Rules` tab from the `Project Setup` page under the `Project Configuration` tab. A valid Spark NLP for Healthcare license is required to deploy rules from project config.



## Custom Models/Embeddings Upload

Custom NER models or embeddings can be uploaded using the Upload button present in the top right corner of the page. The labels predicted by the uploaded NER model need to be specified using the Model upload form.

<img class="image image--xl" src="/assets/images/annotation_lab/1.8.0/upload_models.png" style="width:70%; align:center; box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);"/>

The models& embeddings to upload need to be Spark NLP compatible. 

All available models are listed in the Spark NLP Pipeline Config on the Setup Page of any project and are ready to be included in the Labeling Config for preannotation.