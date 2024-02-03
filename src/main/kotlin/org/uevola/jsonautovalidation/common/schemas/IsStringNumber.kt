package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isStringNumber = """
{
  "string-number": {
    "minimum": "@{minimum}",
    "maximum": "@{maximum}",
    "exclusiveMaximum": "@{exclusiveMaximum}",
    "exclusiveMinimum": "@{exclusiveMinimum}",
    "multipleOf": "@{multipleOf}"
  }
}
""".trimIndent()