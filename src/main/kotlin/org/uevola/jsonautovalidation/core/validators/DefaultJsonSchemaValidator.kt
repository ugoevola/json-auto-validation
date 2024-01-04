package org.uevola.jsonautovalidation.core.validators

import org.json.JSONObject
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.common.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.core.strategies.schemaGenerators.JsonSchemaGeneratorStrategy
import org.uevola.jsonautovalidation.common.utils.JsonUtil
import java.lang.reflect.Parameter

@Component
class DefaultJsonSchemaValidator(
    private val jsonGenerationStrategies: Set<JsonSchemaGeneratorStrategy>
) : AbstractValidator() {

    fun validate(parameter: Parameter, json: String) =
        validate(json, emptyMap(), getSchema(parameter).toString())

    @Cacheable
    fun getSchema(parameter: Parameter): JSONObject? {
        val annotations = parameter.annotations
            .filter { annotation -> annotation.annotationClass.annotations.any { it is IsJsonValidation } }
        val value = annotations.map { annotation ->
            jsonGenerationStrategies.sortedBy { it.getOrdered() }.find { it.resolve(annotation) }
                ?.generate(annotation, parameter)
        }.fold(JSONObject()) { acc, jsonObject ->
            JsonUtil.mergeJSONObject(acc, jsonObject)
            acc
        }
        if (value.isEmpty) return null
        val json = JSONObject()
        json.put(parameter.name, value)
        return json
    }

}