package org.uevola.jsonautovalidation.common.utils

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.uevola.jsonautovalidation.configuration.JsonValidationConfig
import org.uevola.jsonautovalidation.common.Constants.GENERATED_JSON_PATH
import org.uevola.jsonautovalidation.common.Constants.SCHEMA_JSON_EXT
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files

object ResourcesUtil {

    /**
     * retrieve the resource corresponding to the schema name
     * recovery is based on the property "json-validation.resources-path"
     *
     * @param schemaName the name of the resource schema
     */
    fun getSchemaResource(schemaName: String): Resource? {
        val resolver = PathMatchingResourcePatternResolver()
        return resolver.getResources(
            "classpath*:"
                .plus(JsonValidationConfig.resourcesPath)
                .plus("**/")
                .plus(schemaName)
                .plus(SCHEMA_JSON_EXT)
        ).find { schemaName.plus(SCHEMA_JSON_EXT) == it.filename }
    }


    /**
     * add a resource schema in the resource path corresponding to the property "json-validation.resources-path"
     *
     * @param schemaName the name of the resource schema
     * @param content the content of the resource
     */
    fun addSchemaResource(schemaName: String, content: String?) {
        if (content == null) return
        val pathResource = ClassPathResource("")
        val uri = pathResource.uri
        if (uri.scheme == "file") {
            createDirectories(JsonValidationConfig.resourcesPath)
            createFile(
                "${JsonValidationConfig.resourcesPath}/${GENERATED_JSON_PATH}",
                schemaName + SCHEMA_JSON_EXT,
                content
            )
        } else {
            createFileJar(
                JsonValidationConfig.resourcesPath,
                schemaName + SCHEMA_JSON_EXT,
                content
            )
        }
    }

    private fun createDirectories(basePath: String) {
        val pathResource = ClassPathResource(basePath)
        val directory = File(pathResource.uri.path, GENERATED_JSON_PATH)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    private fun createFile(basePath: String, name: String, content: String) {
        val pathResource = ClassPathResource(basePath)
        val file = File(pathResource.uri.path, name)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(content, StandardCharsets.UTF_8)
    }

    private fun createFileJar(basePath: String, filename: String, content: String) {
        val pathResource = ClassPathResource(basePath)
        val fileSystem = FileSystems.newFileSystem(pathResource.uri, emptyMap<String, Any>())
        val filePath = fileSystem.getPath(basePath, GENERATED_JSON_PATH, filename)
        val parentPath = filePath.parent
        if (!Files.isDirectory(parentPath)) {
            Files.createDirectories(parentPath)
        }
        if (!Files.exists(filePath)) {
            Files.createFile(filePath)
            Files.newBufferedWriter(filePath, StandardCharsets.UTF_8).use { writer ->
                writer.write(content)
            }
        }
        fileSystem.close()
    }
}