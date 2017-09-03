package com.neo.sk.hupuSpider.service

import java.io.{File, PrintWriter}

import io.circe.generic.auto._
import io.circe.parser._
import org.jsoup.Jsoup

/**
  * Created by cwz on 2017/8/18.
  */
object GetAreaUrl extends App {
  val testUrl = areaRootSource.areaRoot10
  new File("/Users/cwz/Documents/scala/spider/src/main/RootFile/站务管理板块").mkdir()
  val writer = new PrintWriter(new File("/Users/cwz/Documents/scala/spider/src/main/RootFile/站务管理板块/areaUrl.txt"))

  val doc = Jsoup.connect(testUrl).ignoreContentType(true).get()
  val content = doc.select("body").text()

  case class areaUrlAndName(url: String, fname: String)

  case class Area1(data: List[areaUrlAndName])

  val decodeContent = decode[Area1](content)
  decodeContent match {
    case Right(r) =>
      for (i <- r.data) {
        //          areaNameAndUrlList.areaNameList1 = i.fname :: areaNameAndUrlList.areaNameList1
        //          areaNameAndUrlList.areaUrlList1 = i.url :: areaNameAndUrlList.areaUrlList1
        //          areaUrlNameMap.areaUrlNameMap1 += (i.url -> i.fname)
        writer.write("https:" + i.url + "***" + i.fname)
        writer.write("\n")
      }
      writer.close()
    case Left(e) =>
      println("error" + e)
  }

  //println(areaNameAndUrlList.areaNameList1,areaNameAndUrlList.areaUrlList1,areaUrlNameMap.areaUrlNameMap1)

  //    val getCurPagePostUrlTmp = context.actorOf(Props[getCurPagePostUrl])
  //    transAreaUrlList.urlList = areaNameAndUrlList.areaUrlList1
  //
  //    if(areaUrlNameMap.areaUrlNameMap1.nonEmpty)
  //      transAreaUrlList.state = "已经得到该板块下所有分区的url"
  //
  //    getCurPagePostUrlTmp ! transAreaUrlList


}

