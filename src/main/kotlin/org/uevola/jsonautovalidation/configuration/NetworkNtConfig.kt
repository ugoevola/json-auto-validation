package org.uevola.jsonautovalidation.configuration

import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.utils.Util.STRING_INTEGER_KEYWORD
import org.uevola.jsonautovalidation.utils.Util.STRING_NUMBER_KEYWORD
import org.uevola.jsonautovalidation.utils.keywords.IsIntegerKeyword
import org.uevola.jsonautovalidation.utils.keywords.IsNumberKeyword

@Configuration
open class NetworkNtConfig {

    @Bean
    open fun customJsonSchemaFactory(): JsonSchemaFactory {
        val jsonSchemaVersion = JsonSchemaFactory.checkVersion(SpecVersion.VersionFlag.V202012)
        val metaSchema = jsonSchemaVersion.instance
        metaSchema.keywords[STRING_INTEGER_KEYWORD] = IsIntegerKeyword()
        metaSchema.keywords[STRING_NUMBER_KEYWORD] = IsNumberKeyword()
        return JsonSchemaFactory.builder()
            .defaultMetaSchemaURI(metaSchema.uri)
            .addMetaSchema(metaSchema)
            .build()
    }

}