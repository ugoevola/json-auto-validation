package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
internal val isInteger = """
{
  "type": "integer",
  "minimum": "@{minimum}",
  "maximum": "@{maximum}",
  "exclusiveMaximum": "@{exclusiveMaximum}",
  "exclusiveMinimum": "@{exclusiveMinimum}",
  "multipleOf": "@{multipleOf}"
}
""".trimIndent()