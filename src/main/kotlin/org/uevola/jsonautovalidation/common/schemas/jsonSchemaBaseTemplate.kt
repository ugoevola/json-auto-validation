package org.uevola.jsonautovalidation.common.schemas

import org.uevola.jsonautovalidation.common.utils.JsonUtil

private const val schema = "\$schema"

val jsonSchemaBaseTemplate = JsonUtil.objectNodeFromString("""
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "@{title}",
  "type": "object",
  "required": "@{required}",
  "properties": "@{properties}"
}
""".trimIndent())