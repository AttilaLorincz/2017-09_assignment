package com.moneytransfer.webapi

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.{CommonFilters, LoggingMDCFilter, TraceIdMDCFilter}
import com.twitter.finatra.http.routing.HttpRouter

class WebApiServer extends HttpServer {
  override val modules = Seq(WebApiModule)
  override def defaultFinatraHttpPort = ":9999"

  override def configureHttp(router: HttpRouter) {

    router
      .filter[LoggingMDCFilter[Request, Response]]
      .filter[TraceIdMDCFilter[Request, Response]]
      .filter[CommonFilters]
      .add[AccountController]
  }
}

object WebApiServerMain extends WebApiServer
