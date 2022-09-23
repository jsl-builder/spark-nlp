{%- capture title -%}
ATEst3Tabs custom
{%- endcapture -%}




{%- capture model_description -%}
testing description
[DocumentAssembler](/docs/en/annotators#documentassembler),
[NerConverter](/docs/en/annotators#nerconverter)
and [WordEmbeddingsModel](/docs/en/annotators#wordembeddings).
The result is an assertion status annotation for each recognized entity.
Possible values include `“present”, “absent”, “hypothetical”, “conditional”, “associated_with_other_person”` etc.

For pretrained models please see the
[Models Hub](https://nlp.johnsnowlabs.com/models?task=Assertion+Status) for available models.
{%- endcapture -%}

{%- capture model_input_anno -%}
TEST
{%- endcapture -%}

{%- capture model_output_anno -%}
TESTING
{%- endcapture -%}



{%- capture model_python_medical -%}
PYTHON MEDICAL MODEL
{%- endcapture -%}

{%- capture model_python_finance -%}
PYTHON FINANCE MODEL
{%- endcapture -%}

{%- capture model_python_legal -%}
PYTHON LEGAL MODEL
{%- endcapture -%}


{%- capture model_scala_medical -%}
SCALA MEDICAL MODEL
{%- endcapture -%}

{%- capture model_scala_finance -%}
SCALA FINANCE MODEL
{%- endcapture -%}

{%- capture model_scala_legal -%}
SCALA LEGAL MODEL
{%- endcapture -%}





{%- capture model_api_link -%}
[Test1](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/assertion/dl/AssertionDLModel)
{%- endcapture -%}

{%- capture approach_description -%}
testing descr 2
[Models Hub](https://nlp.johnsnowlabs.com/models?task=Assertion+Status) for available models.
{%- endcapture -%}

{%- capture approach_input_anno -%}
TEST 2
{%- endcapture -%}

{%- capture approach_output_anno -%}
TEST 22
{%- endcapture -%}




{%- capture approach_python_medical -%}
PYTHON MEDICAL APPROACH
{%- endcapture -%}

{%- capture approach_python_finance -%}
PYTHON FINANCE APPROACH
{%- endcapture -%}

{%- capture approach_python_legal -%}
PYTHON LEGAL APPROACH
{%- endcapture -%}


{%- capture approach_scala_medical -%}
SCALA MEDICAL APPROACH
{%- endcapture -%}

{%- capture approach_scala_finance -%}
SCALA FINANCE APPROACH
{%- endcapture -%}

{%- capture approach_scala_legal -%}
SCALA LEGAL APPROACH
{%- endcapture -%}





{%- capture approach_api_link -%}
[TEst 3](https://nlp.johnsnowlabs.com/licensed/api/com/johnsnowlabs/nlp/annotators/assertion/dl/AssertionDLApproach)
{%- endcapture -%}


{% include templates/licensed_approach_model_medical_fin_leg_template.md
title=title
model=model
approach=approach
model_description=model_description
model_input_anno=model_input_anno
model_output_anno=model_output_anno
model_python_finance=model_python_finance
model_python_legal=model_python_legal
model_scala_medical=model_scala_medical
model_scala_finance=model_scala_finance
model_scala_legal=model_scala_legal
model_api_link=model_api_link
approach_description=approach_description
approach_input_anno=approach_input_anno
approach_output_anno=approach_output_anno
approach_python_medical=approach_python_medical
approach_python_finance=approach_python_finance
approach_python_legal=approach_python_legal
approach_scala_medical=approach_scala_medical
approach_scala_legal=approach_scala_legal
approach_api_link=approach_api_link
%}
