---
layout: docs
comment: no
header: true
seotitle: Release Notes | John Snow Labs
title: Release Notes
permalink: /docs/en/alab/release_notes
key: docs-training
modify_date: "2021-11-11"
use_language_switcher: "Python-Scala"
show_nav: true
sidebar:
    nav: annotation-lab
---
## 2.7.0
Annotation Lab 2.7.0 is here! This is another feature reach release from John Snow Labs - Annotation Lab Team. It is powered by the latest Spark NLP and Spark NLP for Healthcare libraries and offers improved support for Rule Base Annotation. With the upgrade of Spark NLP libraries, the Models Hub page inside the application gets more than 100 new models for English along with the introduction of Spanish and German models. In Visual NER projects it is now easier to annotate cross line chunks. As always, there are many security and stabilizations shipped.

### Highlights
- Annotation Lab 2.7.0 includes Spark NLP 3.4.1 and Spark NLP for Healthcare. Model training is now significantly faster and issues related to Rule-based annotation have been solved. The Models Hub has increased the list of models and old incompatible models are now marked as "incompatible". If there are any incompatible models downloaded on the machine, we recommend deleting them.
- Spanish and German Models have been in Models Hub. In previous versions of the Annotation Lab, the Models Hub only offered English language models. But from version 2.7.0. models for two other languages are included as well, namely Spanish and German. It is possible to download or upload these models and use them for preannotation, in the same way as for English language models.

- Rule-Based Annotation improvement. Rule-based annotation, introduced in 2.6.0 was limited to a few view options, has improved in this release. The Rule creation UI form was simplified and extended, and help tips were added on each fields. While creating a rule, the user can define the scope of the rule as being `sentence` or `document`. A new toggle parameter `Complete Match Regex` is added to the rules. It can be toggled on to preannotate the entity that exactly matches the regex or dictionary value regardless of the `Match Scope`. Also case-sensitive is always true (and hence the toggle is hidden in this case) for REGEX while the case-sensitive toggle for dictionary can be toggled on or off. Users can now download the uploaded dictionary of an existing rule. In the previous release, if a dictionary-based rule was defined with an invalid CSV file, the preannotation server would crash and would only recover when the rule was removed from the configuration. This issue has been fixed and it is also possible to upload both vertical and horizontal CSV  consisting of multi-token dictionary values.

- Flexible annotations for Visual NER Projects. The chunk annotation feature added to Visual NER projects, allows the annotation of several consecutive tokens as one chunk. It also supports multiple lines selection. User can now select multiple tokens and annotate them together in Visual NER Projects. The label assigned to a connected group can be updated. This change will apply to all regions in the group.

- Constraints for relation labeling can be defined. While annotating projects with `Relations` between `Entities`, defining constraints (the direction, the domain, the co-domain) of relations is important. Annotation Lab 2.7.0 offers a way to define such constraints by editing the Project Configuration. The Project Owner or Project Managers can specify which `Relation` needs to be bound to which `Labels` and in which direction. This will hide some Relations in Labeling Page for NER Labels which will simplify the annotation process and will avoid the creation of any incorrect relations in the scope of the project.

### Security
Security issues related to SQL Injection Vulnerability and Host Header Attack were fixed in this release.

### Bug Fixes
- Issues related to chunk annotation; Incorrect bounding boxes, Multiple stacking of bounding boxes, Inconsistent IDs of the regions, unchanged labels of one connected region to other were identified and fixed and annotators can now select multiple tokens at once and annotate them as a single chunk
- In the previous release, after an Assertion Status model was trained, it would get deployed without the NER model and hence the preannotation was not working as expected. Going forward, the trained Assertion Model cannot be deployed for projects without a NER model. For this to happen, the "Yes" button in the confirmation box for deploying an assertion model right after training is enabled only when the Project Configuration consists of at least one NER model.
- A bug in the default Project templates (Project Setup page) was preventing users to create projects using “Conditional classification” and “Pairwise comparison" templates. These default Project templates can be used with no trouble as any other 40+ default templates.
- Reviewers were able to view unassigned submitted tasks via the "Next" button on the Labeling page. This bug is also fixed now and the reviewers can only see tasks that are assigned to them both on the Task List page or while navigating through the "Next" button.
- For better user experience, the labeling page has been optimized and the tasks on the page render quicker than in previous versions. When adding a user to the UserAdmins group, the delay in enabling the checkbox has been fixed.

