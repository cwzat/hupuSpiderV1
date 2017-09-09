package com.neo.sk.hupuSpider.service

import akka.actor.{Actor, Props}
import com.neo.sk.hupuSpider.common.AppSettings
import com.neo.sk.hupuSpider.models.Dao.postTableDao
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConversions._
import com.neo.sk.hupuSpider.service.transformTool._
import org.slf4j.LoggerFactory
import com.neo.sk.hupuSpider.models.Dao.postTableDao._

//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import com.neo.sk.hupuSpider.common.AppSettings.taskDelay
import com.neo.sk.hupuSpider.service.transformTool._
import org.apache.http.{Header, HttpStatus}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.message.BasicHeader
import org.apache.http.util.EntityUtils

import scala.collection.JavaConverters._

/**
  * Created by cwz on 2017/8/21.
  */
class GetAreaPostUrl extends Actor {
  private final val logger = LoggerFactory.getLogger(getClass)
  private val postUrlQueue = scala.collection.mutable.Queue[String]()
  private var getPostContentID = ""
  private var curAreaUrl = ""
  private var boardName = ""
  private var areaName = ""
  private var stateReq = ""
  private var isIncrease = 0
  private var maxPostId = -1.toLong

  override def receive: Receive = {
    case GetAreaPostUrlContentTrans(cur) =>
      curAreaUrl = cur
      dealContentQue(cur)
    case GetAreaPostUrlCommentTrans(cur) =>
      curAreaUrl = cur
      dealCommentQue(cur)
    case GetAreaPostUrlTrans(l, board, childCount, state) => {
      getPostContentID = childCount
      boardName = board
      try {
        val tmp = l.split("\\*\\*\\*")
        areaName = tmp(1)
      }
      catch {
        case e: Exception =>
          logger.info("拿分区失败" + board)
      }
      stateReq = state
      getAreaPostUrlFun(GetAreaPostUrlTrans(l, board, childCount, state))
    }
    case _ =>
      println("getAreaPostUrl---error")
      context.stop(self)

  }

  def getAreaPostUrlFun(s: GetAreaPostUrlTrans) = {
    var areaUrl = s.l.split("\\*\\*\\*")(0)
    curAreaUrl = areaUrl + "-postdate-1"

    if (stateReq == "content") {
      //写内容

      searchMaxId(boardName, areaName).onComplete {
        case Success(r) =>
          try {
            maxPostId = r.get
          }
          catch {
            case e: Exception =>
          }
        case Failure(e) =>
          logger.info("获取帖子最大编码失败" + e)
      }

      dealContentQue(curAreaUrl)
    }
    else {
      dealCommentQue(curAreaUrl)
    }

  }

  def dealCommentQue(areaUrl: String) = {

    if (postUrlQueue.isEmpty) {
      var doc: Document = null
      try {
        doc = Jsoup.connect(areaUrl).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
          .timeout(AppSettings.jsoupTimeOut).get()

      }
      catch {
        case e: Exception =>
          //println(areaUrl + "out")
          logger.info("dddddddddddddd" + e)
      } //连接板块分区url成功
      if (doc != null) {
        if (doc.select("#ajaxtable > div.show-list > ul > li > div.titlelink.box").size() == 0) {
          //当前页面是空的 代表帖子该分区已经全部写入完成
          //logger.info("该分区已经全部写完" + boardName + "-" + areaName)
        }
        else {
          //当前页面还有帖子可以写入
          //println("该分区还没全部写完" + boardName + "-" + "areaName" + curAreaUrl)
          for (j <- 1 until doc.select("#ajaxtable > div.show-list > ul > li > div.titlelink.box").length) {
            val i = doc.select("#ajaxtable > div.show-list > ul > li > div.titlelink.box")(j)
            val postUrl = "https://bbs.hupu.com" + i.select("a").last().attr("href") //拿到的是每个帖子的url
            postUrlQueue += postUrl
          } //写的是当前页面的所有帖子的url 队列保存的是当前分区页面的帖子的url
          while (postUrlQueue.nonEmpty) {
            val h = postUrlQueue.dequeue() // 帖子的url
            val tmp = context.child("GetPostInfo" + s"$getPostContentID").getOrElse {
              //              Count.c += 1
              //              println("c================",Count.c)
              context.actorOf(Props[GetPostInfo], name = "GetPostInfo" + s"$getPostContentID")
            } ! TalkGetContentOrComment(h, boardName, areaName, stateReq, getPostContentID)
            //            context.system.scheduler.schedule(0.5.seconds,5.seconds,tmp,
            //              TalkGetContentOrComment(h, boardName, areaName, stateReq, getPostContentID))
            //Thread.sleep(1000)
          }

          if (postUrlQueue.isEmpty) {
            //认为当前页面的帖子已经完全发送出去了
            val pattern = "(https:\\/\\/bbs\\.hupu\\.com\\/\\S+?\\-postdate\\-)([0-9]+)".r
            logger.debug(s"$curAreaUrl" + "已经全部处理完成")
            val result = pattern.findFirstMatchIn(curAreaUrl).get
            val pre = result.group(1)
            var page = result.group(2).toInt
            page += 1
            val nextpage = page.toString
            val nextAreaUrl = pre + nextpage
            context.self ! GetAreaPostUrlCommentTrans(nextAreaUrl)
          }
        }
      }
    }
  }

