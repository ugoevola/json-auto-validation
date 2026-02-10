package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD

/*language=JSON*/
internal val isStringBool = """
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
  ],
  "$ERROR_MESSAGE_KEYWORD": {
    "anyOf": [{
      "type": "@{$ERROR_MESSAGE_KEYWORD}",
      "enum": "@{$ERROR_MESSAGE_KEYWORD}"
    }]
  }
}
""".trimIndent()