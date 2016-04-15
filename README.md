json-schema-diff
========================

![Build status](https://travis-ci.org/paradoxical-io/json-schema-diff.svg?branch=master)

This is a basic json schema differ that takes a master json blob and compares it to another json blob.

It checks against

- Property type mismatches
- Array length mismatches
- Missing properties

And lets you configure the failure options.  A use case for the library is to test the json you expect against json
from an external provider.

## To install

```
<dependency>
    <groupId>io.paradoxical</groupId>
    <artifactId>json-schema-diff</artifactId>
    <version>1.1</version>
</dependency>
```

## Usage

Pass in the master and the compare json and execute the comparer

```
String jsonMaster = "..."
String jsonToCompare = "..."

List<Difference> differences = new SchemaComparer(jsonMaster, jsonToCompare).differences()
```

Output Example

```
Difference(
    fieldName=follows,
    fieldValue=0,
    path=data.counts.follows,
    reason=WrongType,
    expected=NUMBER,
    found=STRING
)
```

One thing to note is that arrays are tricky and interesting.  For EACH array element in the master,
the comparer will compare against EACH element in the same array in the target. This is because array objects
 frequently do not come back with all the same fields, so the master json should represent all the fields you
 care about.  A NULL member in the compared will be ignored, since its type cannot be determined.