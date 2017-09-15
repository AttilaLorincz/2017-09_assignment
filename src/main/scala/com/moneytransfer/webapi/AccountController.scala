package com.moneytransfer.webapi

import javax.inject.Inject

import scalaz.{Failure, Success, Validation}
import com.moneytransfer.accounts.BankService
import com.moneytransfer.accounts.TypeAliases.Money
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class AccountController @Inject()(bankService: BankService) extends Controller {

  get("/account/:id") { request: Request =>
    val result = bankService.GetAccountInformation(request.getIntParam("id"))
    result match {
      case Success(accountInformation) => accountInformation
      case fail => fail
    }
  }

  post("/account") { request: Request =>
    val result =  bankService.OpenAccount()
    result match {
      case Success(openingTransaction) => openingTransaction
      case fail => fail
    }
  }

  put("/account/:fromAccountId/transfer/:toAccountId/:amount") { request: Request =>
    val result = bankService.IntrabankTransfer(
      request.getIntParam("fromAccountId"),
      request.getIntParam("toAccountId"),
      Money(request.getParam("amount"))
    )
    result match {
      case Success(transferTransaction) => transferTransaction
      case fail => fail
    }
  }

  put("/account/:accountId/deposit/:amount") { request: Request =>
    val result = bankService.Deposit(
      request.getIntParam("accountId"),
      Money(request.getParam("amount"))
    )
    result match {
      case Success(depositTransaction) => depositTransaction
      case fail => fail
    }
  }
}
