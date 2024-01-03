package org.uevola.jsonautovalidation.configuration

import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.utils.Util.STRING_INTEGER_KEYWORD
import org.uevola.jsonautovalidation.utils.Util.STRING_NUMBER_KEYWORD
import org.uevola.jsonautovalidation.utils.keywords.IsStringIntegerKeyword
import org.uevola.jsonautovalidation.utils.keywords.IsStringNumberKeyword

@Configuration
open class NetworkNtConfig {

    @Bean
    open fun customJsonSchemaFactory(): JsonSchemaFactory {
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