package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD

/*language=JSON*/
internal val isNotEmpty = """
{
  "type": "string",
  "minLength": 1,
  "$ERROR_MESSAGE_KEYWORD": {
    "type": "@{$ERROR_MESSAGE_KEYWORD}",
    "minLength": "@{$ERROR_MESSAGE_KEYWORD}"
  }
}
""".trimIndent()