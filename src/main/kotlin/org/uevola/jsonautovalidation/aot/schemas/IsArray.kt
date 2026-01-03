package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD

/*language=JSON*/
internal val isArray = """
{
  "type": "array",
  "items": {
    "type": "@{type}"
  },
  "minItems": "@{minItems}",
  "maxItems": "@{maxItems}",
  "uniqueItems": "@{uniqueItems}",
  "$ERROR_MESSAGE_KEYWORD": "@{$ERROR_MESSAGE_KEYWORD}"
}
""".trimIndent()