package org.uevola.jsonautovalidation.runtime.common.strategies.validators

import com.networknt.schema.Error
import com.networknt.schema.SchemaException
import com.networknt.schema.SchemaRegistry
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.uevola.jsonautovalidation.common.exceptions.ValidationException
import org.uevola.jsonautovalidation.runtime.common.utils.ExceptionUtils
import tools.jackson.databind.JsonNode

abstract class AbstractValidator {

    @Autowired
    @Qualifier("customSchemaRegistry")
    private lateinit var schemaRegistry: SchemaRegistry
    protected fun baseValidate(
        json: JsonNode,
        schema: JsonNode
    ) {
        val errors: List<Error>
        try {
            val jsonSchema = schemaRegistry.getSchema(schema)
            errors = jsonSchema.validate(json)
        } catch (e: SchemaException) {
            val errorMessage = "Error in validation schema : ".plus(e.message.toString())
            throw ExceptionUtils.httpServerErrorException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        } catch (e: Exception) {
            val errorMessage = "Error when opening or validating the file : ".plus(e.message.toString())
            throw ExceptionUtils.httpServerErrorException(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR)
        }
        verifyError(errors)
    }

    private fun verifyError(errors: List<Error>) {
        if (errors.isNotEmpty()) {
            throw ValidationException(errors.map{ it.message }.toSet().joinToString(" "), HttpStatus.BAD_REQUEST)
        }
    }

}