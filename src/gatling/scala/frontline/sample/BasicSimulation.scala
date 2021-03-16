/*
 * Copyright 2011-2018 GatlingCorp (https://gatling.io)
 *
 * All rights reserved.
 */

package frontline.sample

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

import com.trilogy.ccab.grpc.api.{ChargingEngineGrpc, ChargingRequest, ChargingRequestType, ChargingResponse}
import com.github.phisgr.gatling.grpc.Predef._
import com.github.phisgr.gatling.grpc.action.GrpcCallActionBuilder
import com.github.phisgr.gatling.grpc.protocol.StaticGrpcProtocol
import com.github.phisgr.gatling.javapb._
import frontline.common.Helper
import io.gatling.core.feeder.BatchableFeederBuilder
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioBuilder


class BasicSimulation extends Simulation {

  val httpConf = http
    .baseUrl("http://computer-database.gatling.io")

  val scn = scenario("scenario1")
    .exec(
      http("Page 0")
        .get("/computers?p=0")
    )
    .exec(
      http("Page 1")
        .get("/computers?p=1")
    )

  setUp(
    scn.inject(rampUsers(10).during(10.seconds))
  ).protocols(httpConf)
}
