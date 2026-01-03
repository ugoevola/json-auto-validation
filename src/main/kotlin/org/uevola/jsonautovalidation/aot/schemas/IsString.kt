package org.uevola.jsonautovalidation.aot.schemas
 
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
 
/*language=JSON*/
internal val isString = """
{
  "type": "string",
  "minLength": "@{minLength}",
  "maxLength": "@{maxLength}",
  "pattern": "@{pattern}",
  "format": "@{format}",
  "$ERROR_MESSAGE_KEYWORD": "@{$ERROR_MESSAGE_KEYWORD}"
}
""".trimIndent()