package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isInteger = """
{
  "type": "integer",
  "minimum": "@{minimum}",
  "maximum": "@{maximum}",
  "exclusiveMaximum": "@{exclusiveMaximum}",
  "exclusiveMinimum": "@{exclusiveMinimum}",
  "multipleOf": "@{multipleOf}"
}
""".trimIndent()