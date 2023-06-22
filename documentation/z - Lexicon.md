# Lexicon

|         Annotation         | fields                                                                                               | automatic<br/>type detection |
|:--------------------------:|------------------------------------------------------------------------------------------------------|:----------------------------:|
|        **@IsArray**        | - message<br/>- type<br/>- minItems<br/>- maxItems<br/> - uniqueItems                                |             :x:              |
|        **@IsBool**         | - message                                                                                            |      :heavy_check_mark:      |
|        **@IsDate**         | - message                                                                                            |             :x:              |
|      **@IsDateTime**       | - message                                                                                            |             :x:              |
|        **@IsEmail**        | - message                                                                                            |             :x:              |
|        **@IsEnum**         | - message                                                                                            |      :heavy_check_mark:      |
|       **@IsEqualTo**       | - message<br/>- value                                                                                |             :x:              |
|      **@IsHostName**       | - message                                                                                            |             :x:              |
|      **@IsIdnEmail**       | - message                                                                                            |             :x:              |
|    **@IsIdnHostEmail**     | - message                                                                                            |             :x:              |
|       **@IsInteger**       | - message<br/>- minimum<br/>- maximum<br/>- exclusiveMinimum<br/>- exclusiveMaximum<br/>- multipleOf |      :heavy_check_mark:      |
|        **@IsIpv4**         | - message                                                                                            |             :x:              |
|        **@IsIpv6**         | - message                                                                                            |             :x:              |
|         **@IsIri**         | - message                                                                                            |             :x:              |
|     **@IsJsonPointer**     | - message                                                                                            |             :x:              |
|     **@IsJsonSchema**      | - message <br/>- jsonSchemaName                                                                      |             :x:              |
|       **@IsNested**        | - message                                                                                            |             :x:              |
|       **@IsNotNull**       | - message                                                                                            |             :x:              |
|      **@IsNotEmpty**       | - message                                                                                            |             :x:              |
|       **@IsNumber**        | - message<br/>- minimum<br/>- maximum<br/>- exclusiveMinimum<br/>- exclusiveMaximum<br/>- multipleOf |      :heavy_check_mark:      |
|     **@IsPhoneNumber**     | - message                                                                                            |             :x:              |
|        **@IsRegex**        | - message                                                                                            |             :x:              |
| **@IsRelativeJsonPointer** | - message                                                                                            |             :x:              |
|      **@IsRequired**       | - message                                                                                            |             :x:              |
|       **@IsString**        | - message<br/>- minLength<br/>- maxLength<br/>- pattern<br/>- format                                 |      :heavy_check_mark:      |
|     **@IsStringBool**      | - message                                                                                            |             :x:              |
|    **@IsStringInteger**    | - message<br/>- minimum<br/>- maximum<br/>- exclusiveMinimum<br/>- exclusiveMaximum<br/>- multipleOf |             :x:              |
|    **@IsStringNumber**     | - message<br/>- minimum<br/>- maximum<br/>- exclusiveMinimum<br/>- exclusiveMaximum<br/>- multipleOf |             :x:              |
|        **@IsTime**         | - message                                                                                            |             :x:              |
|         **@IsUri**         | - message                                                                                            |             :x:              |
|    **@IsUriReference**     | - message                                                                                            |             :x:              |
|     **@IsUriTemplate**     | - message                                                                                            |             :x:              |
|        **@IsUuid**         | - message                                                                                            |             :x:              |
|       **@IsValues**        | - message<br/>- values                                                                               |             :x:              |