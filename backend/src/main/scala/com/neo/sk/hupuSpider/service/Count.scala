package com.neo.sk.hupuSpider.service

import org.jsoup.Jsoup

import scala.util.{Failure, Success, Try}

/**
  * Created by cwz on 2017/8/21.
  */
object Count {
   var postLength = 0
   var commentLength = 0
   var c = -1
   def isConnected(url: String): Boolean = {
      Try(Jsoup.connect(url).get()) match {
         case Failure(e) =>
            false
         case Success(doc) =>
            true

      }
   }
}
