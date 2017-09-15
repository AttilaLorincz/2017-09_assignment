package com.moneytransfer.accounts

import javax.inject.Inject

import com.moneytransfer.accounts.TypeAliases._
import scala.collection.mutable.ListBuffer
import scalaz.{Failure, Success, Validation}

// Note:
// I did not care for performance for this assignment
// This is a very simple, thread-safe implementation with a single source of truth in the ledger
// but having to iterate over the ledger for all operations is not a realistic option

class SimpleBankService @Inject()(private val ledger: ListBuffer[Transaction] = new ListBuffer[Transaction]) extends BankService {

  def GetAccountInformation(accountId: AccountId): Validation[ValidationError, AccountStatement] = {
    ledger.synchronized {
      if (!CheckAccountExists(accountId))
        Failure(ValidationMessages.AccountNotFound(accountId))
      else {
        val history = ledger.filter {
          case transfer: Transfer if transfer.fromAccountId == accountId => true
          case transaction if transaction.accountId == accountId => true
          case _ => false
        }.toList
        Success(AccountStatement(accountId, history))
      }
    }
  }

  def OpenAccount(): Validation[ValidationError, Open] = {
    ledger.synchronized {
      val maxId = (0 +: ledger.filter {
        case _: Open => true
        case _ => false
      }.map(_.accountId)).max
      val openingTransaction: Open = Open(maxId + 1)
      ledger.append(openingTransaction)
      Success(openingTransaction)
    }
  }

  def IntrabankTransfer(fromAccountId: AccountId, toAccountId: AccountId, amount: Money): Validation[ValidationError, Transfer] = {
    if (amount <= 0)
      Failure(ValidationMessages.AmountMustBePositive)
    else if (toAccountId == fromAccountId)
      Failure(ValidationMessages.AccountsMustDiffer)
    else {
      ledger.synchronized {
        if (!CheckAccountExists(toAccountId))
          Failure(ValidationMessages.AccountNotFound(toAccountId))
        else {
          GetAccountInformation(fromAccountId) match {
            case Success(fromAccount) if fromAccount.balance < amount => return Failure(ValidationMessages.InsufficientBalance)
            case Failure(msg) => return Failure(msg)
            case _ =>
          }
          val transferTransaction = Transfer(fromAccountId, toAccountId, amount)
          ledger.append(transferTransaction)
          Success(transferTransaction)
        }
      }
    }
  }

  def Deposit(accountId: AccountId, amount: Money): Validation[ValidationError, DepositCash] = {
    if (amount <= 0)
      Failure(ValidationMessages.AmountMustBePositive)
    else {
      val depositTransaction: DepositCash = DepositCash(accountId, amount)
      ledger.synchronized {
        if (!CheckAccountExists(accountId))
          Failure(ValidationMessages.AccountNotFound(accountId))
        else {
          ledger.append(depositTransaction)
          Success(depositTransaction)
        }
      }
    }
  }

  private def CheckAccountExists(accountId: AccountId) = {
    ledger.exists {
      case openTransaction: Open => openTransaction.accountId == accountId
      case _ => false
    }
  }
}