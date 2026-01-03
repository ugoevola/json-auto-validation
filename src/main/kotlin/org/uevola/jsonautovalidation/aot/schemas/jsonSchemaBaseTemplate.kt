package org.uevola.jsonautovalidation.aot.schemas

import org.uevola.jsonautovalidation.common.utils.JsonUtils

internal val jsonSchemaBaseTemplate = JsonUtils.objectNodeFromString("""
{
  "title": "@{title}",
  "type": "object",
  "required": "@{required}",
  "properties": "@{properties}"
}
""".trimIndent())