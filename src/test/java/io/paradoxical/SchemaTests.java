package io.paradoxical;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTests {
    @Test
    public void test_simple_difference() throws IOException {
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
    public void test_complex_difference() throws IOException {
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

