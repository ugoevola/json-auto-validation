# Problematic
## Typically

In a Springboot REST API with a layered architecture, the controller layer is responsible for validating incoming data.  The developer fills in the various entry points accessible in the API with their associated http method, as well as the types of data read in the request.

The de-serialization process, which is transparent to the developer, transforms the request data into a java object that can be used in the code. A large part of the data validation is done at this point: if the data is not of the expected type, deserialization fails, and the request is rejected with a `400 BadRequest` return code.

We can then add further checks on the data, for example to check that a character string respects a certain regular expression. To do this, a common practice is to use the [spring-boot-starter-validation]() starter, proposed by Spring and based on the use of annotations.

## Problem

The fact that data validation takes place in two stages (deserialization then bean validation) can in some circumstances be difficult to supervise.

Let's suppose we have an API in which we want to have an error specifically linked to the validation of a certain piece of data. The idea would be to create an exception handler, which would catch exceptions linked to deserialization and those linked to validation. We would then need to check whether the exception in question is linked to the data in question and generate the desired error.

In SpringBoot, the default JSON deserializer is Jackson. When a de-serialization fails, Jackson can return various types of Exceptions. Given that not all the data received is JSON, you can imagine the number of exceptions you have to handle in your exception handler. It can quickly become tedious.

## Solution

The aim of this library is to carry out a thorough check of all types of data before deserialization in record time and to return only one type of exception. As a result, there will be no more deserialization errors, and no need to check the beans.