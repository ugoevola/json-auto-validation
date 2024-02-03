package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isNumber = """
{
  "type": "number",
  "minimum": "@{minimum}",
  "maximum": "@{maximum}",
  "exclusiveMaximum": "@{exclusiveMaximum}",
  "exclusiveMinimum": "@{exclusiveMinimum}",
  "multipleOf": "@{multipleOf}"
}
""".trimIndent()