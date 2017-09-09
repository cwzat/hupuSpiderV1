package com.neo.sk.hupuSpider.service



import akka.actor.Actor
import com.neo.sk.hupuSpider.common.AppSettings
import com.neo.sk.hupuSpider.models.Dao.commentTableDao
import com.neo.sk.hupuSpider.service.transformTool.UpdateComDoTrans
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor


//import hupuSpider.models.Dao.commentTableDao._

import scala.collection.JavaConversions._
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
      if(doc != null)
        getComment(doc,url,areaName,boardName)
    }

    case _=>
      println("失败"+"UpdateComDo")
  }
}
