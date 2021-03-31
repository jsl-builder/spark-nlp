from sparknlp.annotator import *
from sparknlp.base import *
from test_emr_internals.common import get_spark
import os
import unittest

class ContextSpellChecker(unittest.TestCase):

    def runTest(self):
        get_spark()
        documentAssembler = DocumentAssembler()\
        .setInputCol("text")\
        .setOutputCol("document")

        tokenizer = RecursiveTokenizer()\
        .setInputCols(["document"])\
        .setOutputCol("token")\
        .setPrefixes(["\"", "(", "[", "\n"])\
        .setSuffixes([".", ",", "?", ")","!", "'s"])

        spellModel = ContextSpellCheckerModel\
        .pretrained('spellcheck_clinical', 'en', 'clinical/models')\
        .setInputCols("token")\
        .setOutputCol("checked")

        finisher = Finisher()\
        .setInputCols("checked")

        pipeline = Pipeline(
        stages = [
        documentAssembler,
        tokenizer,
        spellModel,
        finisher
        ])

        empty_ds = spark.createDataFrame([[""]]).toDF("text")
        fpipeline = pipeline.fit(empty_ds)

        example = ["Witth the hell of phisical terapy the patient was imbulated and on posoperative, the impatient tolerating a post curgical soft diet.",
        "With paint wel controlled on orall pain medications, she was discharged too reihabilitation facilitay.",
        "She is to also call the ofice if she has any ever greater than 101, or leeding form the surgical wounds.",
        "Abdomen is sort, nontender, and nonintended.",
        "Patient not showing pain or any wealth problems.",
        "No cute distress"    
        ]
        example_df = spark.createDataFrame([example]).toDF("text")
        fpipeline.transform(example_df)
        example_df.collect()
