package com.neo.sk.hupuSpider

import com.neo.sk.hupuSpider.models.Dao.commentTableDao
import org.jsoup.nodes.Document
import akka.actor.Actor
import com.neo.sk.hupuSpider.common.AppSettings
import com.neo.sk.hupuSpider.models.Dao.commentTableDao
import com.neo.sk.hupuSpider.service.transformTool.UpdateComDoTrans
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor


//import hupuSpider.models.Dao.commentTableDao._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}


import scala.util.{Failure, Success}

/**
  * Created by cwz on 2017/9/8.
  */
package object service {
  private final val logger = LoggerFactory.getLogger(getClass)
  def getComment(doc:Document,url:String,areaName:String,boardName:String) ={
    if (doc != null && doc.select("div[class=floor]").nonEmpty ) {
      for (index <- 1 until doc.select("div[class=floor]").length) {
        val i = doc.select("div[class=floor]")(index)
        var commentContext = doc.title()
        val commentId = i.id().toLong
        var isHui = false


        val commentGlobalUrl = url + "#"+ commentId
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
        var replyFloor: Long = -1
        i.select("div[class=quote-content]").select("blockquote").size() match {
          case 0 =>{
            isHui = false
            commentContext = i.select("div[class=quote-content]").text()
            if (commentContext == "")
              commentContext = doc.title()
            replyFloor = 0.toLong
          }
            // 代表他没有引用别人的帖子

          case m: Int =>{
            isHui = true
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
          }

        }
//        if(!isHui){
//          println(url,
//            commentId,
//            commentGlobalUrl,
//            commentFloor,
//            commentContext,
//            commenterId,
//            commenterName,
//            lights,
//            replyFloor,
//            boardName,
//            areaName)
//        }
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
    else{
      println("拿不到评论1111111111111111111111" +  url)
    }
  }
}
