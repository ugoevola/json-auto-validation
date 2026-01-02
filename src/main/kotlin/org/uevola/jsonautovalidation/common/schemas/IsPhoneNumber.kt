package org.uevola.jsonautovalidation.common.schemas

/*language=JSON*/
internal val isPhoneNumber = """
{
  "type": "string",
  "pattern": "(\\+[1-9]\\d{0,2}[-.\\s]?)?(\\((\\d{1,3})\\)[-.\\s]?)?(\\d{1,4}[-.\\s]?){1,4}(\\d{1,14})"
}
""".trimIndent()