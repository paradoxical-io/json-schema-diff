package io.paradoxical;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTests {
    @Test
    public void test_difference() throws IOException {
        final JsonValue left = get("wrong_type_simple/left.json");

        final JsonValue right = get("wrong_type_simple/right.json");

        final List<Difference> differences = new SchemaComparer(left, right).differences();

        System.out.println(differences);

        assertThat(differences).isNotEmpty();
    }

    private JsonValue get(String resourceName) throws IOException {
        final InputStream resourceAsStream = SchemaTests.class.getClassLoader().getResourceAsStream(resourceName);

        return new JsonValue(IOUtils.toString(resourceAsStream));
    }
}

