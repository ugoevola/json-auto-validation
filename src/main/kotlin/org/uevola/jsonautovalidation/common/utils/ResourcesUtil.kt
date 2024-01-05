package org.uevola.jsonautovalidation.common.utils

import mu.KLogging
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.uevola.jsonautovalidation.common.Constants.GENERATED_JSON_PATH
import org.uevola.jsonautovalidation.common.Constants.SCHEMA_JSON_EXT
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.FileSystems
import java.nio.file.Files

object ResourcesUtil : KLogging() {

    /**
     * retrieve the resource corresponding to the schema name
     *
     * @param schemaName the name of the resource schema
     */
    fun getResourceSchema(schemaName: String): Resource? {
        val resolver = PathMatchingResourcePatternResolver()
        val path = "classpath*:**/$schemaName$SCHEMA_JSON_EXT"
        return resolver
            .getResources(path)
            .find { schemaName.plus(SCHEMA_JSON_EXT) == it.filename }
    }

    fun getResourceSchemaAsString(schemaName: String): String? {
        val classPathResource = ClassPathResource("")
        val uri = classPathResource.uri
        return if (uri.scheme == "file") {
            getResourceSchema(schemaName)?.inputStream?.bufferedReader().use { it?.readText() }
        } else {
            getResourceSchemaJar(schemaName)
        }
    }

    private fun getResourceSchemaJar(schemaName: String): String {
        val pathResource = ClassPathResource("")
        val fileSystem = FileSystems.newFileSystem(pathResource.uri, emptyMap<String, Any>())
        val filePath = fileSystem.getPath(GENERATED_JSON_PATH, schemaName + SCHEMA_JSON_EXT)
        val result = Files.readString(filePath)
        fileSystem.close()
        return result
    }


    /**
     * add a resource schema in the resources /generated-schemas
     *
     * @param schemaName the name of the resource schema
     * @param content the content of the resource
     */
    fun addSchemaResource(schemaName: String, content: String?) {
        if (content == null) return
        val pathResource = ClassPathResource("")
        val uri = pathResource.uri
        if (uri.scheme == "file") {
            createDirectories()
            createFile(
                schemaName + SCHEMA_JSON_EXT,
                content
            )
        } else {
            createFileJar(
                schemaName + SCHEMA_JSON_EXT,
                content
            )
        }
    }

    private fun createDirectories() {
        val pathResource = ClassPathResource("")
        val directory = File(pathResource.uri.path, GENERATED_JSON_PATH)
        if (!directory.exists()) {
            directory.mkdirs()
        }
    }

    private fun createFile(name: String, content: String) {
        val pathResource = ClassPathResource(GENERATED_JSON_PATH)
        val file = File(pathResource.uri.path, name)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(content, StandardCharsets.UTF_8)
    }

    private fun createFileJar(filename: String, content: String) {
        val pathResource = ClassPathResource("")
        val fileSystem = FileSystems.newFileSystem(pathResource.uri, emptyMap<String, Any>())
        val filePath = fileSystem.getPath(GENERATED_JSON_PATH, filename)
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