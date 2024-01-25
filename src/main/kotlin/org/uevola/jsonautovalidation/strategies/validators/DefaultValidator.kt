package org.uevola.jsonautovalidation.strategies.validators

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.common.extensions.merge
import org.uevola.jsonautovalidation.common.utils.JsonUtil.newObjectNode
import org.uevola.jsonautovalidation.strategies.StrategyFactory
import java.lang.reflect.Parameter

@Component
class DefaultValidator(
    private val strategyFactory: StrategyFactory
): ValidatorStrategy<Any>, AbstractValidator() {

    override fun getOrdered() = Int.MAX_VALUE

    override fun resolve(parameterType: Class<*>) = true

    override fun validate(
        json: JsonNode,
        parameter: Parameter
    ) {
        val schema = getSchema(parameter)
        if (schema != null) {
            validate(json, schema, emptyMap())
        }
    }

    @Cacheable
    fun getSchema(
        parameter: Parameter
    ): JsonNode? {
        val value = parameter.annotations
            .filter { annotation -> annotation.annotationClass.annotations.any { it is IsJsonValidation } }
            .map { strategyFactory.generateSchemaFor(it, parameter) }
            .merge()
        if (value.isEmpty) return null
        val json = newObjectNode()
        json.set<JsonNode>(parameter.name, value)
        return json
    }
}