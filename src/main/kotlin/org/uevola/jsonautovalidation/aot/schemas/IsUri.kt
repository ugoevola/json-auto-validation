package org.uevola.jsonautovalidation.aot.schemas
 
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
 
/*language=JSON*/
internal val isUri = """
{
  "type": "string",
  "format": "uri",
  "$ERROR_MESSAGE_KEYWORD": {
    "type": "@{$ERROR_MESSAGE_KEYWORD}",
    "format": "@{$ERROR_MESSAGE_KEYWORD}"
  }
}
""".trimIndent()