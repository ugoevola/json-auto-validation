package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.MAX_ITEMS_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.MIN_ITEMS_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.TYPE_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.UNIQUE_ITEMS_ERROR_MESSAGE_PLACEHOLDER

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
  "$ERROR_MESSAGE_KEYWORD": {
    "items": {
      "type": "@{$TYPE_ERROR_MESSAGE_PLACEHOLDER}"
    },
    "minItems": "@{$MIN_ITEMS_ERROR_MESSAGE_PLACEHOLDER}",
    "maxItems": "@{$MAX_ITEMS_ERROR_MESSAGE_PLACEHOLDER}",
    "uniqueItems": "@{$UNIQUE_ITEMS_ERROR_MESSAGE_PLACEHOLDER}"
  }
}
""".trimIndent()