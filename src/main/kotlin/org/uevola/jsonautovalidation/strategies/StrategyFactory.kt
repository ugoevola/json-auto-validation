package org.uevola.jsonautovalidation.strategies

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.common.enums.HttpRequestPartEnum
import org.uevola.jsonautovalidation.common.extensions.merge
import org.uevola.jsonautovalidation.common.utils.ExceptionUtil
import org.uevola.jsonautovalidation.common.utils.JsonUtil
import org.uevola.jsonautovalidation.strategies.readers.RequestReaderStrategy
import org.uevola.jsonautovalidation.strategies.schemas.JsonSchemaGeneratorStrategy
import org.uevola.jsonautovalidation.strategies.validators.ValidatorStrategy
import java.lang.reflect.Parameter
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

@Component
class StrategyFactory(
    private val validators: Set<ValidatorStrategy>,
    private val requestReaders: Set<RequestReaderStrategy>,
    private val schemaGenerators: Set<JsonSchemaGeneratorStrategy>
) {

    private val generateSchemaForParameterLambda = { parameter: Parameter -> generateSchemaForParameter(parameter) }

    fun generateSchemaFor(
        annotation: Annotation,
        property: KProperty1<out Any, *>,
        getJsonSchema: (clazz: KClass<*>) -> ObjectNode?
    ): ObjectNode? {
        return schemaGenerators
            .sortedBy { it.getOrdered() }
            .find { it.resolve(annotation) }
            ?.generate(annotation, property, getJsonSchema)
    }

    fun validate(
        request: HttpServletRequest,
        parameter: Parameter
    ) {
        val requestReader = requestReaders
            .sortedBy { it.getOrdered() }
            .find { it.resolve(parameter, request) }!!
        validate(
            requestReader.requestPart,
            requestReader.read(request),
            parameter
        )
    }

    private fun validate(
        requestPart: HttpRequestPartEnum,
        json: JsonNode,
        parameter: Parameter
    ) {
        try {
            validators
                .sortedBy { it.getOrdered() }
                .find { it.resolve(parameter.type) }!!
                .validate(json, parameter, generateSchemaForParameterLambda)
        } catch (e: HttpClientErrorException) {
            throw ExceptionUtil
                .httpClientErrorException("Error in ${requestPart.name}: ${e.message}", e.statusCode)
        }
    }

    @Cacheable
    private fun generateSchemaForParameter(
        parameter: Parameter
    ): ObjectNode? {
        val value = parameter.annotations
            .filter { annotation -> annotation.annotationClass.annotations.any { it is IsJsonValidation } }
            .map { annotation ->
                schemaGenerators
                    .sortedBy { it.getOrdered() }
                    .find { it.resolve(annotation) }!!
                    .generate(annotation, parameter)
            }
            .merge()
        if (value.isEmpty) return null
        val json = JsonUtil.newObjectNode()
        json.set<JsonNode>(parameter.name, value)
        return json
    }
}