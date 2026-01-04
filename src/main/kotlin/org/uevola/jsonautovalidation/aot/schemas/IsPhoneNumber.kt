package org.uevola.jsonautovalidation.aot.schemas
 
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
 
/*language=JSON*/
internal val isPhoneNumber = """
{
  "type": "string",
  "pattern": "(\\+[1-9]\\d{0,2}[-.\\s]?)?(\\((\\d{1,3})\\)[-.\\s]?)?(\\d{1,4}[-.\\s]?){1,4}(\\d{1,14})",
  "$ERROR_MESSAGE_KEYWORD": {
    "type": "@{$ERROR_MESSAGE_KEYWORD}",
    "pattern": "@{$ERROR_MESSAGE_KEYWORD}"
  }
}
""".trimIndent()