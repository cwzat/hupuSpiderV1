package com.neo.sk.hupuSpider.service

import akka.actor.{Actor, Props}
import akka.actor.Actor
import org.jsoup.{Connection, Jsoup}
import com.neo.sk.hupuSpider.common.AppSettings
import org.slf4j.LoggerFactory
import akka.actor.Actor
import com.neo.sk.hupuSpider.models.Dao.{commentTableDao, postTableDao}
import com.neo.sk.hupuSpider.models.Dao.commentTableDao._
import com.neo.sk.hupuSpider.service.transformTool.{GetPostEachPge, UpdateComDoTrans, UpdateComTrans}
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

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
  private final val logger = LoggerFactory.getLogger(getClass)
  override def receive: Receive = {
    case rec: UpdateComTrans => {
      val boardName = rec.boardName
      val areaName = rec.areaName
      postTableDao.getAreaConPs(boardName, areaName).onComplete {
        case Success(r) => {
          for (url <- r) {
            val pattern = "https:\\/\\/bbs.hupu.com\\/([0-9]+?)(-[0-9]+)?.html".r
            val postId = pattern.findFirstMatchIn(url) match {
              case Some(r) =>
                r.group(1).toString
              case None =>
                ""
            }
            var doc:Document = null
//            val doc = Jsoup.connect(url).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
//              .timeout(AppSettings.jsoupTimeOut) match {
//              case c:Connection =>
//                c.get()
//              case _ =>
//                logger.info("帖子被删除"+url)
//                null
//            }
            try {
              val con = Jsoup.connect(url).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
                .timeout(AppSettings.jsoupTimeOut)
              val statuscode = Jsoup.connect(url).followRedirects(true).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
                .timeout(AppSettings.jsoupTimeOut).execute().statusCode()
              if(statuscode==404){
                logger.info("帖子被删除"+url)
              }
              else{
                doc = Jsoup.connect(url).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
                  .timeout(AppSettings.jsoupTimeOut).get()
              }
            }
            catch {
              case e: Exception =>

                doc = null
            }
            var maxPage = 1
            try{
              if(doc!=null && doc.getElementsByClass("page").isEmpty){
                maxPage = 1
              }
              else if(doc != null){
                val len = doc.getElementsByClass("page").select("a").length
                doc.getElementsByClass("page").select("a").get(len-2).text().toInt
              }
            }
            catch {
              case e:Exception =>
                e.printStackTrace()
            }

            //读当前页面的
            if(maxPage!=1) {
              getComment(doc,url,areaName,boardName)
              for(idx <- 2 to maxPage){
                val up = context.child("UpdateComDo" + s"$idx").getOrElse(
                  context.actorOf(Props[UpdateComDo], name = "UpdateComDo" + s"$idx")
                )
                val nextUrl = "https://bbs.hupu.com/"+ postId + "-" + idx + ".html"
                context.system.scheduler.scheduleOnce(3.seconds, up, UpdateComDoTrans(boardName, areaName, nextUrl))
                // 当前页面发送完 下一页
              }
            }
            else{
              getComment(doc,url,areaName,boardName)
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

