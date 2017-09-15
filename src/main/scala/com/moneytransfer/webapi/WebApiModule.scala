package com.moneytransfer.webapi
import com.moneytransfer.accounts.{BankService, SimpleBankService}
import com.twitter.inject.TwitterModule

object WebApiModule extends TwitterModule {
  override def configure(): Unit = {
    bind[BankService].to[SimpleBankService]
  }
}
