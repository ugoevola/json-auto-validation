package org.uevola.jsonautovalidation.common.schemas

import org.uevola.jsonautovalidation.common.utils.JsonUtils

private const val schema = $$"$schema"

internal val jsonSchemaBaseTemplate = JsonUtils.objectNodeFromString("""
{
  "$schema": "https://json-schema.org/draft/2020-12/schema",
  "title": "@{title}",
  "type": "object",
  "required": "@{required}",
  "properties": "@{properties}"
}
""".trimIndent())