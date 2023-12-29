package org.uevola.jsonautovalidation.core.validators

import org.uevola.jsonautovalidation.utils.exceptions.ValidationException

/**
 * Used to validate json that must be deserialized into T type
 */
interface IJsonSchemaValidator<T> {

    /**
     * validate json data with json schema of T
     * if the validation failed, a validation exception that contains the custom messages is thrown
     *
     * @param json the json that must be validated
     * @param customMessages
     * @throws ValidationException
     */
    fun validate(json: String, customMessages: Map<String, String>): Unit?
}