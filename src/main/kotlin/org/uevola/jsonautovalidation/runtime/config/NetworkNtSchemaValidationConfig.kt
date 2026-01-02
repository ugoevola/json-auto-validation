package org.uevola.jsonautovalidation.runtime.config

import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.common.Constants.STRING_INTEGER_KEYWORD
import org.uevola.jsonautovalidation.common.Constants.STRING_NUMBER_KEYWORD
import org.uevola.jsonautovalidation.common.keywords.IsStringIntegerKeyword
import org.uevola.jsonautovalidation.common.keywords.IsStringNumberKeyword

@Configuration
internal class NetworkNtSchemaValidationConfig {

    @Bean
    fun customJsonSchemaFactory(): JsonSchemaFactory {
        val jsonSchemaVersion = JsonSchemaFactory.checkVersion(SpecVersion.VersionFlag.V202012)
        val metaSchema = jsonSchemaVersion.instance
        metaSchema.keywords[STRING_INTEGER_KEYWORD] = IsStringIntegerKeyword()
        metaSchema.keywords[STRING_NUMBER_KEYWORD] = IsStringNumberKeyword()
        return JsonSchemaFactory.builder()
            .defaultMetaSchemaURI(metaSchema.uri)
            .addMetaSchema(metaSchema)
            .build()
    }

}