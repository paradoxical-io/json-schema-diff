package io.paradoxical;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTests {
    @Test
    public void wrong_type_simple() throws IOException {
        final JsonValue left = get("wrong_type_simple/left.json");

        final JsonValue right = get("wrong_type_simple/right.json");

        final List<Difference> differences = new SchemaComparer(left, right).differences();

        differences.forEach(System.out::println);

        assertThat(differences).isNotEmpty();

        final Difference difference = differences.get(0);

        final Difference expected = new Difference("follows", "0", "data.counts.follows", DifferenceType.WrongType, "NUMBER", "STRING");

        assertThat(difference).isEqualTo(expected);
    }

    @Test
    public void left_hand_missing_properties_ok() throws IOException {
        final JsonValue left = get("left_hand_missing_properties/left.json");

        final JsonValue right = get("left_hand_missing_properties/right.json");

        final List<Difference> differences = new SchemaComparer(left, right).differences();

        differences.forEach(System.out::println);

        assertThat(differences).isEmpty();
    }

    @Test
    public void left_hand_missing_properties_fail() throws IOException {
        final JsonValue left = get("left_hand_missing_properties/left.json");

        final JsonValue right = get("left_hand_missing_properties/right.json");

        final SchemaCompareOptions schemaCompareOptions = new SchemaCompareOptions();

        schemaCompareOptions.setFailIfMasterIsMissingProperties(true);

        final List<Difference> differences = new SchemaComparer(left, right, schemaCompareOptions).differences();

        differences.forEach(System.out::println);

        assertThat(differences).isNotEmpty();
    }

    @Test
    public void complex_array_diff() throws IOException {
        final JsonValue left = get("complex_array_diff/left.json");

        final JsonValue right = get("complex_array_diff/right.json");

        final List<Difference> differences = new SchemaComparer(left, right).differences();

        differences.forEach(System.out::println);

        assertThat(differences).isEmpty();
    }

    private JsonValue get(String resourceName) throws IOException {
        final InputStream resourceAsStream = SchemaTests.class.getClassLoader().getResourceAsStream(resourceName);

        return new JsonValue(IOUtils.toString(resourceAsStream));
    }
}

