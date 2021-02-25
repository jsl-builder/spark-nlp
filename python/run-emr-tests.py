from test_emr.read_write_s3 import *
from test_emr.pretrained import *
# Read/Write S3.
unittest.TextTestRunner().run(ReadWriteS3())

# Pretrained models(some of them).
unittest.TextTestRunner().run(Pretrained())

# Basic annotators
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

# Tensorflow models(NER & Spell).
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

# Language models like Bert.
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

