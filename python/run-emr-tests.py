from test_emr.read_write_s3 import *
from test_emr.pretrained import *
from test_emr.basic import *
from test_emr.tf import *
#from test_emr.lm import *

# Read/Write S3.
unittest.TextTestRunner().run(ReadWriteS3TestSpec())

# Pretrained models(some of them).
unittest.TextTestRunner().run(PretrainedTestSpec())

# Basic annotators
unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

# Tensorflow models(NER & Spell).
unittest.TextTestRunner().run(TensorflowTestSpec())

# Language models like Bert.
#unittest.TextTestRunner().run(LanguageModelsTestSpec())

