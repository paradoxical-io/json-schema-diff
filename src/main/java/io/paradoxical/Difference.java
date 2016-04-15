package io.paradoxical;

import lombok.Value;

@Value
public class Difference {
    String fieldName;
    String fieldValue;
    String path;
    DifferenceType reason;
    String expected;
    String found;
}

