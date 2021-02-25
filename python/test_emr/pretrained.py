from sparknlp.annotator import *
from sparknlp.base import *
from test_emr.common import get_spark
import os
import unittest

class Pretrained(unittest.TestCase):
    def setUp(self):
        pass
    def runTest(self):
        T5Transformer.pretrained()
        ContextSpellCheckerModel.pretrained()
        WordEmbeddingsModel.pretrained()
        StopWordsCleaner.pretrained()
        PerceptronModel.pretrained()
        UniversalSentenceEncoder.pretrained()
        ElmoEmbeddings.pretrained()
        PerceptronModel.pretrained()
        NerCrfModel.pretrained()
        Stemmer.pretrained()
        NormalizerModel.pretrained()
        RegexMatcherModel.pretrained()
        LemmatizerModel.pretrained()
        DateMatcher.pretrained()
        TextMatcherModel.pretrained()
        SentimentDetectorModel.pretrained()
        ViveknSentimentModel.pretrained()
        NorvigSweetingModel.pretrained()
        SymmetricDeleteModel.pretrained()
        NerDLModel.pretrained()
        BertEmbeddings.pretrained()
        DependencyParserModel.pretrained()
        TypedDependencyParserModel.pretrained()
        ClassifierDLModel.pretrained()
        AlbertEmbeddings.pretrained()
        XlnetEmbeddings.pretrained()
        SentimentDLModel.pretrained()
        LanguageDetectorDL.pretrained()
        StopWordsCleaner.pretrained()
        BertSentenceEmbeddings.pretrained()
        MultiClassifierDLModel.pretrained()
        SentenceDetectorDLModel.pretrained()
        MarianTransformer.pretrained()
