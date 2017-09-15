package com.moneytransfer.tests.apitests

case class ApiErrorResponseResult(e: String) {
  val success = false;
  val failure = true;
}
