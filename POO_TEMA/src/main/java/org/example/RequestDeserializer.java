package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestDeserializer extends StdDeserializer<Request> {
    public RequestDeserializer() {
        this(null);
    }

    protected RequestDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Request deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String type = node.get("type").asText();
        RequestType requestType = RequestType.valueOf(type);

        String createdDateStr = node.get("createdDate").asText();
        LocalDateTime createdDate = LocalDateTime.parse(createdDateStr, DateTimeFormatter.ISO_DATE_TIME);

        String username = node.get("username").asText();
        String to = node.get("to").asText();
        String description = node.get("description").asText();

        return new Request(requestType, createdDate, username, to, description);
    }
}