  def dealContentQue(areaUrl: String) = {
    if (postUrlQueue.isEmpty) {
      var doc: Document = null
      try {
        doc = Jsoup.connect(areaUrl).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
          .timeout(AppSettings.jsoupTimeOut).get()

      }
      catch {
        case e: Exception =>
          logger.info("dddddddddddddd" + e)
      } //连接板块分区url成功
      val con = context
      val me = self
      if (doc != null) {
        if (doc.select("#ajaxtable > div.show-list > ul > li > div.titlelink.box").size() == 0) {
          //当前页面是空的 代表帖子该分区已经全部写入完成
          //logger.info("该分区已经全部写完" + boardName + "-" + areaName)
        }
        else {
          //当前页面还有帖子可以写入
          //println("该分区还没全部写完" + boardName + "-" + "areaName" + curAreaUrl)
          for (j <- 1 until doc.select("#ajaxtable > div.show-list > ul > li > div.titlelink.box").length) {
            val i = doc.select("#ajaxtable > div.show-list > ul > li > div.titlelink.box")(j)
            val postUrl = "https://bbs.hupu.com" + i.select("a").last().attr("href") //拿到的是每个帖子的url
            val pattern = "(https:\\/\\/bbs.hupu.com\\/[0-9]+?)(-[0-9]+)?.html".r
            val trueUrl = pattern.findFirstMatchIn(postUrl) match {
              case Some(r) =>
                r.group(1) + ".html"
              case None =>
                ""
            }
            postUrlQueue += trueUrl
          } //写的是当前页面的所有帖子的url 队列保存的是当前分区页面的帖子的url
          while (postUrlQueue.nonEmpty) {
            val h = postUrlQueue.dequeue()
            //logger.info("hhhhhhhhhhhhhhhh"+h)
            // 帖子的url
            //            val pattern = "https:\\/\\/bbs\\.hupu\\.com\\/([0-9]+).html".r
            //            val curId = pattern.findFirstMatchIn(h) match {
            //              case Some(r) =>
            //                r.group(1).toString
            //              case None =>
            //                logger.info("拿帖子的url 失败" + h)
            //                ""
            //            }

            //if (curId > maxPostId) {
            //logger.info("最新帖子" + " " + boardName + " " + areaName + " " + h)
            val tmpT = context.child("GetPostInfo" + s"$getPostContentID").getOrElse {
              context.actorOf(Props[GetPostInfo], name = "GetPostInfo" + s"$getPostContentID")
            } ! TalkGetContentOrComment(h, boardName, areaName, stateReq, getPostContentID)
            //}
            //            else {
            //              postUrlQueue.clear()
            //              isIncrease = 1 //更新的已经更新完了  可以停止了
            //              //context.stop(self)
            //
            //            }


          }
          if (postUrlQueue.isEmpty && isIncrease == 0) {
            //认为当前页面的帖子已经完全发送出去了
            val pattern = "(https:\\/\\/bbs\\.hupu\\.com\\/\\S+?\\-postdate\\-)([0-9]+)".r
            val result = pattern.findFirstMatchIn(curAreaUrl).get
            val pre = result.group(1)
            var page = result.group(2).toInt
            page += 1
            val nextpage = page.toString
            val nextAreaUrl = pre + nextpage
            if(page <= 10)
              context.self ! GetAreaPostUrlContentTrans(nextAreaUrl)
          }
        }

      }


    }


  }


}







