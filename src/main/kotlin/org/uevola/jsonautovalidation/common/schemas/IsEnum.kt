package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
internal val isEnum = """
{
  "type": "string",
  "enum": "@{enum}"
}
""".trimIndent()