## 2.6.0
Annotation Lab v2.6.0 improves the performance of the `Project Setup` page, adds a "View as" option in the `Labeling` Page, improves the layout of OCR-ed documents, adds the option to stop training and model server deployment from UI. Many more cool features are also delivered in this version to enhance usability and stabilize the product. Here are details of features and bug fixes included in this release.

### Highlights
- Performance improvement in Setup page. In previous versions of Annotation Lab, changes in Project Configuration would take a long time to validate if that project included a high volume of completions. The configuration validation time is now almost instant, even for projects with thousand of tasks. Multiple tests were conducted on projects with more than 13K+ tasks and thousands of extractions per task. For all of those test situations, the validation of the Project Configuration took under 2 seconds. Those tests results were replicated for all types of projects including NER, Image, Audio, Classification, and HTML projects.
- New "View as" option in the labeling screen. When a user has multiple roles (Manager, Annotator, Reviewer), the Labeling Page should present and render different content and specific UX, depending on the role impersonated by the user. For a better user experience, this version adds a "View as" switch in the Labeling Page. Once the "View as" option is used to select a certain role, the selection is preserved even when the tab is closed or refreshed.
- OCR Layout improvement. In previous versions of the Annotation Lab, layout was not preserved in OCRed tasks. Recognized texts would be placed in a top to bottom approach without considering the paragraph each token belonged to. From this version on, we are using layout-preserving transformers from Spark OCR. As a result, tokens that belong to the same paragraph are now grouped together, producing more meaningful output.
- Ability to stop training and model server deployment. Up until now, training and model server deployment could be stopped by system admins only. This version of Annotation Lab provides Project Owners/Managers with the option to stop these processes simply by clicking a button in the UI. This option is necessary in many cases, such as when a manager/project owner starts the training process on a big project that takes a lot of resources and time, blocking access to preannotations to the other projects. 
- Display meaningful message when training fails due to memory issues. In case the training of a model fails due to memory issues, the reason for the failure are available via the UI (i.e. out of memory error).
- Allow combining NER labels and Classification classes from Spark NLP pipeline config. The earlier version had an issue with adding `choice` from the predefined classification model to an existing NER project. This issue has been fixed in this version.

### Bug Fixes
- Previously there was a UI reloading issue when a User was removed from the "Annotators" user group, which has now been fixed. The user can log in without the reloading issue, a warning is shown in UI regarding the missing annotator privilege.
- Also, setting up the `HTML NER Tagging` project was not possible in the earlier version which has been fixed in this release.
- On the labeling page, the renamed title of the next served task was not displayed. Similarly, in the Import page, the count of the tasks imported was missing in the Import Status dialog box. Now both these issues are fixed.

## 2.5.0

Annotation Lab v2.5.0 introduces support for rule based annotations, new search feature and COCO format export for Visual NER projects. It also includes fixes for the recently identified security issues and other known bugs. Below are the highlights of this release.

### Highlights
- Rule Based Annotations. Spark NLP for Healthcare supports rule-based annotations via the ContextualParser Annotator. In this release Annotationlab adds support for creating and using ContextualParser rules in NER project. Any user with admin privilegis can see rules under the `Available Rules` tab on the Models Hub page and can create new rules using the `+ Add Rule` button. After adding a rule on `Models Hub` page, the `Project Owner` or `Manager` can add the rule to the configuration of the project where he wants to use it. This can be done via the Rules tab from the `Project Setup` page under the `Project Configuration` tab. A valid Spark NLP for Healthcare licence is required to deploy rules from project config. Two types of rules are supported:1. `Regex Based`: User can enter the Regex which matches to the entities of the required label; and 2. `Dictionary Based`: User can create a dictionary of labels and user can upload the CSV of the list of entity that comes under the label.
- Search through Visual NER Projects. For the Visual NER Projects, it is now possible to search for a keyword inside of image/pdf based tasks using the search box available on the top of the `Labeling` page. Currently, the search is performed on the current page only. 
Furthermore, we have also extended the keyword-based task search already available for text-based projects for Visual NER Projects. On the `Tasks` page, use the search bar on the upper right side of the screen like you would do in other text-based projects, to identify all image/pdf tasks containing a given text.
- COCO export for pdf tasks in `Visual NER Projects`. Up until now, the COCO format export was limited to simple image documents. With version 2.5.0, this functionality is extended to single-page or multi-page pdf documents. 
- In **Classification Project**, users are now able to use different layouts for the list of choices:
   - **`layout="select"`**: It will change choices from list of choices inline to dropdown layout. Possible values are `"select", "inline", "vertical"`
   - **`choice="multiple"`**: Allow user to select multiple values from dropdown. Possible values are: `"single", "single-radio", "multiple"`
