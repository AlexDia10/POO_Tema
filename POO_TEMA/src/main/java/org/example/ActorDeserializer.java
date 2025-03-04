package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ActorDeserializer extends StdDeserializer<Actor> {

    public ActorDeserializer() {
        this(null);
    }

    protected ActorDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Actor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String name = getRequiredField(node, "name");
        String biography = getRequiredField(node, "biography");
        Map<String, Type> performances = deserializePerformances(node.path("performances"));

        return new Actor(name, performances, biography);
    }

    private String getRequiredField(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && fieldNode.isTextual() && !fieldNode.asText().isEmpty()) {
            return fieldNode.asText();
        } else {
            return null;
        }
    }

    private Map<String, Type> deserializePerformances(JsonNode performancesNode) {
        Map<String, Type> performances = new HashMap<>();
        for (JsonNode performanceNode : performancesNode) {
            JsonNode titleNode = performanceNode.get("title");
            JsonNode typeNode = performanceNode.get("type");

            if (titleNode != null && typeNode != null) {
                String title = titleNode.asText();
                String type = typeNode.asText();

                try {
                    performances.put(title, Type.valueOf(type));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid performance type.");
                }
            } else {
                System.out.println("Title or type missing.");
            }
        }
        return performances;
    }
}

