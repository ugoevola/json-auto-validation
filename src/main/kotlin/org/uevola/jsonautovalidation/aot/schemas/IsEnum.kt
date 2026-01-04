package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD

/*language=JSON*/
internal val isEnum = """
{
  "type": "string",
  "enum": "@{enum}",
  "$ERROR_MESSAGE_KEYWORD": {
    "type": "@{$ERROR_MESSAGE_KEYWORD}",
    "enum": "@{$ERROR_MESSAGE_KEYWORD}"
  }
}
""".trimIndent()