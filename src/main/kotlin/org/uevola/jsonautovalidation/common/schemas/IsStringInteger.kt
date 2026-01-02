package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
internal val isStringInteger = """
{
  "string-integer": {
    "minimum": "@{minimum}",
    "maximum": "@{maximum}",
    "exclusiveMaximum": "@{exclusiveMaximum}",
    "exclusiveMinimum": "@{exclusiveMinimum}",
    "multipleOf": "@{multipleOf}"
  }
}
""".trimIndent()