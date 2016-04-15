package io.paradoxical;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

enum DifferenceType {
    Missing,
    WrongType
}

@Value
class Difference {
    String fieldName;
    String fieldValue;
    String path;
    DifferenceType reason;
    String expected;
    String found;
}

class Path {
    private Stack<String> stack = new Stack<>();

    void push(JsonNode parent) {
        push(parent.asText());
    }

    void push(String parent) {
        stack.push(parent);
    }


    void pop() {
        stack.pop();
    }

    String resolve() {
        return String.join(".", stack);
    }
}

@Data
public class SchemaComparer {
    private final JsonNode left;
    private final JsonNode right;
    private final Path path;

    public SchemaComparer(final JsonValue left, final JsonValue right) throws IOException {
        this.left = getParser(left);
        this.right = getParser(right);

        path = new Path();
    }

    protected SchemaComparer(final JsonNode left, final JsonNode right, Path path) {
        this.left = left;
        this.right = right;

        this.path = path;
    }

    public List<Difference> differences() throws IOException {
        if (left.getNodeType() != right.getNodeType()) {
            return Collections.singletonList(
                    new Difference("root",
                                   left.asText(),
                                   path.resolve(),
                                   DifferenceType.WrongType,
                                   left.getNodeType().toString(),
                                   right.getNodeType().toString()));
        }

        switch (left.getNodeType()) {
            case ARRAY:
                return compareArrays(left, right);
            case OBJECT:
                return compareObjects(left, right);
        }

        return compareFields(left, right, "");
    }

    private List<Difference> compareArrays(final JsonNode leftNode, final JsonNode rightNode) {
        final ArrayList<Difference> differences = new ArrayList<>();

        path.push("[]");

        final ArrayList<JsonNode> leftObjects = Lists.newArrayList(leftNode.elements());

        final ArrayList<JsonNode> rightObjects = Lists.newArrayList(rightNode.elements());

        if (leftObjects.size() != rightObjects.size()) {
            return Arrays.asList(new Difference("", "", path.resolve(), DifferenceType.Missing, "Length: " + leftObjects.size(), String.valueOf(rightObjects.size())));
        }

        for (int i = 0; i < leftObjects.size(); i++) {
            try {
                final List<Difference> arrayDifferences = new SchemaComparer(leftObjects.get(0), rightObjects.get(0), path).differences();

                differences.addAll(arrayDifferences);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        path.pop();

        return differences;
    }

    private List<Difference> compareObjects(JsonNode leftNode, JsonNode rightNode) {
        final ArrayList<Difference> differences = new ArrayList<>();

        final ArrayList<String> leftElements = Lists.newArrayList(leftNode.fieldNames());

        leftElements.sort(String::compareTo);

        final ArrayList<String> rightElements = Lists.newArrayList(rightNode.fieldNames());

        rightElements.sort(String::compareTo);

        for (final String fieldName : leftElements) {
            differences.addAll(compareFields(leftNode, rightNode, fieldName));
        }

        return differences;
    }

    private List<Difference> compareFields(final JsonNode leftParent, final JsonNode rightParent, final String fieldName) {
        try {
            path.push(fieldName);

            JsonNode leftValue = leftParent.get(fieldName);
            JsonNode rightValue = rightParent.get(fieldName);

            if (rightValue == null) {
                return Arrays.asList(
                        new Difference(fieldName,
                                       null,
                                       path.resolve(),
                                       DifferenceType.Missing,
                                       leftValue.getNodeType().toString(),
                                       "none"));
            }

            switch (leftValue.getNodeType()) {
                case STRING:
                case BINARY:
                case BOOLEAN:
                case MISSING:
                case NUMBER:
                    if (leftValue.getNodeType() != rightValue.getNodeType()) {
                        return Arrays.asList(
                                new Difference(fieldName,
                                               rightValue.textValue(),
                                               path.resolve(),
                                               DifferenceType.WrongType,
                                               leftValue.getNodeType().toString(),
                                               rightValue.getNodeType().toString()));
                    }
                case ARRAY:
                    return compareArrays(leftValue, rightValue);
                case OBJECT:
                    return compareObjects(leftValue, rightValue);
                case NULL:
                case POJO:
                    break;
            }
        }
        finally {
            path.pop();
        }

        return Collections.emptyList();
    }

    private JsonNode getParser(final JsonValue value) throws IOException {
        return new ObjectMapper().readTree(value.getData());
    }
}
