package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.FORMAT_ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.MAX_LENGTH_ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.MIN_LENGTH_ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.PATTERN_ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.TYPE_ERROR_MESSAGE_PLACEHOLDER

/*language=JSON*/
internal val isString = """
{
  "type": "string",
  "minLength": "@{minLength}",
  "maxLength": "@{maxLength}",
  "pattern": "@{pattern}",
  "format": "@{format}",
  "$ERROR_MESSAGE_KEYWORD": {
    "type": "@{$TYPE_ERROR_MESSAGE_PLACEHOLDER}",
    "minLength": "@{$MIN_LENGTH_ERROR_MESSAGE_KEYWORD}",
    "maxLength": "@{$MAX_LENGTH_ERROR_MESSAGE_KEYWORD}",
    "pattern": "@{$PATTERN_ERROR_MESSAGE_KEYWORD}",
    "format": "@{$FORMAT_ERROR_MESSAGE_KEYWORD}"
  }
}
""".trimIndent()