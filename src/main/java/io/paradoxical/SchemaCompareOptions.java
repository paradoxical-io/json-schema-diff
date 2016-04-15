package io.paradoxical;

import lombok.Data;

@Data
public class SchemaCompareOptions {
    private Boolean failOnMisMatchedArrayLengths = false;

    private Boolean failOnMissingObject = false;

    private Boolean failIfMasterIsMissingProperties = false;
}
