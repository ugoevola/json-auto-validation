package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.EXCLUSIVE_MAXIMUM_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.EXCLUSIVE_MINIMUM_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.MAXIMUM_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.MINIMUM_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.MULTIPLE_OF_ERROR_MESSAGE_PLACEHOLDER
import org.uevola.jsonautovalidation.common.Constants.TYPE_ERROR_MESSAGE_PLACEHOLDER

/*language=JSON*/
internal val isInteger = """
{
  "type": "integer",
  "minimum": "@{minimum}",
  "maximum": "@{maximum}",
  "exclusiveMinimum": "@{exclusiveMinimum}",
  "exclusiveMaximum": "@{exclusiveMaximum}",
  "multipleOf": "@{multipleOf}",
  "$ERROR_MESSAGE_KEYWORD": {
    "type": "@{$TYPE_ERROR_MESSAGE_PLACEHOLDER}",
    "minimum": "@{$MINIMUM_ERROR_MESSAGE_PLACEHOLDER}",
    "maximum": "@{$MAXIMUM_ERROR_MESSAGE_PLACEHOLDER}",
    "exclusiveMaximum": "@{$EXCLUSIVE_MINIMUM_ERROR_MESSAGE_PLACEHOLDER}",
    "exclusiveMinimum": "@{$EXCLUSIVE_MAXIMUM_ERROR_MESSAGE_PLACEHOLDER}",
    "multipleOf": "@{$MULTIPLE_OF_ERROR_MESSAGE_PLACEHOLDER}"
  }
}
""".trimIndent()