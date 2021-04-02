from pyspark.sql import SparkSession

def get_spark():
    spark = SparkSession.builder \
        .master("yarn") \
        .config("spark.packages", 'com.johnsnowlabs.nlp:spark-nlp_2.12:3.0.1') \
        .config("spark.driver.memory", "12G") \
        .config("spark.driver.maxResultSize", "2G") \
        .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer") \
        .config("spark.kryoserializer.buffer.max", "500m") \
        .getOrCreate()
    return spark

