package com.neo.sk.hupuSpider.service

import java.time.LocalDate

import akka.actor.{Actor, Props}
import com.neo.sk.hupuSpider.common.AppSettings
import com.neo.sk.hupuSpider.models.Dao.postTableDao
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.util.{Failure, Success}
import com.neo.sk.hupuSpider.service.transformTool._
import com.neo.sk.hupuSpider.Boot.executor
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
/**
  * Created by cwz on 2017/8/21.
  */

class GetPostInfo extends Actor {
  private final val logger = LoggerFactory.getLogger(getClass)
  private var postUrl = ""
  private var boardName = ""
  private var areaName = ""
  private var maxPage = -1
  private var id = ""
  private var pageQueue = scala.collection.mutable.Queue[String]()
  private var isReceive = 1

  def receive = {
    case r: TalkGetContentOrComment =>
      if(isReceive==1){
        postUrl = r.url
        boardName = r.boardName
        areaName = r.areaName
        id = r.id
        getPostInfoFun(r.state)
      }
    case _ =>
      println("getPostContent --- error")
  }


  def getPostInfoFun(state: String) = {
    //传进来帖子的url和分区的名称 每一个分区都有一个getPostContentFun
    var doc: Document = null

    try {
      doc = Jsoup.connect(postUrl).header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36")
        .timeout(AppSettings.jsoupTimeOut).get()
    }
    catch {
      case e: Exception => e.printStackTrace
        //logger.error("连接帖子失败" + s"$postUrl")
    }

    state match {
      case "comment" =>
      //爬评论
        var incCom = 0
        postTableDao.getTime(postUrl).onComplete{
          case Failure(e) =>
            println("拿帖子对应的时间失败")
          case Success(r) =>
            val yesDay = LocalDate.now().minusDays(5).toString
            var postDay = yesDay
            try {
              val tmp = r.get(0)
              postDay = r.get(0).split(" ")(0)
            }
            catch {
              case e: Exception =>
                //logger.info("拿帖子发布的时间正则失败")
            }
            if( postDay > yesDay ){
              val pattern = "https:\\/\\/bbs\\.hupu\\.com\\/([0-9]+)\\.html".r
              var postId = "0"
              try{
                val result = pattern.findFirstMatchIn(postUrl)
                val tmp = result.get
                postId = tmp.group(1)//"https://bbs.hupu.com/20029186"
              }
              catch {
                case e: Exception =>
                  //logger.info("拿帖子的编号失败")
              }
              if (doc.select("div[class=page]").select("a").size() == 0) {
                //帖子只有一页
                context.child("GetPostComment" + s"$postId" ) getOrElse {
                  context.actorOf(Props[GetPostComment], name = "GetPostComment" + s"$postId" )
                } ! GetPostEachPge(postUrl,incCom,boardName,areaName)
//                context.system.scheduler.schedule(1.second,2.seconds,tmp,
//                  )

              }
              else {
                val len = doc.getElementsByClass("page").select("a").length
                maxPage = doc.getElementsByClass("page").select("a").get(len - 2).text().toInt
                var idx = maxPage
                while (idx >= 2){
                  var curPage = "https://bbs.hupu.com/" +postId + "-" + s"$idx" +".html"
                  val tmp = context.child("GetPostComment" + s"$postId" ) getOrElse {
//                    Count.c += 1
//                    println("c================",Count.c)
                    context.actorOf(Props[GetPostComment], name = "GetPostComment" + s"$postId" )
                  } ! GetPostEachPge(curPage,incCom,boardName,areaName)
//                  context.system.scheduler.schedule(1.second,2.seconds,tmp,
//                   GetPostEachPge(curPage,incCom,boardName,areaName))
                  idx -= 1
                  //Thread.sleep(1000)
                }
                context.child("GetPostComment" + s"$postId" ) getOrElse {
//                  Count.c += 1
//                  println("c================",Count.c)
                  context.actorOf(Props[GetPostComment], name = "GetPostComment" + s"$postId" )
                } ! GetPostEachPge(postUrl,incCom,boardName,areaName)
              }

            }
            else{
              isReceive = 0
            }


        }

      case "content" =>
        getPostContent(doc)
      //爬帖子内容
      case _ =>
        logger.info("我不知道要干什么")
    }

  }


  def getPostContent(doc: Document): Unit = {
    val board = boardName
    val subarea = areaName

    val pattern = "https:\\/\\/bbs\\.hupu\\.com\\/([0-9]+).html".r
    try{
      val result = pattern.findFirstMatchIn(postUrl).get
      val id = result.group(1).toLong
      var postTitle = doc.title()
      var authorInfo = doc.select("div[class=author]").select("div[class=left]")
      var authorName = "unknowName"
      var authorUrl = "unknowUrl"
      var time = ""
      if (authorInfo.size() != 0) {
        val author = authorInfo.first()
        authorUrl = author.select("a").attr("href")
        authorName = author.select("a[class=u]").text()
        time = author.select("span[class=stime]").text()
      }
      var content = postTitle
      try{
        val tmp =  doc.select("div[class=quote-content]")
        content = tmp.first().text()
      }
      catch {
        case e:Exception =>
      }

      postTableDao.insert(board, subarea, id,
        postUrl, postTitle, authorName,
        authorUrl, content, time).onComplete{
        case Failure(e) =>
        case Success(r) =>
          Count.postLength += 1
      }
    }
    catch {
      case e:Exception =>

    }



  }



}

