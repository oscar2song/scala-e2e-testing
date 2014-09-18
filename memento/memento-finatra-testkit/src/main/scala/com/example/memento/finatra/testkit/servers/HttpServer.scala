package com.example.memento.finatra.testkit.servers

import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.finagle.http.Http
import com.twitter.finagle.Service
import com.twitter.util.{Await, Future}
import java.net.InetSocketAddress
import org.jboss.netty.handler.codec.http.{HttpRequest, HttpResponse}

abstract class HttpServer(port: Int) {

  private var server: Option[Server] = None

  private lazy val service = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest): Future[HttpResponse] = Future(onRequest(request))
  }

  protected def onRequest(request: HttpRequest): HttpResponse

  def start(): Unit = {
    val s = ServerBuilder()
      .codec(Http())
      .bindTo(new InetSocketAddress(port))
      .name("HttpServer")
      .build(service)

    server = Some(s)
  }

  def stop(): Unit = {
    Await.result(service.close())
    server.map(s => Await.result(s.close()))
  }
}
