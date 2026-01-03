package org.uevola.jsonautovalidation.aot.schemas
 
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
 
/*language=JSON*/
internal val isIriReference = """
{
  "type": "string",
  "format": "iri-reference",
  "$ERROR_MESSAGE_KEYWORD": "@{$ERROR_MESSAGE_KEYWORD}"
}
""".trimIndent()