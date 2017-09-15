package com.moneytransfer.tests.apitests

import com.moneytransfer.utils.JsonUtil
import com.moneytransfer.webapi.WebApiServer
import com.twitter.finagle.http.Status.{NotFound, Ok}
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest



class WebApiFeatureTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "WebServer" should {
    "return 404 for unsupported urls" in {
      server.httpGet(
        path = "/",
        andExpect = NotFound
      )
      server.httpGet(
        path = "/asdf",
        andExpect = NotFound
      )
      server.httpGet(
        path = "/asdf/1",
        andExpect = NotFound
      )
      server.httpPost(
        path = "/account/something/1",
        postBody = "",
        andExpect = NotFound
      )
    }
  }
}

class GetNonExistingAccountInfoTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Get non existing account" should {
    "return api error" in {
      server.httpGet(
        path = "/account/1",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(ApiErrorResponseResult("No account exists with id: 1"))
      )
    }
  }
}
