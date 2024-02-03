package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isStringBool = """
{
  "anyOf": [
    {
      "type": "boolean"
    },
    {
      "type": "string",
      "enum": [
        "true",
        "True",
        "TRUE",
        "false",
        "False",
        "FALSE"
      ]
    }
  ]
}
""".trimIndent()