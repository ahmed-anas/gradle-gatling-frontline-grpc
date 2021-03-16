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
  val chargingRequestTerminateMessage: Expression[ChargingRequest] = ChargingRequest.newBuilder().build()
      .update(_.setId)("id")
      .update(_.setProvider)("provider")
      .update(_.setRequestType)(ChargingRequestType.TERMINATION_REQUEST)
      
  def chargingRequestTerminate(name: String): GrpcCallActionBuilder[ChargingRequest, ChargingResponse] = grpc(name)
    .rpc(ChargingEngineGrpc.getChargeRequestMethod)
    .payload(chargingRequestTerminateMessage)

  val scn = scenario("scenario1")
    .exec(chargingRequestTerminate("Terminate"))
  val grpcConf: StaticGrpcProtocol = grpc(managedChannelBuilder("charge-engine-dev.private.central-eks.aureacentral.com", 443)).shareChannel

  setUp(
    scn.inject(rampUsers(10).during(10.seconds))
  ).protocols(grpcConf)
}
