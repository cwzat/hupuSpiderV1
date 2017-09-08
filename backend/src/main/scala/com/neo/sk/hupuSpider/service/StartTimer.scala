package com.neo.sk.hupuSpider.service

import akka.actor.{Actor, Props}
import com.neo.sk.hupuSpider.service.transformTool.{StartStopRequireTrans, StopTrans}

import scala.concurrent.duration._

/**
  * Created by cwz on 2017/9/1.
  */
class StartTimer extends Actor{
  import context.dispatcher
  private var isStop = 0
  def receive = {
    case r : StartStopRequireTrans =>
      if(r.start == "start"){
        isStop = 0
        startTimerFuc(r)
      }
      else if(r.start == "stop"){
        isStop = 1
        startTimerFuc(r)
      }

    case _ =>
      println("StartTimer error")
  }
  def startTimerFuc(r:StartStopRequireTrans) = {
    val tmp = context.child("StartRequire").getOrElse{
      context.actorOf(Props[StartRequire],name = "StartRequire")
    }
    if(isStop == 1){
      println("sssssssssssssssssssssssssssssssssssssssssssssssss")
      context.stop(tmp)
      isStop = 0
    }

    context.system.scheduler.schedule(0.3.seconds,7200.seconds,tmp,r)
  }
}
