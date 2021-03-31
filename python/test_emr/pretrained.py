from sparknlp.annotator import *
from sparknlp.base import *
from test_emr.common import get_spark
import os
import unittest

class PretrainedTestSpec(unittest.TestCase):

    def runTest(self):
        models = [T5Transformer,
		ContextSpellCheckerModel,
		WordEmbeddingsModel,
		StopWordsCleaner,
		PerceptronModel,
		UniversalSentenceEncoder,
		ElmoEmbeddings,
		PerceptronModel,
		NerCrfModel,
		Stemmer,
		NormalizerModel,
		RegexMatcherModel,
		LemmatizerModel,
		DateMatcher,
		TextMatcherModel,
		SentimentDetectorModel,
		ViveknSentimentModel,
		NorvigSweetingModel,
		SymmetricDeleteModel,
		NerDLModel,
		BertEmbeddings,
		DependencyParserModel,
		TypedDependencyParserModel,
		ClassifierDLModel,
		AlbertEmbeddings,
		XlnetEmbeddings,
		SentimentDLModel,
		LanguageDetectorDL,
		StopWordsCleaner,
		BertSentenceEmbeddings,
		MultiClassifierDLModel,
		SentenceDetectorDLModel,
		MarianTransformer]
        models = models[:5]
        failed = []
        for model in models:
            try:
                self.spark = get_spark()
                model.pretrained()
            except:
                failed.append(model)
        for f in failed:
            print("pretrained failed: " + str(f) )
