package org.uevola.jsonautovalidation.runtime.common.config

import com.networknt.schema.SchemaRegistry
import com.networknt.schema.SchemaRegistryConfig
import com.networknt.schema.dialect.Dialect
import com.networknt.schema.dialect.Dialects
import com.networknt.schema.keyword.DisallowUnknownKeywordFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.uevola.jsonautovalidation.common.Constants.ERROR_MESSAGE_KEYWORD
import org.uevola.jsonautovalidation.common.keywords.IsStringIntegerKeyword
import org.uevola.jsonautovalidation.common.keywords.IsStringNumberKeyword

@Configuration
internal class NetworkNtSchemaValidationConfig {

    @Bean
    fun customSchemaRegistry(): SchemaRegistry {
        val config = SchemaRegistryConfig.builder().errorMessageKeyword(ERROR_MESSAGE_KEYWORD).build()
        val dialect = Dialect.builder(Dialects.getDraft202012())
            .keyword(IsStringIntegerKeyword())
            .keyword(IsStringNumberKeyword())
            .unknownKeywordFactory(DisallowUnknownKeywordFactory.getInstance())
            .build()
        return SchemaRegistry.withDialect(dialect) { builder ->
            builder.schemaRegistryConfig(config)
        }
    }

}