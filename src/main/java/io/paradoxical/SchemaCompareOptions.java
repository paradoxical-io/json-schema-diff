package io.paradoxical;

import lombok.Data;

@Data
public class SchemaCompareOptions {
    /**
     * Fail if array lengths between master and compare dont match (i.e. element mismatches)
     */
    private Boolean failOnMisMatchedArrayLengths = false;

    /**
     * Fail if the compared item does not have a field the master does
     */
    private Boolean failOnMissingObject = false;

    /**
     * Fail if the master is missing fields the compare does
     */
    private Boolean failIfMasterIsMissingProperties = false;
}
