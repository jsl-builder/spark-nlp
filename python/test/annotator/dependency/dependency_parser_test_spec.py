class DependencyParserConllUTestSpec(unittest.TestCase):

    def setUp(self):
        self.data = SparkContextForTest.spark \
            .createDataFrame([["I saw a girl with a telescope"]]).toDF("text")
        self.corpus = os.getcwd() + "/../src/test/resources/anc-pos-corpus-small/"
        self.conllu = os.getcwd() + "/../src/test/resources/parser/unlabeled/conll-u/train_small.conllu.txt"
        from sparknlp.training import POS
        self.train_pos = POS().readDataset(SparkContextForTest.spark,
                                           os.getcwd() + "/../src/test/resources/anc-pos-corpus-small/test-training.txt",
                                           delimiter="|", outputPosCol="tags", outputDocumentCol="document",
                                           outputTextCol="text")

    def runTest(self):
        document_assembler = DocumentAssembler() \
            .setInputCol("text") \
            .setOutputCol("document")

        sentence_detector = SentenceDetector() \
            .setInputCols(["document"]) \
            .setOutputCol("sentence")

        tokenizer = Tokenizer() \
            .setInputCols(["sentence"]) \
            .setOutputCol("token")

        pos_tagger = PerceptronApproach() \
            .setInputCols(["token", "sentence"]) \
            .setOutputCol("pos") \
            .setIterations(1) \
            .fit(self.train_pos)

        dependency_parser = DependencyParserApproach() \
            .setInputCols(["sentence", "pos", "token"]) \
            .setOutputCol("dependency") \
            .setConllU(self.conllu) \
            .setNumberOfIterations(10)

        assembled = document_assembler.transform(self.data)
        sentenced = sentence_detector.transform(assembled)
        tokenized = tokenizer.fit(sentenced).transform(sentenced)
        pos_tagged = pos_tagger.transform(tokenized)
        dependency_parsed = dependency_parser.fit(pos_tagged).transform(pos_tagged)
        dependency_parsed.show()

class DependencyParserTreeBankTestSpec(unittest.TestCase):

    def setUp(self):
        self.data = SparkContextForTest.spark \
            .createDataFrame([["I saw a girl with a telescope"]]).toDF("text")
        self.corpus = os.getcwd() + "/../src/test/resources/anc-pos-corpus-small/"
        self.dependency_treebank = os.getcwd() + "/../src/test/resources/parser/unlabeled/dependency_treebank"
        from sparknlp.training import POS
        self.train_pos = POS().readDataset(SparkContextForTest.spark,
                                           os.getcwd() + "/../src/test/resources/anc-pos-corpus-small/test-training.txt",
                                           delimiter="|", outputPosCol="tags", outputDocumentCol="document",
                                           outputTextCol="text")

    def runTest(self):
        document_assembler = DocumentAssembler() \
            .setInputCol("text") \
            .setOutputCol("document")

        sentence_detector = SentenceDetector() \
            .setInputCols(["document"]) \
            .setOutputCol("sentence")

        tokenizer = Tokenizer() \
            .setInputCols(["sentence"]) \
            .setOutputCol("token")

        pos_tagger = PerceptronApproach() \
            .setInputCols(["token", "sentence"]) \
            .setOutputCol("pos") \
            .setIterations(1) \
            .fit(self.train_pos)

        dependency_parser = DependencyParserApproach() \
            .setInputCols(["sentence", "pos", "token"]) \
            .setOutputCol("dependency") \
            .setDependencyTreeBank(self.dependency_treebank) \
            .setNumberOfIterations(10)

        assembled = document_assembler.transform(self.data)
        sentenced = sentence_detector.transform(assembled)
        tokenized = tokenizer.fit(sentenced).transform(sentenced)
        pos_tagged = pos_tagger.transform(tokenized)
        dependency_parsed = dependency_parser.fit(pos_tagged).transform(pos_tagged)
        dependency_parsed.show()

