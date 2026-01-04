package org.uevola.jsonautovalidation.aot.schemas
 
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
 
/*language=JSON*/
internal val isEqualTo = """
{
  "const": "@{value}",
  "$ERROR_MESSAGE_KEYWORD": {
    "const": "@{$ERROR_MESSAGE_KEYWORD}"
  }
}
""".trimIndent()