- Better Toasts, Confirmation-Boxes and Masking UI on potentially longer operations.

### Security Fixes
- Annotationlab v2.5.0 got different Common Vulnerabilities and Exposures(CVE) issues fixed. As always, in this release we performed security scans to detect CVE issues, upgraded python packages to eliminate known vulnerabilities and also we made sure the CVE-2021-44228 (Log4j2 issue) is not present in any images used by Annotation Lab.
- A reported issue when logout endpoint was sometimes redirected to insecure http after access token expired was also fixed.

### Bug Fixes

- The `Filters` option in the `Models Hub` page was not working properly. Now the "Free/Licensed" filter can be selected/deselected without getting any error.
- After creating relations and saving/updating annotations for the Visual NER projects with multi-paged pdf files, the annotations and relations were not saved. 
- An issue with missing text tokens in the exported JSON file for the Visual NER projects also have been fixed.


## 2.4.0

Annotation Lab v2.4.0 adds relation creation features for Visual NER projects and redesigns the Spark NLP Pipeline Config on the Project Setup Page. Several bug fixes and stabilizations are also included. Following are the highlights:

### Highlights
- Relations on Visual NER Projects. Annotators can create relations between annotated tokens/regions in Visual NER projects. This functionality is similar to what we already had in text-based projects. It is also possible to assign relation labels using the contextual widget (the "+" sign displayed next to the relation arrow).
- SparkNLP Pipeline Config page was redesigned. The SparkNLP Pipeline Config in the Setup Page was redesigned to ease filtering, collapsing, and expanding models and labels. For a more intuitive use, the Add Label button was moved to the top right side of the tab and no longer scrolls with the config list.
- This version also adds many improvements to the new Setup Page. The Training and Active Learning Tabs are only available to projects for which Annotation Lab supports training. When unsaved changes are present in the configuration, the user cannot move to the Training and Active Learning Tab and/or Training cannot be started. When the OCR server was not deployed, imports in the Visual NER project were failing. With this release, when a valid Spark OCR license is present, the OCR server is deployed and the import of pdf and image files is executed.

### Bug fixes
- When a login session is timed out and cookies are expired, users had to refresh the page to get the login screen. This known issue has been fixed and the user will be redirected to the login page.
- When a task was assigned to an annotator who does not have completions for it, the task status was shown incorrectly. This was fixed in this version.
- While preannotating tasks with some specific types of models, only the first few lines were annotated. We have fixed the Spark NLP pipeline for such models and now the entire document gets preannotations. When Spark NLP for Healthcare license was expired, the deployment was allowed but it used to fail. Now a proper message is shown to Project Owners/Managers and the deployment is not started in such cases.
- Inconsistencies were present in training logs and some embeddings were not successfully used for models training.
- Along with these fixes, several UI bugs are also fixed in this release.


## 2.3.0
### Highlights

- Multipage PDF annotation. Annotation Lab 2.3.0 supports the complete flow of import, annotation, and export for multi-page PDF files. Users have two options for importing a new PDF file into the Visual NER project:
  - Import PDF file from local storage
  - Add a link to the PDF file in the file attribute. 


