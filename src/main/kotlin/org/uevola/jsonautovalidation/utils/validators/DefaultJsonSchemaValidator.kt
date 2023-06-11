package org.uevola.jsonautovalidation.utils.validators

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.uevola.jsonautovalidation.utils.Utils
import org.uevola.jsonautovalidation.utils.annotations.jsonValidationAnnotation.IsJsonValidation
import org.uevola.jsonautovalidation.utils.strategies.generators.JsonSchemaGeneratorStrategy
import java.lang.reflect.Parameter

@Component
class DefaultJsonSchemaValidator: AbstractValidator() {

    @Autowired
    private lateinit var strategies: List<JsonSchemaGeneratorStrategy>

    @Autowired
    private lateinit var utils: Utils

    fun validate(parameter: Parameter, json: String) =
        validate(json, emptyMap(), getSchema(parameter).toString())

    @Cacheable
    fun getSchema(parameter: Parameter): JSONObject? {
        val annotations = parameter.annotations
            .filter { annotation -> annotation.annotationClass.annotations.any { it is IsJsonValidation } }
        val value = annotations.map { annotation ->
            strategies.sortedBy { it.getOrdered() }.find { it.resolve(annotation) }
                ?.generate(annotation, parameter)
        }.fold(JSONObject()) { acc, jsonObject ->
            utils.mergeJSONObject(acc, jsonObject)
            acc
        }
        if (value.isEmpty) return null
        val json = JSONObject()
        json.put(parameter.name, value)
        return json
    }

}