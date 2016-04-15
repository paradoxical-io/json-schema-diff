package io.paradoxical;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Stack;

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
