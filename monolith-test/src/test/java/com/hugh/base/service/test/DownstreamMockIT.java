package com.hugh.base.service.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class DownstreamMockIT {
    static WireMockServer wireMockServer;

    @BeforeAll
    public static void start() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void stop() {
        if (wireMockServer != null) wireMockServer.stop();
    }

    @Test
    public void testMock() {
        stubFor(get(urlEqualTo("/downstream/ping"))
                .willReturn(aResponse().withStatus(200).withBody("pong")));

        // call the mocked endpoint (simple)
        String url = "http://localhost:" + wireMockServer.port() + "/downstream/ping";
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL(url).openStream(), "UTF-8")) {
            String body = s.useDelimiter("\\A").next();
            Assertions.assertEquals("pong", body);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }
}
