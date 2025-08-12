import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http.baseUrl("http://localhost:8080") // target gateway

  val scn = scenario("Basic Traffic")
    .repeat(100) {
      exec(http("ping").get("/api/metrics/ping"))
    }

  setUp(scn.inject(atOnceUsers(50))).protocols(httpProtocol)
}
