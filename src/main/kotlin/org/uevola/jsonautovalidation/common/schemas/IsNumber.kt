package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
internal val isNumber = """
{
  "type": "number",
  "minimum": "@{minimum}",
  "maximum": "@{maximum}",
  "exclusiveMaximum": "@{exclusiveMaximum}",
  "exclusiveMinimum": "@{exclusiveMinimum}",
  "multipleOf": "@{multipleOf}"
}
""".trimIndent()