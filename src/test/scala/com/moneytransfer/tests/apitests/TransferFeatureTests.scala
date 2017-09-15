package com.moneytransfer.tests.apitests

import com.moneytransfer.accounts._
import com.moneytransfer.utils.JsonUtil
import com.moneytransfer.webapi.WebApiServer
import com.twitter.finagle.http.Status.Ok
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class TransferNegativeAmountTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Transfer" should {
    "return api error if amount is negative" in {
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
        path = "/account/1/transfer/2/-88",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(ApiErrorResponseResult(ValidationMessages.AmountMustBePositive))
      )
    }
  }
}

class TransferBetweenOneAccountTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Transfer if source and target are the same" should {
    "return api error" in {
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
        path = "/account/1/transfer/1/1",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(ApiErrorResponseResult(ValidationMessages.AccountsMustDiffer))
      )
    }
  }
}

class TransferInsufficientBalanceTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)

  "Transfer" should {
    "return insufficient balance error if not enough funds on source" in {
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
        path = "/account/1/transfer/2/1",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(ApiErrorResponseResult(ValidationMessages.InsufficientBalance))
      )
    }
  }
}

class TransferHistoryTest extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)
  "Successful Transfer" should {
    "add transaction to both accounts history" in {
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
        withJsonBody = JsonUtil.toJson(DepositCash(1, 8))
      )
      server.httpPut(
        path = "/account/1/transfer/2/1",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Transfer(1, 2, 1))
      )
      server.httpGet(
        path = "/account/1",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(1, List(Open(1), DepositCash(1, 8), Transfer(1, 2, 1))))
      )
      server.httpGet(
        path = "/account/2",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(2, List(Open(2), Transfer(1, 2, 1))))
      )

    }
  }
}


class TransferHistoryTest2 extends FeatureTest {
  override val server = new EmbeddedHttpServer(new WebApiServer)
  "Successful Transfer" should {
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
      server.httpPost(
        path = "/account",
        postBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Open(3))
      )
      server.httpPut(
        path = "/account/1/deposit/8",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(DepositCash(1, 8))
      )
      server.httpPut(
        path = "/account/1/transfer/2/1",
        putBody = "",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(Transfer(1, 2, 1))
      )

      server.httpGet(
        path = "/account/3",
        andExpect = Ok,
        withJsonBody = JsonUtil.toJson(AccountStatement(3, List(Open(3))))
      )
    }
  }
}
