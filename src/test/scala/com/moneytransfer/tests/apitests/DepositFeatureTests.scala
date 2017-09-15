package com.moneytransfer.tests.apitests

import com.moneytransfer.accounts._
import com.moneytransfer.utils.JsonUtil
import com.moneytransfer.webapi.WebApiServer
import com.twitter.finagle.http.Status.{NotFound, Ok}
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest


class DepositHistoryTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Successful Deposit" should {
    "show up in account history" in {
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(1))
      )
      server.httpPut(
        path = "/account/1/deposit/8",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(DepositCash(1,8))
      )
      server.httpGet(
        path = "/account/1",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(1, List(Open(1),DepositCash(1,8))))
      )
    }
  }
}

class DepositHistoryTest2 extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Successful Deposit" should {
    "not show up in uninvolved accounts history" in {
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(1))
      )
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(2))
      )
      server.httpPut(
        path = "/account/1/deposit/8",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(DepositCash(1,8))
      )
      server.httpGet(
        path = "/account/1",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(1, List(Open(1),DepositCash(1,8))))
      )
      server.httpGet(
        path = "/account/2",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(2, List(Open(2))))
      )

    }
  }
}

class DepositNegativeAmountTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Deposit" should {
    "not accept negative amount" in {
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(1))
      )
      server.httpPut(
        path = "/account/1/deposit/-88",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(ApiErrorResponseResult(ValidationMessages.AmountMustBePositive))
      )
    }
  }
}
