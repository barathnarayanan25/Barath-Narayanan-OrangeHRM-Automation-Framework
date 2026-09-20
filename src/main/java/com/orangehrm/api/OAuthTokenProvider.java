package com.orangehrm.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.orangehrm.utils.ConfigReader;

import com.sun.net.httpserver.HttpServer;

import io.restassured.response.Response;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static io.restassured.RestAssured.given;

public class OAuthTokenProvider {

    private OAuthTokenProvider() {
    }

    public static String getAccessToken(WebDriver driver) {

        String clientId = ConfigReader.get("apiClientId");
        String redirectUri = ConfigReader.get("apiRedirectUri");

        String codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.generateCodeChallenge(codeVerifier);
        String state = PkceUtil.generateState();

        AuthorizationCodeReceiver receiver =
                new AuthorizationCodeReceiver();

        receiver.start();

        String authorizationUrl =
                buildAuthorizationUrl(
                        clientId,
                        redirectUri,
                        codeChallenge,
                        state
                );

        System.out.println("Opening OAuth authorization URL...");

        driver.get(authorizationUrl);

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(15));

        By allowAccessButton =
                By.xpath("//button[normalize-space()='Allow Access']");

        wait.until(
                ExpectedConditions.elementToBeClickable(allowAccessButton)
        ).click();

        System.out.println("OAuth consent approved.");

        String authorizationCode =
                receiver.waitForAuthorizationCode();

        receiver.stop();

        if (authorizationCode == null || authorizationCode.isBlank()) {
            throw new RuntimeException(
                    "Authorization code was not received."
            );
        }

        System.out.println("Authorization code received successfully.");

        return exchangeCodeForToken(
                authorizationCode,
                clientId,
                redirectUri,
                codeVerifier
        );
    }

    private static String buildAuthorizationUrl(
            String clientId,
            String redirectUri,
            String codeChallenge,
            String state) {

        String baseUrl = ConfigReader.get("baseUrl");

        return baseUrl
                + "web/index.php/oauth2/authorize"
                + "?response_type=code"
                + "&client_id=" + encode(clientId)
                + "&redirect_uri=" + encode(redirectUri)
                + "&code_challenge_method=S256"
                + "&code_challenge=" + encode(codeChallenge)
                + "&state=" + encode(state);
    }

    private static String exchangeCodeForToken(
            String authorizationCode,
            String clientId,
            String redirectUri,
            String codeVerifier) {

        String baseUrl = ConfigReader.get("baseUrl");

        Response response =
                given()
                    .baseUri(baseUrl)
                    .contentType("application/x-www-form-urlencoded")
                    .formParam("grant_type", "authorization_code")
                    .formParam("client_id", clientId)
                    .formParam("code", authorizationCode)
                    .formParam("redirect_uri", redirectUri)
                    .formParam("code_verifier", codeVerifier)
                .when()
                    .post("web/index.php/oauth2/token");

        System.out.println(
                "OAuth token response status: "
                        + response.statusCode()
        );

        if (response.statusCode() != 200) {

            System.out.println(
                    "OAuth token response:"
                            + response.asPrettyString()
            );

            throw new RuntimeException(
                    "Failed to obtain OAuth access token."
            );
        }

        String accessToken =
                response.jsonPath().getString("access_token");

        if (accessToken == null || accessToken.isBlank()) {
            throw new RuntimeException(
                    "Access token missing from OAuth response."
            );
        }

        System.out.println("Access token received successfully.");

        return accessToken;
    }

    private static String encode(String value) {
        return java.net.URLEncoder
                .encode(value, StandardCharsets.UTF_8);
    }

    private static class AuthorizationCodeReceiver {

        private HttpServer server;
        private final CountDownLatch latch =
                new CountDownLatch(1);

        private String authorizationCode;
        private String returnedState;

        public void start() {

            try {

                server =
                        HttpServer.create(
                                new InetSocketAddress(
                                        "localhost",
                                        8080
                                ),
                                0
                        );

                server.createContext(
                        "/callback",
                        exchange -> {

                            String query =
                                    exchange.getRequestURI()
                                            .getRawQuery();

                            Map<String, String> parameters =
                                    parseQuery(query);

                            authorizationCode =
                                    parameters.get("code");

                            returnedState =
                                    parameters.get("state");

                            String responseMessage =
                                    "<html><body>"
                                    + "<h2>Authorization successful.</h2>"
                                    + "<p>You can close this browser window.</p>"
                                    + "</body></html>";

                            exchange.sendResponseHeaders(
                                    200,
                                    responseMessage.getBytes(
                                            StandardCharsets.UTF_8
                                    ).length
                            );

                            try (OutputStream output =
                                         exchange.getResponseBody()) {

                                output.write(
                                        responseMessage.getBytes(
                                                StandardCharsets.UTF_8
                                        )
                                );
                            }

                            latch.countDown();
                        }
                );

                server.start();

            } catch (IOException e) {

                throw new RuntimeException(
                        "Could not start OAuth callback server on port 8080.",
                        e
                );
            }
        }

        public String waitForAuthorizationCode() {

            try {

                boolean received =
                        latch.await(
                                60,
                                TimeUnit.SECONDS
                        );

                if (!received) {

                    throw new RuntimeException(
                            "Timed out waiting for OAuth authorization code."
                    );
                }

                return authorizationCode;

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                throw new RuntimeException(
                        "Interrupted while waiting for OAuth authorization code.",
                        e
                );
            }
        }

        public void stop() {

            if (server != null) {
                server.stop(0);
            }
        }

        private Map<String, String> parseQuery(String query) {

            Map<String, String> parameters =
                    new HashMap<>();

            if (query == null || query.isBlank()) {
                return parameters;
            }

            for (String parameter : query.split("&")) {

                String[] pair = parameter.split("=", 2);

                String key =
                        URLDecoder.decode(
                                pair[0],
                                StandardCharsets.UTF_8
                        );

                String value =
                        pair.length > 1
                                ? URLDecoder.decode(
                                        pair[1],
                                        StandardCharsets.UTF_8
                                )
                                : "";

                parameters.put(key, value);
            }

            return parameters;
        }
    }
}