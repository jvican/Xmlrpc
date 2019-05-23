package xmlrpc

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding._
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.{FromResponseUnmarshaller, Unmarshal}
import akka.stream.Materializer
import akka.util.Timeout
import xmlrpc.protocol._

import scala.concurrent.{ExecutionContext, Future}
import scala.xml.NodeSeq
import akka.http.scaladsl.model.headers.HttpCredentials

/**
  * This is the client api to connect to the Xmlrpc server. A client can send any request
  * and he will receive a response. A request is a method call and a response is the result of
  * that method in the server or a fault.
  *
  * The configuration of the Server is a Uri, make sure you have this implicit in context
  * before calling invokeMethod.
  *
  */
object Xmlrpc {

  import XmlrpcProtocol._

  case class XmlrpcServer(fullAddress: String, credentials: Option[HttpCredentials] = None) {
    def uri: Uri = Uri(fullAddress)
  }

  def invokeMethod[P: Datatype, R: Datatype](name: String, parameter: P = Void)
                                            (implicit xmlrpcServer: XmlrpcServer,
                                             as: ActorSystem,
                                             ma: Materializer,
                                             ec: ExecutionContext,
                                             fc: Timeout): XmlrpcResponse[R] = {

    import XmlrpcResponse.AkkaHttpToXmlrpcResponse

    def unmarshall[A](f: Future[HttpResponse])(implicit um: FromResponseUnmarshaller[A]): Future[A] =
      f.flatMap(Unmarshal(_).to[A])


    val requestBody: NodeSeq = writeXmlRequest(name, parameter)
    val requestBodyWithHeader: String = """<?xml version="1.0"?>""" + requestBody.toString
    val request: HttpRequest = {
      val base = Post(xmlrpcServer.uri, requestBody)
      xmlrpcServer.credentials.map(base.addCredentials).getOrElse(base)
    }

    try {
      (Http().singleRequest(request) ~> unmarshall[NodeSeq]).asXmlrpcResponse[R]
    } catch {
      case t: Throwable => XmlrpcResponse(ConnectionError("An exception has been thrown by Spray", Some(t)).failures)
    }
  }
}
