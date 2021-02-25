from sparknlp.annotator import *
from sparknlp.base import *
from common import get_spark
import os

class ReadWriteS3(unittest.TestCase):
    def setUp(self):
        self.target_file = "s3://auxdata.johnsnowlabs.com/public/test/sentences.parquet"
        os.system("aws s3 rm " + target_file)
        self.spark = get_spark()
        
    def runTest(self):
        sentences = ["I'm a repeated sentence."] * 10000
        test_dataset = self.spark.createDataFrame(sentences).toDF("text")

        document_assembler = DocumentAssembler() \
            .setInputCol("text") \
            .setOutputCol("document")
        tokenizer = Tokenizer() \
            .setInputCols(["document"]) \
            .setOutputCol("token")
        
        pipeline = Pipeline(stages=[document_assembler, tokenizer])
        pipeline.fit(test_dataset).transform(test_dataset).write(targe_file)
        spark.parquet.read(target_file).collect()


