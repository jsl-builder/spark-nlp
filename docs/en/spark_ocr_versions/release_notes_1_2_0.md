---
layout: docs
header: true
seotitle: Spark NLP
title: Spark NLP release notes 1.2.0
permalink: /docs/en/spark_ocr_versions/release_notes_1_2_0
key: docs-release-notes
modify_date: "2022-01-06"
show_nav: true
sidebar:
    nav: sparknlp
---

## 1.2.0

Release date: 08-04-2020


#### Overview

Improved support Databricks and processing selectable pdfs.

#### Enhancements

* Adapted Spark OCR for run on Databricks.
* Added rewriting positions in [ImageToText](ocr_pipeline_components#imagetotext) when run together with PdfToText.
* Added 'positionsCol' param to [ImageToText](ocr_pipeline_components#imagetotext).
* Improved support Spark NLP. Changed [start](/ocr_install#using-start-function) function.

#### New Features

* Added [showImage](ocr_structures#showimages) implicit to Dataframe for display images in Scala Databricks notebooks.
* Added [display_images](ocr_structures#display_images) function for display images in Python Databricks notebooks.
* Added propagation selectable pdf file in [TextToPdf](ocr_pipeline_components#texttopdf). Added 'inputContent' param to 'TextToPdf'.


<div class="prev_ver h3-box" markdown="1">

## Versions

</div>

<ul class="pagination">
    <li>
        <a href="release_notes_1_1_2">Version 1.1.2</a>
    </li>
    <li>
        <strong>Version 1.2.0</strong>
    </li>
    <li>
        <a href="release_notes_1_3_0">Version 1.3.0</a>
    </li>
</ul>

<ul class="pagination pagination_big">
  <li><a href="release_notes_3_12_0">3.12.0</a></li>
  <li><a href="release_notes_3_11_0">3.11.0</a></li>
  <li><a href="release_notes_3_10_0">3.10.0</a></li>
  <li><a href="release_notes_3_9_1">3.9.1</a></li>
  <li><a href="release_notes_3_9_0">3.9.0</a></li>
  <li><a href="release_notes_3_8_0">3.8.0</a></li>
  <li><a href="release_notes_3_7_0">3.7.0</a></li>
  <li><a href="release_notes_3_6_0">3.6.0</a></li>
  <li><a href="release_notes_3_5_0">3.5.0</a></li>
  <li><a href="release_notes_3_4_0">3.4.0</a></li>
  <li><a href="release_notes_3_3_0">3.3.0</a></li>
  <li><a href="release_notes_3_2_0">3.2.0</a></li>
  <li><a href="release_notes_3_1_0">3.1.0</a></li>
  <li><a href="release_notes_3_0_0">3.0.0</a></li>
  <li><a href="release_notes_1_11_0">1.11.0</a></li>
  <li><a href="release_notes_1_10_0">1.10.0</a></li>
  <li><a href="release_notes_1_9_0">1.9.0</a></li>
  <li><a href="release_notes_1_8_0">1.8.0</a></li>
  <li><a href="release_notes_1_7_0">1.7.0</a></li>
  <li><a href="release_notes_1_6_0">1.6.0</a></li>
  <li><a href="release_notes_1_5_0">1.5.0</a></li>
  <li><a href="release_notes_1_4_0">1.4.0</a></li>
  <li><a href="release_notes_1_3_0">1.3.0</a></li>
  <li class="active"><a href="release_notes_1_2_0">1.2.0</a></li>
  <li><a href="release_notes_1_1_2">1.1.2</a></li>
  <li><a href="release_notes_1_1_1">1.1.1</a></li>
  <li><a href="release_notes_1_1_0">1.1.0</a></li>
  <li><a href="release_notes_1_0_0">1.0.0</a></li>
</ul>