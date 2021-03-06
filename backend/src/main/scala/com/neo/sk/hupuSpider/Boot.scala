package com.neo.sk.hupuSpider

import akka.actor.{ActorSystem, Props}
import akka.dispatch.MessageDispatcher
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.neo.sk.hupuSpider.service.{HttpService, StartTimer}

import scala.language.postfixOps
import scala.util.{Failure, Success}

/**
  * User: Taoz
  * Date: 11/16/2016
  * Time: 1:00 AM
  */
object Boot extends HttpService {


  import com.neo.sk.hupuSpider.common.AppSettings._

  import concurrent.duration._


  override implicit val system = ActorSystem("helloSystem", config)
  // the executor should not be the default dispatcher.
  override implicit val executor: MessageDispatcher =
    system.dispatchers.lookup("akka.actor.my-blocking-dispatcher")

  override implicit val materializer = ActorMaterializer()

  override val timeout = Timeout(20 seconds) // for actor asks

  val log: LoggingAdapter = Logging(system, getClass)
  implicit val startOrStop = system.actorOf(Props[StartTimer],name = "StartTimer")



  def main(args: Array[String]) {
    log.info("Starting.")

//    val nba = system.actorOf(Props[GetArea],name = "getNbaArea")
//    nba ! StartRequire("NBA论坛","content")
//    val cba = system.actorOf(Props[GetArea],name = "getCbaArea")
//    cba ! StartRequire("CBA论坛","content")
//    val chinaFootball = system.actorOf(Props[GetArea],name = "getChinaFootBallArea")
//    chinaFootball ! StartRequire("中国足球论坛","content")
//    val intenFootBall = system.actorOf(Props[GetArea],name = "getIntenFootBallArea")
//    intenFootBall ! StartRequire("国际足球论坛","content")
//    val tiyu = system.actorOf(Props[GetArea],name = "getTiyuArea")
//    tiyu ! StartRequire("综合体育","content")
//    val own = system.actorOf(Props[GetArea],name = "getOwnArea")
//    own ! StartRequire("自建板块","content")
//    val buxingjie = system.actorOf(Props[GetArea],name = "getBuxingjieArea")
//    buxingjie ! StartRequire("步行街","content")
//    val zhuanbei = system.actorOf(Props[GetArea],name = "getZhuangBeiArea")
//    zhuanbei ! StartRequire("运动装备","content")

    val binding = Http().bindAndHandle(routes, httpInterface, httpPort)
    binding.onComplete {
      case Success(b) ⇒
        val localAddress = b.localAddress
        println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
      case Failure(e) ⇒
        println(s"Binding failed with ${e.getMessage}")
        system.terminate()
        System.exit(-1)
    }
  }



}
