package com.moneytransfer.accounts

import com.fasterxml.jackson.annotation.JsonIgnore
import com.moneytransfer.accounts.TypeAliases.{AccountId, Money}

case class AccountStatement(accountId: AccountId, @JsonIgnore history: List[Transaction])
{
  lazy val balance = history.map {
    case deposit: DepositCash => deposit.amount
    case transfer: Transfer if transfer.fromAccountId    == accountId => -transfer.amount
    case transfer: Transfer if transfer.accountId == accountId =>  transfer.amount
    case _ => 0 : Money
  }.sum
  // Accounts with empty history should not happen, there is always the Open transaction, so not handling that case
}
