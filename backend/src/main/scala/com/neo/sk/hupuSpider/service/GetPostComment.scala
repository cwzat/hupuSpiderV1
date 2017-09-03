package com.neo.sk.hupuSpider.service

import akka.actor.Actor
import org.jsoup.Jsoup
import com.neo.sk.hupuSpider.common.AppSettings
import org.slf4j.LoggerFactory
import akka.actor.Actor
import com.neo.sk.hupuSpider.models.Dao.commentTableDao
import com.neo.sk.hupuSpider.service.transformTool.GetPostEachPge
import org.jsoup.nodes.Document
//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor


//import hupuSpider.models.Dao.commentTableDao._

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}


/**
  * Created by cwz on 2017/8/24.
  */
class GetPostComment extends Actor {
  private final val logger = LoggerFactory.getLogger(getClass)

  override def receive: Receive = {
    case r: GetPostEachPge =>
        getPostCommentFun(r)
    case _ =>
      logger.info("mmmmmmmmmmmmmmmmmmmmmmm")
  }

  def getPostCommentFun(r: GetPostEachPge) = {
    //println("ggggggggggggggggggggggggg")
    val boardName = r.boardName
    val areaName = r.areaName
    val postUrl = r.curPageUrl
    //println("收到curPage" + postUrl + boardName + areaName)
    var doc: Document = null
    try {
      doc = Jsoup.connect(postUrl).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
        .timeout(AppSettings.jsoupTimeOut).get()
    }
    catch {
      case e: Exception => e.printStackTrace
        println(postUrl, "连接失败")
    }
    if (doc.select("div[class=floor]").size() != 0) {
      for (index <- 1 until doc.select("div[class=floor]").length) {
        val i = doc.select("div[class=floor]")(index)
        var commentContext = ""
        val commentId = i.id().toLong

        val commentGlobalUrl = postUrl + "#" + commentId
        val commentFloor = i.getElementsByClass("floornum").first().id().toLong
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
            if(commentContext == "")
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
            commentTableDao.insert(postUrl,
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
            ).onComplete{
              case Success(r) =>
                Count.commentLength += 1
              case Failure(e) =>

            }

        }

      }

    }
    else {
      //logger.info("获取评论所有内容失败" + postUrl)
      //logger.info("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
    }
  }

}
