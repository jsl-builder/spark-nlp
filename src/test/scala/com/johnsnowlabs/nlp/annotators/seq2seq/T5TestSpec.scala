package com.johnsnowlabs.nlp.annotators.seq2seq

import com.johnsnowlabs.ml.tensorflow.TensorflowWrapper
import com.johnsnowlabs.ml.tensorflow.TensorflowT5
import com.johnsnowlabs.ml.tensorflow.sentencepiece.SentencePieceWrapper
import com.johnsnowlabs.nlp.annotator.SentenceDetectorDLModel
import com.johnsnowlabs.nlp.base.DocumentAssembler
import com.johnsnowlabs.nlp.util.io.ResourceHelper
import org.apache.spark.ml.Pipeline
import org.scalatest._

class T5TestSpec extends FlatSpec {
  "t5-small" should "run SparkNLP pipeline" in {
    val testData = ResourceHelper.spark.createDataFrame(Seq(

      (1, "Preheat the oven to 220°C/ fan200°C/gas 7. Trim the lamb fillet of fat and cut into slices the thickness" +
        " of a chop. Cut the kidneys in half and snip out the white core. Melt a knob of dripping or 2 tablespoons " +
        "of vegetable oil in a heavy large pan. Fry the lamb fillet in batches for 3-4 minutes, turning once, until " +
        "browned. Set aside. Fry the kidneys and cook for 1-2 minutes, turning once, until browned. Set aside." +
        "Wipe the pan with kitchen paper, then add the butter. Add the onions and fry for about 10 minutes until " +
        "softened. Sprinkle in the flour and stir well for 1 minute. Gradually pour in the stock, stirring all the " +
        "time to avoid lumps. Add the herbs. Stir the lamb and kidneys into the onions. Season well. Transfer to a" +
        " large 2.5-litre casserole. Slice the peeled potatoes thinly and arrange on top in overlapping rows. Brush " +
        "with melted butter and season. Cover and bake for 30 minutes. Reduce the oven temperature to 160°C" +
        "/fan140°C/gas 3 and cook for a further 2 hours. Then increase the oven temperature to 200°C/ fan180°C/gas 6," +
        " uncover, and brush the potatoes with more butter. Cook uncovered for 15-20 minutes, or until golden."),
      (1, "Donald John Trump (born June 14, 1946) is the 45th and current president of the United States. Before " +
        "entering politics, he was a businessman and television personality. Born and raised in Queens, New York " +
        "City, Trump attended Fordham University for two years and received a bachelor's degree in economics from the " +
        "Wharton School of the University of Pennsylvania. He became president of his father Fred Trump's real " +
        "estate business in 1971, renamed it The Trump Organization, and expanded its operations to building or " +
        "renovating skyscrapers, hotels, casinos, and golf courses. Trump later started various side ventures," +
        " mostly by licensing his name. Trump and his businesses have been involved in more than 4,000 state and" +
        " federal legal actions, including six bankruptcies. He owned the Miss Universe brand of beauty pageants " +
        "from 1996 to 2015, and produced and hosted the reality television series The Apprentice from 2004 to 2015.")
    )).toDF("id", "text")

    val documentAssembler = new DocumentAssembler()
      .setInputCol("text")
      .setOutputCol("documents")

    val t5 = T5Transformer.pretrained()
      .setTask("summarize:")
      .setInputCols(Array("documents"))
      .setOutputCol("summaries")

    val pipeline = new Pipeline().setStages(Array(documentAssembler, t5))

    val model = pipeline.fit(testData)
    val results = model.transform(testData)

    results.select("summaries.result").show(truncate = false)
  }

  "google/t5-small-ssm-nq " should "run SparkNLP pipeline" in {
    val testData = ResourceHelper.spark.createDataFrame(Seq(

      (1, "Which is the capital of France? Who was the first president of USA?"),
      (1, "Which is the capital of Bulgaria ?"),
      (2, "Who is Donald Trump?")

    )).toDF("id", "text")

    val documentAssembler = new DocumentAssembler()
      .setInputCol("text")
      .setOutputCol("documents")

    val sentenceDetector = SentenceDetectorDLModel
      .pretrained()
      .setInputCols(Array("documents"))
      .setOutputCol("questions")

    val t5 = T5Transformer.pretrained("t5-small-ssm-nq")
      .setInputCols(Array("questions"))
      .setOutputCol("answers")

    val pipeline = new Pipeline().setStages(Array(documentAssembler, sentenceDetector, t5))

    val model = pipeline.fit(testData)
    val results = model.transform(testData)

    results.select("questions.result", "answers.result").show(truncate = false)
  }


  "T5Transformer" should "load and save models" ignore {
    val google_t5_small_ssm_nq = T5Transformer.loadSavedModel("/models/t5/google_t5_small_ssm_nq/tf/combined", ResourceHelper.spark)
    google_t5_small_ssm_nq.write.overwrite().save("/models/sparknlp/google_t5_small_ssm_nq")

    val t5_small = T5Transformer.loadSavedModel("/models/t5/t5_small/tf/combined", ResourceHelper.spark)
    t5_small.write.overwrite().save("/models/sparknlp/t5_small")

  }

  "T5 TF" should "process text" ignore {
    val texts = Array("When was America discovered?", "Which was the first president of USA?")
    val tfw = TensorflowWrapper.read("/models/t5/t5_small/tf/combined", zipped = false, useBundle = true, tags = Array("serve"))
    val spp = SentencePieceWrapper.read("/models/t5/t5_small/tf/combined/assets/spiece.model")
    val t5tf = new TensorflowT5(tfw, spp)
    t5tf.process(texts).foreach(x => println(x))
  }
}
