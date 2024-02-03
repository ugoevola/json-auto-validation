package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
val isString = """
{
  "type": "string",
  "minLength": "@{minLength}",
  "maxLength": "@{maxLength}",
  "pattern": "@{pattern}",
  "format": "@{format}"
}
""".trimIndent()