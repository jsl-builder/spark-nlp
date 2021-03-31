from test_emr.common import get_spark
from sparknlp.base import *
from sparknlp.annotator import *
import unittest
import os
class BasicAnnotatorsTestSpec(unittest.TestCase):

    def setUp(self):
        self.target_file = "s3://auxdata.johnsnowlabs.com/public/test/sentences.parquet"
        os.system("aws s3 rm " + self.target_file + " --recursive")
        self.spark = get_spark()
        
    def runTest(self):
        sentences = [{"text": "I'm a repeated sentence."}] * 10000
        test_dataset = self.spark.createDataFrame(sentences).toDF("text")

        document_assembler = DocumentAssembler() \
            .setInputCol("text") \
            .setOutputCol("document")
        tokenizer = Tokenizer() \
            .setInputCols(["document"]) \
            .setOutputCol("token") \
            .setExceptions(["New York"]) \
            .addInfixPattern("(%\\d+)")
        stemmer = Stemmer() \
            .setInputCols(["token"]) \
            .setOutputCol("stem")
        normalizer = Normalizer() \
            .setInputCols(["stem"]) \
            .setOutputCol("normalize")
        token_assembler = TokenAssembler() \
            .setInputCols(["document", "normalize"]) \
            .setOutputCol("assembled")
        finisher = Finisher() \
            .setInputCols(["assembled"]) \
            .setOutputCols(["reassembled_view"]) \
            .setCleanAnnotations(True)
        assembled = document_assembler.transform(test_dataset)
        tokenized = tokenizer.fit(assembled).transform(assembled)
        stemmed = stemmer.transform(tokenized)
        normalized = normalizer.fit(stemmed).transform(stemmed)
        reassembled = token_assembler.transform(normalized)
        finisher.transform(reassembled).show()
