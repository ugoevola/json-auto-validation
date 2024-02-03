package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isStringInteger = """
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