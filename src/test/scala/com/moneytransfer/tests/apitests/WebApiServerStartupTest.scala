package com.moneytransfer.tests.apitests

import com.google.inject.Stage
import com.moneytransfer.webapi.WebApiServer
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.Test

class WebApiServerStartupTest extends Test {

  val server = new EmbeddedHttpServer(
    stage = Stage.PRODUCTION,
    twitterServer = new WebApiServer)

  "server" in {
    server.assertHealthy()
  }
}
