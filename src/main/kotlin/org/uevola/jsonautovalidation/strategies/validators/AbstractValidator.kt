package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaException
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.ValidationMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.common.exceptions.ValidationException
import org.uevola.jsonautovalidation.common.utils.ExceptionUtil
import java.lang.reflect.Field
import java.lang.reflect.Parameter

abstract class AbstractValidator {

    @Autowired
    @Qualifier("customJsonSchemaFactory")
    private lateinit var jsonSchemaFactory: JsonSchemaFactory
    protected fun validate(
        json: JsonNode,
        schema: JsonNode,
        customMessages: Map<String, String>
    ) {
        val errors: Set<ValidationMessage>
        try {
            val jsonSchema = jsonSchemaFactory.getSchema(schema)
            errors = jsonSchema.validate(json)
        } catch (e: JsonSchemaException) {
            val errorMessage = "Error in validation schema : ".plus(e.message.toString())
            throw ExceptionUtil.httpServerErrorException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: Exception) {
            val errorMessage = "Error when opening or validating the file : ".plus(e.message.toString())
            throw ExceptionUtil.httpServerErrorException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        verifyError(errors, customMessages)
    }

    private fun verifyError(
        errors: Set<ValidationMessage>,
        customMessages: Map<String, String>
    ) {
        if (errors.isNotEmpty()) {
            val message = errors.filter { !customMessages.containsKey(it.path.drop(2)) }
                .map { it.toString() }
                .plus(customMessages.values)
                .joinToString(", ")
            throw ValidationException(message, HttpStatus.BAD_REQUEST, errors)
        }
    }

    @Cacheable
    protected fun getCustomMessage(parameter: Parameter) = parameter.type.declaredFields
        .associate { it.name to getFieldMessage(it) }
        .filter { it.value.isNotEmpty() }

    private fun getFieldMessage(field: Field) = field.annotations
        .filter { annotation ->
            annotation.annotationClass.annotations.any { it is IsJsonValidation }
        }
        .map { it.annotationClass.java.getMethod("message").invoke(it) as String }
        .filter { it.isNotEmpty() }
        .joinToString(", ")

}