package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.REQUIRED_ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.utils.JsonUtils

internal val jsonSchemaBaseTemplate = JsonUtils.objectNodeFromString("""
{
  "title": "@{title}",
  "type": "object",
  "required": "@{required}",
  "properties": "@{properties}",
  "$ERROR_MESSAGE_KEYWORD": "@{$REQUIRED_ERROR_MESSAGE_KEYWORD}"
}
""".trimIndent())