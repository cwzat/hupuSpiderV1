package com.neo.sk.hupuSpider.service
import com.neo.sk.hupuSpider.models.Dao.commentTableDao

import scala.util.Success
//import scala.concurrent.ExecutionContext.Implicits.global
import com.neo.sk.hupuSpider.Boot.executor


import scala.util.Failure
/**
  * Created by cwz on 2017/9/12.
  */
object Add {
  def main(args: Array[String]): Unit = {
    commentTableDao.getAllPostUrl().onComplete{
      case Failure(e) =>
        println("e" + e.printStackTrace())
      case Success(rec) =>
        val pg = scala.collection.mutable.Queue[String]()
        for ( i <- rec ){
          pg += i
        }
        update()
        def update() :Unit = {
          println("update")
            val url = pg.dequeue()
            val pattern = "https:\\/\\/bbs.hupu.com\\/([0-9]+?)(-[0-9]+)?.html".r
            val id = pattern.findFirstMatchIn(url) match {
              case Some(p) =>
                p.group(1).toLong
              case None =>
                0.toLong
            }
            commentTableDao.updateId(url,id).onComplete {
              case Success(r) =>{
                update()
                println(url+" "+id)
              }
              case Failure(e) =>
                println("e" + e.printStackTrace())
            }

        }

    }
  }

}
