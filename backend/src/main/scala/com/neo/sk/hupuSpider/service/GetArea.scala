package com.neo.sk.hupuSpider.service

import akka.actor.{Actor, Props}

import scala.io.Source
import com.neo.sk.hupuSpider.service.transformTool.{GetAreaPostUrlTrans, StartRequireBoardAreaTrans, StartStopRequireTrans, UpdateComTrans}

/**
  * Created by cwz on 2017/8/21.
  */
class GetArea extends Actor{
  def receive = {
    case r : StartRequireBoardAreaTrans => getEachArea(r)
    case _=>
      context.stop(self)
      println("start error")
  }
  def getEachArea(r:StartRequireBoardAreaTrans) = {
    val board = r.board
    val boardCount = board match {
      case "CBA论坛" => 1
      case "NBA论坛" => 2
      case "中国足球论坛" => 3
      case "国际足球论坛" => 4
      case "综合体育" => 5
      case "自建板块" => 6
      case "运动装备" => 7
      case "步行街" => 8
    }
    val file = Source.fromFile("/Users/cwz/Documents/scala/hupuSpider/backend/src/main/RootFile/"+s"$board"+"/areaUrl.txt")
    val lines = file.getLines()
    //file.close()
    var i = 0
//    val tmp = lines.toList.head
    for( l <- lines ){
      i += 1
      var childCount = boardCount+"-"+i.toString
      val areaName = l.split("\\*\\*\\*").length match {
        case 2 =>
          l.split("\\*\\*\\*")(1)
        case _=>
          println("lllllllllllllllllllllllll")
          board
      }
      val getAreaPostUrlCom = context.actorOf(Props[UpdateCom],name =  "UpdateCom" +s"$childCount")
      getAreaPostUrlCom ! UpdateComTrans(board,areaName,childCount)

//      val getAreaPostUrlCon = context.actorOf(Props[GetAreaPostUrl],name =  "getAreaPostUrlContent" +s"$childCount")
//      getAreaPostUrlCon ! GetAreaPostUrlTrans(l,board,childCount,"content")
//      val getAreaPostUrlCom = context.actorOf(Props[GetAreaPostUrl],name =  "getAreaPostUrlComment" +s"$childCount")
//      getAreaPostUrlCom ! GetAreaPostUrlTrans(l,board,childCount,"comment")

//      Count.c += 2
//      println("c================",Count.c)
//    val getAreaPostUrlTmp = context.actorOf(Props[GetAreaPostUrl],name = "getAreaPostUrl"+"1")
//    getAreaPostUrlTmp ! GetAreaPostUrlTrans(tmp,board,"1",r.state)
    }
  }
}
