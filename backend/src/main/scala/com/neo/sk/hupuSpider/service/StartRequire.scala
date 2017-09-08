package com.neo.sk.hupuSpider.service

import akka.actor.{Actor, ActorSystem, Props}
import com.neo.sk.hupuSpider.service.transformTool.{StartRequireBoardAreaTrans, StartStopRequireTrans}
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import com.neo.sk.hupuSpider.Boot.executor

/**
  * Created by cwz on 2017/8/31.
  */
class StartRequire extends Actor {
  private val log = LoggerFactory.getLogger(this.getClass)

  def receive = {
    case r: StartStopRequireTrans =>
      if (r.start == "start")
        startRequireFun();
    case _ => log.info("StartRequire", "接收指令失败")
  }

  def startRequireFun() = {
    //println("进入startRequireFun")
    val boardList = List("CBA论坛", "NBA论坛", "中国足球论坛", "国际足球论坛", "综合体育",
      "自建板块", "运动装备", "步行街")

    //    for( board <- boardList ) {
    //      val boardCount = board match {
    //        case "CBA论坛" => 1
    //        case "NBA论坛" => 2
    //        case "中国足球论坛" => 3
    //        case "国际足球论坛" => 4
    //        case "综合体育" => 5
    //        case "自建板块" => 6
    //        case "运动装备" => 7
    //        case "步行街" => 8
    //        case _ => 0
    //      }
    //      val tmp = context.child(s"$boardCount"+"GetArea") getOrElse {
    ////        Count.c += 1
    ////        println("c================",Count.c)
    //        context.actorOf(Props[GetArea],name = s"$boardCount"+"GetArea")
    //      } ! StartRequireBoardAreaTrans(board)
    //      //context.system.scheduler.scheduleOnce(10.seconds,tmp,StartRequireBoardAreaTrans(board))
    //      Thread.sleep(15000)
    //
    //    }
    val tmp = context.child("1" + "GetArea") getOrElse {
      context.actorOf(Props[GetArea], name = "1" + "GetArea")
    } ! StartRequireBoardAreaTrans("运动装备")
  }
}
