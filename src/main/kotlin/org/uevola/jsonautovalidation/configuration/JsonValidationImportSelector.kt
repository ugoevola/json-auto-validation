package org.uevola.jsonautovalidation.configuration

import org.springframework.context.annotation.ImportSelector
import org.springframework.core.type.AnnotationMetadata

class JsonValidationImportSelector : ImportSelector {
    override fun selectImports(importingClassMetadata: AnnotationMetadata): Array<String> {
        return arrayOf(JsonValidationConfiguration::class.java.name)
    }
}