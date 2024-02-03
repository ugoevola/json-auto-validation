package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isArray = """
{
  "type": "array",
  "items": {
    "type": "@{type}"
  },
  "minItems": "@{minItems}",
  "maxItems": "@{maxItems}",
  "uniqueItems": "@{uniqueItems}"
}
""".trimIndent()