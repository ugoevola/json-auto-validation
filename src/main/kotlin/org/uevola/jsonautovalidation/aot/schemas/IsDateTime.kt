package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD

/*language=JSON*/
internal val isDateTime = """
{
  "type": "string",
  "format": "date-time",
  "$ERROR_MESSAGE_KEYWORD": "@{$ERROR_MESSAGE_KEYWORD}"
}
""".trimIndent()