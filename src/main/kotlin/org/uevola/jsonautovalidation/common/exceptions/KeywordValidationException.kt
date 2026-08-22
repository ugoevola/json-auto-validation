package org.uevola.jsonautovalidation.common.exceptions

/**
 * Thrown when a value does not satisfy a constraint of a custom keyword.
 *
 * @param keyword the name of the violated constraint (type, minimum, maximum...),
 * used to pick the matching error message declared in the schema.
 */
class KeywordValidationException(val keyword: String) : RuntimeException()
