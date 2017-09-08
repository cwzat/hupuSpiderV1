package com.neo.sk.hupuSpider.service

import akka.actor.{Actor, Props}
import akka.actor.Actor
import org.jsoup.Jsoup
import com.neo.sk.hupuSpider.common.AppSettings
import org.slf4j.LoggerFactory
import akka.actor.Actor
import com.neo.sk.hupuSpider.models.Dao.{commentTableDao, postTableDao}
import com.neo.sk.hupuSpider.models.Dao.commentTableDao._
import com.neo.sk.hupuSpider.service.transformTool.{GetPostEachPge, UpdateComDoTrans, UpdateComTrans}
import org.jsoup.nodes.Document

import scala.util.control.Breaks
import scala.concurrent.duration._
//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor


//import hupuSpider.models.Dao.commentTableDao._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}

import scala.util.{Failure, Success}

/**
  * Created by cwz on 2017/9/7.
  */
class UpdateCom extends Actor {
  override def receive: Receive = {
    case r: UpdateComTrans => {
      val boardName = r.boardName
      val areaName = r.areaName
      val count = r.count
      postTableDao.getAreaConPs(boardName, areaName).onComplete {
        case Success(r) => {
          for (url <- r) {
            var postiveUrl = url
            var idx = 1
            val loop = new Breaks()
            loop.breakable {
              //读当前页面的
              if(Count.isConnected(postiveUrl)){
                val up = context.child("UpdateComDo" + s"$idx").getOrElse(
                  context.actorOf(Props[UpdateComDo], name = "UpdateComDo" + s"$idx")
                )
                context.system.scheduler.scheduleOnce(1.second,up,UpdateComDoTrans(boardName,areaName,postiveUrl))
                // 当前页面发送完 下一页
                idx += 1
                val pattern = "https:\\/\\/bbs.hupu.com\\/([0-9]+?)(-[0-9]+)?.html".r
                val postId = pattern.findFirstMatchIn(url) match {
                  case Some(r) =>
                    r.group(1).toString
                  case None =>
                    ""
                }
                postiveUrl = "https://bbs.hupu.com/"+postId+"-"+idx+".html"
                println(postiveUrl)

              }
              else {
                loop.break()
              }


            }
          }
        }
        case Failure(e) =>

      }

    }
    case _ =>
      println("UpdateCom" + "error")
  }
}

