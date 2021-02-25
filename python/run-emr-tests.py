from test.annotators import *
from test.misc import *
from test.base import *

# Read/Write S3.
unittest.TextTestRunner().run(ReadWriteS3())

# Pretrained models(some of them).
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

# Basic annotators
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

# Tensorflow models(NER & Spell).
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

# Language models like Bert.
#unittest.TextTestRunner().run(BasicAnnotatorsTestSpec())

