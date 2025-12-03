package com.github.bda.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class WialonApiService {
    private static final Logger logger = LoggerFactory.getLogger(WialonApiService.class);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();
    private static final String AUTH_TOKEN = "5d7bac6e70f763dbcbceb45e40d3945eDF80FB19FF5EAE819FD2EE869CB2AD66BB7E887D";

    private String sessionId = null;

    public Map<String, Object> loginToWialon() {
        try {
            JsonObject params = new JsonObject();
            params.addProperty("token", AUTH_TOKEN);
            params.addProperty("fl", 1);

            String paramsString = "params=" + URLEncoder.encode(params.toString(), StandardCharsets.UTF_8);

            // Create the POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://hst-api.qa.wialondev.net/wialon/ajax.html?svc=token%2Flogin"))
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("Accept", "*/*")
                    .POST(HttpRequest.BodyPublishers.ofString(paramsString))
                    .build();

            // Send the request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("=== LOGIN RESPONSE ===");
            logger.info("Status Code: {}", response.statusCode());
            logger.info("Response Body: {}", response.body());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                try {
                    JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                    // Check if there's an error in the response
                    if (jsonResponse.has("error")) {
                        return Map.of(
                                "success", false,
                                "operation", "login",
                                "error", "Authentication failed with error code: " + jsonResponse.get("error").toString()
                        );
                    }

                    // Extract session ID (eid field)
                    if (jsonResponse.has("eid")) {
                        sessionId = jsonResponse.get("eid").getAsString();
                        return Map.of(
                                "success", true,
                                "operation", "login",
                                "sessionId", sessionId,
                                "message", "Successfully authenticated with Wialon"
                        );
                    } else {
                        return Map.of(
                                "success", false,
                                "operation", "login",
                                "error", "No session ID (eid) found in response"
                        );
                    }

                } catch (Exception e) {
                    return Map.of(
                            "statusCode", response.statusCode(),
                            "success", false,
                            "operation", "login",
                            "error", "Could not parse JSON response: " + e.getMessage(),
                            "body", response.body()
                    );
                }
            } else {
                return Map.of(
                        "statusCode", response.statusCode(),
                        "success", false,
                        "operation", "login",
                        "error", "HTTP error: " + response.statusCode(),
                        "body", response.body()
                );
            }

        } catch (IOException | InterruptedException e) {
            return Map.of(
                    "error", "Login request failed: " + e.getMessage(),
                    "operation", "login",
                    "success", false
            );
        }
    }

    public Map<String, Object> createWialonUser(String name, String password) {
        try {
            // Check if we have a session ID
            if (sessionId == null || sessionId.trim().isEmpty()) {
                return Map.of(
                        "error", "No active session. Please login first using loginToWialon",
                        "success", false,
                        "operation", "createUser",
                        "suggestion", "Call loginToWialon first to authenticate"
                );
            }

            // Validate required parameters
            if (name == null || name.trim().isEmpty()) {
                return Map.of("error", "Username is required", "success", false);
            }
            if (password == null || password.trim().isEmpty()) {
                return Map.of("error", "Password is required", "success", false);
            }

            // Build the parameters JSON
            JsonObject params = new JsonObject();
            params.addProperty("creatorId", 16949647);
            params.addProperty("name", name);
            params.addProperty("password", password);
            params.addProperty("dataFlags", 5);

            String paramsJson = URLEncoder.encode(params.toString(), StandardCharsets.UTF_8);

            // Build the URL
            String url = String.format(
                    "https://hst-api.qa.wialondev.net/wialon/ajax.html?svc=core%%2Fcreate_user&sid=%s&params=%s",
                    URLEncoder.encode(sessionId, StandardCharsets.UTF_8),
                    paramsJson
            );

            // Create the POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();

            logger.info("=== CREATE USER REQUEST ===");
            logger.info("Parameters: {}", params.toString());
            logger.info("Session ID: {}", sessionId);
            // Send the request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.info("=== CREATE USER RESPONSE ===");
            logger.info("Status Code: {}", response.statusCode());
            logger.info("Response Body: {}", response.body());

            // Parse response
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                try {
                    JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                    // Check if there's an error in the response
                    if (jsonResponse.has("error")) {
                        int errorCode = jsonResponse.get("error").getAsInt();
                        String errorMessage = getWialonErrorMessage(errorCode);

                        return Map.of(
                                "statusCode", response.statusCode(),
                                "success", false,
                                "operation", "createUser",
                                "username", name,
                                "errorCode", errorCode,
                                "error", errorMessage,
                                "response", jsonResponse.toString()
                        );
                    }

                    return Map.of(
                            "statusCode", response.statusCode(),
                            "success", true,
                            "operation", "createUser",
                            "username", name,
                            "sessionId", sessionId,
                            "data", jsonResponse.toString(),
                            "message", "User created successfully"
                    );

                } catch (Exception e) {
                    return Map.of(
                            "statusCode", response.statusCode(),
                            "success", response.statusCode() >= 200 && response.statusCode() < 300,
                            "operation", "createUser",
                            "username", name,
                            "body", response.body(),
                            "parseError", "Could not parse JSON response: " + e.getMessage()
                    );
                }
            } else {
                return Map.of(
                        "statusCode", response.statusCode(),
                        "success", false,
                        "operation", "createUser",
                        "username", name,
                        "error", "HTTP error: " + response.statusCode(),
                        "body", response.body()
                );
            }

        } catch (IOException | InterruptedException e) {
            return Map.of(
                    "error", "API call failed: " + e.getMessage(),
                    "operation", "createUser",
                    "username", name != null ? name : "unknown",
                    "success", false
            );
        }
    }

    private String getWialonErrorMessage(int errorCode) {
        return switch (errorCode) {
            case 1 -> "Invalid session";
            case 2 -> "Invalid service";
            case 3 -> "Invalid result";
            case 4 -> "Invalid input";
            case 5 -> "Error performing request";
            case 6 -> "Unknown error";
            case 7 -> "Access denied";
            case 8 -> "Invalid user name or password";
            case 9 -> "Authorization server is unavailable";
            case 1001 -> "No messages for selected interval";
            case 1002 -> "Item with such unique property already exists";
            case 1003 -> "Only one request of given time is allowed";
            default -> "Unknown error code: " + errorCode;
        };
    }
}
