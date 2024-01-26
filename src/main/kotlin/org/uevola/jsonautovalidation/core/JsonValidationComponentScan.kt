package org.uevola.jsonautovalidation.core

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = ["org.uevola.jsonautovalidation"]
)
open class JsonValidationComponentScan