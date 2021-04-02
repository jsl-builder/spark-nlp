from sparknlp_jsl import start
import os
def get_spark():
    secret = os.environ['JSL_SECRET']
    _params = {'master' : 'yarn'}
    return start(secret, params=_params)
