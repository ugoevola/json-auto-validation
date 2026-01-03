package org.uevola.jsonautovalidation.aot.schemas
 
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
 
/*language=JSON*/
internal val isNumber = """
{
  "type": "number",
  "minimum": "@{minimum}",
  "maximum": "@{maximum}",
  "exclusiveMaximum": "@{exclusiveMaximum}",
  "exclusiveMinimum": "@{exclusiveMinimum}",
  "multipleOf": "@{multipleOf}",
  "$ERROR_MESSAGE_KEYWORD": "@{$ERROR_MESSAGE_KEYWORD}"
}
""".trimIndent()