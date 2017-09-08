//package com.neo.sk.analyse
//
//import java.io.{File, PrintWriter}
//
//import akka.actor.Actor
//import com.neo.sk.hupuSpider.common.AppSettings
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//import akka.actor.{Actor, Props}
//import com.neo.sk.hupuSpider.common.AppSettings
//import com.neo.sk.hupuSpider.models.Dao.postTableDao
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//import org.slf4j.LoggerFactory
//
//import scala.collection.JavaConversions._
//import scala.util.{Failure, Success}
//import com.neo.sk.hupuSpider.service.transformTool._
//import com.neo.sk.hupuSpider.Boot.executor
//import akka.actor.{Actor, Props}
//import com.neo.sk.hupuSpider.common.AppSettings
//import com.neo.sk.hupuSpider.models.Dao.postTableDao
//import org.jsoup.Jsoup
//import org.jsoup.nodes.Document
//
//import scala.collection.JavaConversions._
//import com.neo.sk.hupuSpider.service.transformTool._
//import org.slf4j.LoggerFactory
//import com.neo.sk.hupuSpider.models.Dao.postTableDao._
//import org.apache.http.{Header, HttpStatus}
//import org.apache.http.client.methods.HttpGet
//import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
//import org.apache.http.message.BasicHeader
//import org.apache.http.util.EntityUtils
//
//import scala.io.Source
//
///**
//  * Created by cwz on 2017/9/5.
//  */
//object Analyse {
//  def main(args: Array[String]): Unit = {
//    var authorNum = 0
//    var nan = 0
//    var nv = 0
//    var unknow = 0
//    val fileName = "/Users/cwz/Desktop/author.txt"
//    val source = Source.fromFile(fileName)
//    val lines = source.getLines
//
//    for (line <- lines) {
//      val authorUrl = line
//      //      var doc:Document = null
//      //      try {
//      //        doc = Jsoup.connect(authorUrl).header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
//      //          .timeout(AppSettings.jsoupTimeOut).get()
//      //      }
//      //      catch {
//      //        case e: Exception => e.printStackTrace
//      //        //logger.error("连接帖子失败" + s"$postUrl")
//      //      }
//      val httpHeaders = List[Header](
//        new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"),
//        new BasicHeader("Accept-Encoding", "gzip, deflate"),
//        new BasicHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6"),
//        new BasicHeader("Cache-Control", "max-age=0"),
//        new BasicHeader("Connection", "keep-alive"),
//        new BasicHeader("Upgrade-Insecure-Requests", "1"),
//        new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")
//      )
//      import scala.collection.JavaConverters._
//      val httpClient: CloseableHttpClient = HttpClientBuilder.create().setDefaultHeaders(httpHeaders.asJava).build()
//
//      def fetch(url: String): Option[String] = {
//        try {
//          val request = new HttpGet(url)
//
//          val response = httpClient.execute(request)
//          val statusCode = response.getStatusLine.getStatusCode
//          val entity = response.getEntity
//          val str = EntityUtils.toString(entity, "utf-8")
//          EntityUtils.consume(response.getEntity)
//          response.close()
//
//          if (statusCode == HttpStatus.SC_OK) {
//            Some(str)
//          } else {
//            None
//          }
//        } catch {
//          case e: Exception =>
//            println(s"fetch url:$url error: $e")
//            None
//        }
//      }
//
//      var doc: Document = null
//      fetch(authorUrl).foreach {
//        str =>
//          doc = Jsoup.parse(str)
//      }
//
//      var sex = "未知"
//      try{
//        val tmp = doc.select("span[itemprop=gender]")
//        sex = tmp.text()
//      }
//      catch {
//        case e :Exception =>
//          println("啥也拿不到" + authorUrl)
//      }
//      if(sex == "男")
//        nan += 1
//      else if(sex == "女")
//        nv += 1
//      else
//        unknow += 1
//    }
//    println("nan",nan)
//    println("nv",nv)
//    println("unknow",unknow)
//  }
//
//}
