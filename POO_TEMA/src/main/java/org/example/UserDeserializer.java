package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class UserDeserializer extends StdDeserializer<User> {

    public UserDeserializer() {
        this(null);
    }
    protected UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        String userType = node.get("userType").asText();

        switch (userType) {
            case "Regular":
                return deserializeRegular(node);
            case "Contributor":
                return deserializeContributor(node);
            case "Admin":
                return deserializeAdmin(node);
            default:
                throw new IllegalArgumentException("Unknown user type: " + userType);
        }
    }

    private User deserializeAdmin(JsonNode node) {
        return deserializeCommonFields(node, new Admin());
    }

    private User deserializeContributor(JsonNode node) {
        return deserializeCommonFields(node, new Contributor());
    }

    private User deserializeRegular (JsonNode node) {
        return deserializeCommonFields(node, new Regular());
    }

    private User deserializeCommonFields(JsonNode node, User user) {
        String username = node.get("username").asText();
        int experience = node.get("experience").asInt();

        user.setType(AccountType.valueOf(node.get("userType").asText()));

        User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder(
                node.path("information").path("name").asText(),
                node.path("information").path("country").asText()
        );

        JsonNode credentialsNode = node.path("information").path("credentials");
        if (!credentialsNode.isMissingNode()) {
            Credentials credentials = new Credentials(
                    credentialsNode.path("email").asText(),
                    credentialsNode.path("password").asText()
            );
            informationBuilder.credentials(credentials);
        }

        informationBuilder.gender(Gender.valueOf(node.path("information").path("gender").asText()));

        JsonNode birthDateNode = node.path("information").path("birthDate");
        LocalDate birthDate = null;
        if (!birthDateNode.isMissingNode()) {
            birthDate = LocalDate.parse(birthDateNode.asText(), DateTimeFormatter.ISO_DATE);
        }
        informationBuilder.birthDate(birthDate);

        User.Information information = informationBuilder.build();

        user.setUsername(username);
        user.setExperience(experience);
        user.setInformation(information);

        // Deserialize notifications
        JsonNode notificationsNode = node.path("notifications");
        if (!notificationsNode.isMissingNode()) {
            List<String> notifications = new ArrayList<>();
            for (JsonNode notificationNode : notificationsNode) {
                notifications.add(notificationNode.asText());
            }
            user.setNotifications(notifications);
        }

        // Deserialize favorite actors and favorite productions into a common list
        JsonNode favoriteActorsNode = node.path("favoriteActors");
        JsonNode favoriteProductionsNode = node.path("favoriteProductions");

        SortedSet<Object> favoritesList = new TreeSet<>();

        if (!favoriteActorsNode.isMissingNode()) {
            for (JsonNode actorNode : favoriteActorsNode) {
                favoritesList.add(actorNode.asText());
            }
        }

        if (!favoriteProductionsNode.isMissingNode()) {
            for (JsonNode productionNode : favoriteProductionsNode) {
                favoritesList.add(productionNode.asText());
            }
        }

        user.setFavoritesList(favoritesList);

        return user;
    }
}
