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
//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor


//import hupuSpider.models.Dao.commentTableDao._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}

import scala.util.{Failure, Success}

/**
  * Created by cwz on 2017/9/8.
  */
class UpdateComDo extends Actor{
  def receive = {
    case r:UpdateComDoTrans =>{
      val boardName = r.boardName
      val areaName = r.areaName
      val url = r.url
      var doc: Document = null
      try {
        doc = Jsoup.connect(url).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
          .timeout(AppSettings.jsoupTimeOut).get()
      }
      catch {
        case e: Exception => e.printStackTrace()
          doc = null
      }

      //if the page count overflow, there will be a tag:
      //<meta http-equiv="refresh" content="the url of the last page">
      val ifRefreshTag = doc.select("meta[http-equiv=refresh]")
      if (ifRefreshTag.size() != 0)
      {

      }
      else
      {

      }


      if (doc.select("div[class=floor]").size() != 0) {
        for (index <- 1 until doc.select("div[class=floor]").length) {
          val i = doc.select("div[class=floor]")(index)
          var commentContext = ""
          val commentId = i.id().toLong

          val commentGlobalUrl = "https://bbs.hupu.com/"+i.id() + ".html#" + commentId
          val commentFloor =  i.getElementsByClass("floornum").length match {
            case 0 => 0.toLong
            case _ => i.getElementsByClass("floornum").first().id().toLong
          }
          val commenterId = i.select("div[class=j_u]").attr("uid")
          val commenterName = i.select("div[class=j_u]").attr("uname")
          val lights = i.select("a[class=ilike_icon]").select("span[class=stime]").text() match {
            case "" => 0.toLong
            case _ => i.select("a[class=ilike_icon]").select("span[class=stime]").text().toLong
          }
          var replyFloor: Long = 0
          i.select("div[class=quote-content]").select("blockquote").size() match {
            case 0 =>
              // 代表他没有引用别人的帖子
              commentContext = i.select("div[class=quote-content]").text()
              if (commentContext == "")
                commentContext = doc.title()
              replyFloor = 0
            case m: Int =>
              commentContext = i.select("div[class=quote-content]").first().ownText()
              val replyInfo = i.select("div[class=quote-content]").select("blockquote").text()
              val replyF = replyInfo.split("楼")(0)
              //引用2
              val pattern = "引用([0-9]+)".r

              try {
                replyFloor = pattern.findFirstMatchIn(replyF).get.group(1).toLong
              }
              catch {
                case e: Exception =>
                  replyFloor = 0.toLong
                //logger.info("拿引用的楼层失败" + commentGlobalUrl + "www" + e)
              }
              commentTableDao.insert(url,
                commentId,
                commentGlobalUrl,
                commentFloor,
                commentContext,
                commenterId,
                commenterName,
                lights,
                replyFloor,
                boardName,
                areaName
              ).onComplete {
                case Success(r) =>
                  Count.commentLength += 1
                case Failure(e) =>

              }

          }

        }

      }
      else{
        println("拿不到评论1111111111111111111111" +  url)
        commentTableDao.insert(//TODO magic code
          url,
          0,
          "0",
          0,
          "0",
          "0",
          "0",
          0,
          0,
          "0",
          "0"
        ).onComplete {
          case Success(r) =>
            Count.commentLength += 1
          case Failure(e) =>

        }


      }
    }


    case _=>
      println("失败"+"UpdateComDo")
  }
}