After import, the user can see a task on the task page with a file name. On the labeling page, the user can view the PDF file with pagination so that the user can annotate the PDF one page at a time. After completing the annotation, the user can submit a task and it is ready to be exported to JSON and the user can import this exported file into any Visual NER project. [Note: Export in COCO format is not yet supported for PDF file]
- Redesign of the Project Setup Page. With the addition of many new features on every release, the Project Setup page became crowded and difficult to digest by users. In this release we have split its main components into multiple tabs: Project Description, Sharing, Configuration, and Training.
- Train and Test Dataset. Project Owner/Manager can tag the tasks that will be used for train and for test purposes. For this, two predefined tags will be available in all projects: `Train` and `Test`.
- Enhanced Relation Annotation. The user experience while annotating relations on the Labeling page has been improved. Annotators can now select the  desired relation(s) by clicking the plus "+" sign present next to the relations arrow.
- Other UX improvements: 
  - Multiple items selection with Shift Key in Models Hub and Tasks page,
  - Click instead of hover on more options in Models Hub and Tasks page,
  - Tabbed View on the ModelsHub page.

### Bug fixes
- Validations related to Training Settings across different types of projects were fixed.
- It is not very common to upload an expired license given a valid license is already present. But in case users did that there was an issue while using a license in the spark session because only the last uploaded license was used. Now it has been fixed to use any valid license no matter the order of upload.
- Sometimes search and filters in the ModelsHub page were not working. Also, there was an issue while removing defined labels on the Upload Models Form. Both of these issues are fixed.


## 2.2.2
### Highlights
- Support for pretrained Relation Extraction and Assertion Status models. A valid Spark NLP for HealthCare License is needed to download pretrained models via the Models Hub page. After download, they can be added to the Project Config and used for preannotations.
- Support for uploading local images. Until this version, only images from remote URLs could be uploaded for Image projects. With this version the Annotation Lab supports uploading images from you local storage/computer. It is possible to either import one image or multiple images by zipping them together. The maximum image file size is 16 MB. If you need to upload files exceding the default configuration, please contact your system administrator who will change the limit size in the installation artifact and run the upgrade script.
- Improved support for Visual NER projects. A sample task can be imported from the Import page by clicking the "Add Sample Task" button. Also default config for the Visual NER project contains zoom feature which supports maximum possible width for low resolution images when zooming.
- Improved Relation Labeling. Creating numerous relations in a single task can look a bit clumsy. The limited space in Labeling screen, the relation arrows and different relation types all at once could create difficulty to visualize them properly. We improved the UX for this feature:
  - Spaces between two lines if relations are present
  - Ability to Filter by certain relations
  - When hovered on one relation, only that is focused
- Miscellaneous. Generally when a first completion in a task is submitted, it is very likely for that completion to be the ground truth for that task. Starting with this version, the first submitted completion gets automatically starred. Hitting submit button on next completion, annotator are asked to either just submit or submit and star it.

### Bug fixes
- On restart of the Annotation Lab machine/VM all Downloaded models (from Models Hub) compatible with Spark NLP 3.1 version were deleted. We have now fixed this issue. Going forward, it is user's responsibility to remove any incompatible models. Those will only be marked as "Incompatible" in Models Hub.
- This version also fixes some reported issues in training logs.
- The CONLL exports were including Assertion Status labels too. Going forward Assertion Status labels will be excluded given correct Project Config is setup.

