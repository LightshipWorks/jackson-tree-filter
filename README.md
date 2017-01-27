# Overview

This project contains a PropertyFilter for [Jackson Mapper](http://wiki.fasterxml.com/JacksonHome) along with an 
annotation for the [Spring Framework](http://projects.spring.io/spring-framework/) which provides a method of filtering 
the properties returned in a JSON object via passing in a formatted string.  The project is licensed under 
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

-----

# Get

## Maven

The Java package is `works.lightship`, and can be retrieved through Maven dependency:

```xml
<dependencies>
  ...
  <dependency>
    <groupId>works.lightship</groupId>
    <artifactId>jackson-tree-filter</artifactId>
    <version>1.0-RC1</version>
  </dependency>
  ...
</dependencies>
```

-----

# Use

This filter can be used in one of two ways, directly on a Spring Controller, or manually through an Object Mapper.

Given the following example objects

```java
public class ExampleObject {
    private UUID id;
    private String publicName;
    private String hiddenName;
    private SubObject sub;
    ...
}

public class SubObject {
    private UUID id;
    private String subName;
    ...
}
```

The following will serialize the ExampleObject and it's SubObject while hiding the hiddenName and the SubObjects id.

## Spring

```java
@RequestMapping(value = "/example")
@FilteredResponseBody("id, publicName, sub{subName}")
public ExampleObject getExample() {
    return exampleObjectRepository.findOne(...);
}

```

To make the @FilteredResponseBody annotation work, we have to add the FilteredResponseBodyAdvice to the project.
The simplest way to do this is to extend the class and annotate it with @ControllerAdvice.

```java
@ControllerAdvice
public class FilteredResponseBodyAdviceImpl extends FilteredResponseBodyAdvice {
}
```

## ObjectMapper

```java
...
Map<String, Object> filterTree = TreeStringParser.parse("id, publicName, sub{subName}");
TreeFilterProvider propertyFilterProvider = new TreeFilterProvider(filterTree);
ObjectWriter objectWriter = objectMapper.writer(propertyFilterProvider);
String json = objectWriter.writeValue(generator, exampleObject);
```

-----
