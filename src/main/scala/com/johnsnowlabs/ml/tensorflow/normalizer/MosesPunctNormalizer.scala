package com.johnsnowlabs.ml.tensorflow.normalizer

class MosesPunctNormalizer() {

  def normalize(text: String): String = {

    val EXTRA_WHITESPACE = List(
      (raw"""\r""", raw""""""),
      (raw"""\(""", raw""" ("""),
      (raw"""\)""", raw""") """),
      (raw""" +""""", raw""" """),
      (raw"""\) ([.!:?;,])""", raw""")$$1"""),
      (raw"""\( """, raw"""("""),
      (raw""" \)""", raw""")"""),
      (raw"""(\d) %""", raw"""$$1%"""),
      (raw""" :""", raw""":"""),
      (raw""" ;""""", raw""";""")
    )

    val NORMALIZE_UNICODE_IF_NOT_PENN = List(
      (raw"""`""", raw"""'"""),
      (raw"""''""", raw""" " """)
    )

    val NORMALIZE_UNICODE = List(
      (raw"""„""", raw""" " """),
      (raw"""“""", raw""" " """),
      (raw"""”""", raw""" " """),
      (raw"""–""", raw"""-"""),
      (raw"""—""", raw""" - """),
      (raw""" +""", raw""" """),
      (raw"""´""", raw"""'"""),
      (raw"""([a-zA-Z])‘([a-zA-Z])""", raw"""$$1'$$2"""),
      (raw"""([a-zA-Z])’([a-zA-Z])""", raw"""$$1'$$2"""),
      (raw"""‘""", raw"""'"""),
      (raw"""‚""", raw"""'"""),
      (raw"""’""", raw"""'"""),
      (raw"""''""", raw""" " """),
      (raw"""´´""", raw""" " """),
      (raw"""…"""", raw"""...""")
    )

    val FRENCH_QUOTES = List(
      (raw"""\u00A0«\u00A0""", raw"""""""),
      (raw"""«\u00A0""", raw"""""""),
      (raw"""«""", raw"""""""),
      (raw"""\u00A0»\u00A0""", raw"""""""),
      (raw"""\u00A0»""", raw"""""""),
      (raw"""»""", raw""""""")
    )

    val HANDLE_PSEUDO_SPACES = List(
      (raw"""\u00A0%""", raw"""%"""),
      (raw"""nº\u00A0""", raw"""nº """),
      (raw"""\u00A0:""", raw""":"""),
      (raw"""\u00A0ºC""", raw""" ºC"""),
      (raw"""\u00A0cm""", raw""" cm"""),
      (raw"""\u00A0\\?""", raw"""?"""),
      (raw"""\u00A0\\!""", raw"""!"""),
      (raw"""\u00A0;""", raw""";"""),
      (raw""""",\u00A0""", raw""""", """),
      (raw""" +""", raw""" """)
    )

    val EN_QUOTATION_FOLLOWED_BY_COMMA = List((raw""""([,.]+)""", raw"""$$1"""))

    val DE_ES_FR_QUOTATION_FOLLOWED_BY_COMMA = List(
      (raw""","""", raw"""","""),
      (raw"""(\.+)"(\s*[^<])""", raw""""$$1$$2""")
    )

    val DE_ES_CZ_CS_FR = List(
      (raw"""(\\d)\u00A0(\\d)""", raw"""$$1,$$2""")
    )

    val OTHER = List(
      (raw"""(\\d)\u00A0(\\d)""", raw"""$$1.$$2""")
    )

    val REPLACE_UNICODE_PUNCTUATION = List(
      (raw"""，""", raw""","""),
      (raw"""。\s*""", raw""". """),
      (raw"""、""", raw""","""),
      (raw"""“""", raw"""""""),
      (raw"""∶""", raw""":"""),
      (raw"""：""", raw""":"""),
      (raw"""？""", raw"""?"""),
      (raw"""《""", raw"""""""),
      (raw"""》""", raw"""""""),
      (raw"""）""", raw""")"""),
      (raw"""！""", raw"""!"""),
      (raw"""（""", raw"""("""),
      (raw"""；""", raw""";"""),
      (raw"""」""", raw"""""""),
      (raw"""「""", raw"""""""),
      (raw"""０""", raw"""0"""),
      (raw"""１""", raw"""1"""),
      (raw"""２""", raw"""2"""),
      (raw"""３""", raw"""3"""),
      (raw"""４""", raw"""4"""),
      (raw"""５""", raw"""5"""),
      (raw"""６""", raw"""6"""),
      (raw"""７""", raw"""7"""),
      (raw"""８""", raw"""8"""),
      (raw"""９""", raw"""9"""),
      (raw"""．\s*""", raw""". """),
      (raw"""～""", raw"""~"""),
      (raw"""’""", raw"""'"""),
      (raw"""…""", raw"""..."""),
      (raw"""━""", raw"""-"""),
      (raw"""〈""", raw"""<"""),
      (raw"""〉""", raw""">"""),
      (raw"""【""", raw"""["""),
      (raw"""】""", raw"""]"""),
      (raw"""％""", raw"""%""")
    )

    val substitutions = List.concat(
      EXTRA_WHITESPACE,
      NORMALIZE_UNICODE_IF_NOT_PENN,
      NORMALIZE_UNICODE,
      FRENCH_QUOTES,
      HANDLE_PSEUDO_SPACES,
      EN_QUOTATION_FOLLOWED_BY_COMMA,
      DE_ES_FR_QUOTATION_FOLLOWED_BY_COMMA,
      DE_ES_CZ_CS_FR,
      OTHER,
      REPLACE_UNICODE_PUNCTUATION)

    var acc = text

    substitutions
      .foreach {
        case (pattern, replacement) =>
          acc = s"$pattern".r.replaceAllIn(acc, replacement)
      }
    acc
  }
}
