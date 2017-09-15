package com.moneytransfer.tests.apitests

import com.moneytransfer.accounts._
import com.moneytransfer.utils.JsonUtil
import com.moneytransfer.webapi.WebApiServer
import com.twitter.finagle.http.Status.{NotFound, Ok}
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest


class OpenAccountTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Post /account" should {
    "return the successful transaction" in {
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(1))
      )
    }
  }
}

class OpenAccountBalanceTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Newly created account" should {
    "have 0 balance" in {
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(1))
      )
      server.httpGet(
        path = "/account/1",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(1, List(Open(1))))
      )
    }
  }
}


class OpenAccountOnlyOneTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Open account" should {
    "create only one account" in {
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(1))
      )
      server.httpGet(
        path = "/account/1",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(1, List(Open(1))))
      )
      server.httpGet(
        path = "/account/2",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(ApiErrorResponseResult(ValidationMessages.AccountNotFound(2)))
      )
    }
  }
}

