package com.example.sicredi_challenge;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class VotingSimulation extends Simulation {

    private static final int USERS = 500;
    private static final Duration RAMP_DURATION = Duration.ofSeconds(30);
    private static final Duration TEST_DURATION = Duration.ofMinutes(5);
    private static final String AGENDA_ID = "1";

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json");

    ScenarioBuilder votingScenario = scenario("Voting Load Testing")
            .forever()
            .on(
                    exec(
                            http("Votar")
                                    .post(session -> "/api/agendas/" + AGENDA_ID + "/votes")
                                    .body(StringBody(session -> """
                                            {
                                                "associate_id": "%s",
                                                "vote": "%s"
                                            }
                                            """.formatted(
                                            UUID.randomUUID(),
                                            ThreadLocalRandom.current().nextBoolean()
                                                    ? "SIM"
                                                    : "NAO"
                                    )))
                                    .check(status().is(200))
                    ),
                    pause(Duration.ofMillis(50), Duration.ofMillis(200))
            );

    {
        setUp(
                votingScenario.injectOpen(
                        rampUsers(USERS)
                                .during(RAMP_DURATION)
                )
        )
                .protocols(httpProtocol)
                .maxDuration(TEST_DURATION);
    }
}