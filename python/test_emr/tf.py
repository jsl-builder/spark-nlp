from test_emr.common import get_spark
from sparknlp.annotator import *
from sparknlp.base import *
import os
import unittest

class TensorflowTestSpec(unittest.TestCase):

    def setUp(self):
        self.target_file = "s3://auxdata.johnsnowlabs.com/public/test/annotations.parquet"
        os.system("aws s3 rm " + self.target_file + " --recursive")
        self.spark = get_spark()
        
    def runTest(self):
        sentences = [{"text": "Peter Parker works for Intel Corp. in Santa Clara."}] * 10000
        test_dataset = self.spark.createDataFrame(sentences).toDF("text")
   
        documentAssembler = DocumentAssembler()\
            .setInputCol("text")\
            .setOutputCol("document")

        tokenizer = Tokenizer() \
            .setInputCols(["document"]) \
            .setOutputCol("token")

	# ner_dl model is trained with glove_100d. So we use the same embeddings in the pipeline
        glove_embeddings = WordEmbeddingsModel.pretrained('glove_100d').\
          setInputCols(["document", 'token']).\
          setOutputCol("embeddings")

        ner = NerDLModel.pretrained("ner_dl", 'en') \
          .setInputCols(["document", "token", "embeddings"]) \
          .setOutputCol("ner")

        nlpPipeline = Pipeline(stages=[
            documentAssembler, 
            tokenizer,
            glove_embeddings,
            ner
            ])
        pipelineModel = nlpPipeline.fit(test_dataset)
        pipelineModel.transform(test_dataset).write.parquet(self.target_file)

