package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import com.networknt.schema.JsonSchemaException
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.ValidationMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.uevola.jsonautovalidation.common.exceptions.ValidationException
import org.uevola.jsonautovalidation.common.utils.ExceptionUtil

abstract class AbstractValidator {

    @Autowired
    @Qualifier("customJsonSchemaFactory")
    private lateinit var jsonSchemaFactory: JsonSchemaFactory
    fun validate(
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

}