{:.btn-block}
[Read more](https://www.johnsnowlabs.com/active-learning-for-relation-extraction-and-assertion-status-models-with-annotation-lab/){:.button.button--primary.button--rounded.button--lg}


## 2.1.0

### Highlights
- A new project configuration "Visual NER Labeling" was added, which provides the skeleton for text annotation on scanned images.
- Project Owners or Project Manager can train open-source models too.
- The UI components and navigation of Annotation Lab - as a SPA - continues to improve its performance.
- The application has an increased performance (security and bug fixes, general optimizations).
- More models & embeddings included in the Annotation Lab image used for deployments. This should reduce the burden for system admins during the installation in air-gapped or enterprise environments.
- Easier way to add relations. 
- Project Owners and Managers can see the proper status of tasks, taking into account their own completions.
- Security Fixes. We understand and take the security issues as the highest priority. On every release, we run our artifacts and images through series of security testings (Static Code analysis, PenTest, Images Vulnerabilities Test, AWS AMI Scan Test). This version resolves a few critical issues that were recently identified in Python Docker image we use. We have upgraded it to a higher version. Along with this upgrade, we have also refactored our codebase to pass our standard Static Code Analysis.

### Bug fixes
- An issue with using Uploaded models was fixed so any uploaded models can be loaded in Project Config and used for preannotation.
- Issues related to error messages when uploading a valid Spark OCR license and when trying to train NER models while Spark OCR license was expired are now fixed.
- The issue with exporting annotations in COCO format for image projects was fixed. Project Owners and Managers should be able to export COCO format which also includes images used for annotations.
- The bug reports related to unexpected scrolling of the Labeling page, issues in Swagger documentation, and typos in some hover texts are now fixed.

{:.btn-block}
[Read more](https://www.johnsnowlabs.com/model-tuning-and-transfer-learning-in-the-annotation-lab/){:.button.button--primary.button--rounded.button--lg}

## 2.0.1
### Highlights
- Inter-Annotation Agreement Charts. To get a measure of how well multiple annotators can make the same annotation decision for a certain category, we are shipping seven different charts. To see these charts users can click on the third tab “Inter-Annotator Agreement” of the Analytics Dashboard of NER projects. There are dropdown boxes to change annotators for comparison purposes. It is also possible to download the data of some charts in CSV format by clicking the download button present at the bottom right corner of each of them. 
- Updated CONLL Export. In previous versions, numerous files were created based on Tasks and Completions. There were issues in the Header and no sentences were detected. Also, some punctuations were not correctly exported or were missing. The new CONLL export implementation results in a single file and fixes all the above issues. As in previous versions, if only Starred completions are needed in the exported file, users can select the “Only ground truth” checkbox.
- Search tasks by label. Now, it is possible to list the tasks based on some annotation criteria. Examples of supported queries: "label: ABC", "label: ABC=DEF", "choice: Mychoice", "label: ABC=DEF".
- Validation of labels and models is done beforehand. An error message is shown if the label is incompatible with models.
- Transfer Learning support for Training Models. Now its is possible to continue model training from an already available model. If a Medical NER model is present in the system, the project owner or manager can go to Advanced Options settings of the Training section in the Setup Page and choose it to Fine Tune the model. When Fine Tuning is enabled, the embeddings that were used to train the model need to be present in the system. If present, it will be automatically selected, otherwise users need to go to the Models Hub page and download or upload it.
- Training Community Models without the need of License. In previous versions, Annotation Lab didn’t allow training without the presence of Spark NLP for Healthcare license. But now the training with community embeddings is allowed even without the presence of Valid license. 
- Support for custom training scripts. If users want to change the default Training script present within the Annotation Lab, they can upload their own training pipeline. In the Training section of the Project Setup Page, only admin users can upload the training scripts. At the moment we are supporting the NER custom training script only.
- Users can now see a proper message on the Modelshub page when annotationlab is not connected to the internet (AWS S3 to be more precise). This happens in air-gapped environments or some issues in the enterprise network.
- Users now have the option to download the trained models from the Models Hub page. The download option is available under the overflow menu of each Model on the “Available Models” tab.
- Training Live Logs are improved in terms of content and readability.
- Not all Embeddings present in the Models Hub are supported by NER and Assertion Status Training. These are now properly validated from the UI.
- Conflict when trying to use deleted embeddings. The existence of the embeddings in training as well as in deployment is ensured and a readable message is shown to users.
- Support for adding custom CA certificate chain. Follow the instructions described in instruction.md file present in the installation artifact.


### Bug fixes

- When multiple paged OCR file was imported using Spark OCR, the task created did not have pagination.
- Due to a bug in the Assertion Status script, the training was not working at all. 
- Any AdminUser could delete the main “admin” user as well as itself. We have added proper validation to avoid such situations.

{:.btn-block}
[Read more](https://www.johnsnowlabs.com/inter-annotator-agreement-charts-transfer-learning-training-without-license-custom-training-script-with-annotation-lab/){:.button.button--primary.button--rounded.button--lg}
