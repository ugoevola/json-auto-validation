package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
internal val isStringNumber = """
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