package com.moneytransfer.tests.unittests

import com.moneytransfer.accounts.{AccountStatement, DepositCash, Open, Transfer}

class AccountStatementTests extends com.twitter.inject.Test {
  "Balance" should {
    "add up deposit transactions" in {
      val accountInformation = AccountStatement(1, List(Open(1), DepositCash(1, 10), DepositCash(1, 20)))
      assertResult(30)(accountInformation.balance)
    }
  }
  "Balance" should {
    "add up transfer transaction amounts where target is current account" in {
      val accountInformation = AccountStatement(1, List(Open(1), Transfer(0, 1, 10.5), Transfer(0, 1, 10.1)))
      assertResult(20.6)(accountInformation.balance)
    }
  }

  "Balance" should {
    "subtract transfer transaction amounts where source is current account" in {
      val accountInformation = AccountStatement(1, List(Open(1), Transfer(0, 1, 10), Transfer(1, 0, 9)))
      assertResult(1)(accountInformation.balance)
    }
  }

  "Balance" should {
    "sum up all transactions" in {
      val accountInformation = {
        AccountStatement(1, List(
          Open(1),
          Transfer(0, 1, 10),
          DepositCash(1, 3),
          Transfer(1, 0, 9),
          Transfer(1, 0, 1),
          DepositCash(1, 999)
        ))
      }
      assertResult(1002)(accountInformation.balance)
    }
  }
}


