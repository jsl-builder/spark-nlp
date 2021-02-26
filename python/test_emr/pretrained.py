from sparknlp.annotator import *
from sparknlp.base import *
from test_emr.common import get_spark
import os
import unittest

class Pretrained(unittest.TestCase):

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
        for model in models:
            try:
                model.pretrained()
            except:
                print("pretrained failed: " + str(model) )
