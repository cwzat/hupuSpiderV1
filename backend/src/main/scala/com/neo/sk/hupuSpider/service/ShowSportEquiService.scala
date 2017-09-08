package com.neo.sk.hupuSpider.service

/**
  * Created by cwz on 2017/9/6.
  */
import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date

import scala.concurrent.{Await, Future}
import akka.actor.{Actor, Props}
import org.slf4j.LoggerFactory
import akka.http.scaladsl.server.Directives._
import com.neo.sk.hupuSpider.service.transformTool.{StartStopRequireTrans, StopTrans}
import com.neo.sk.utils.CirceSupport
import akka.http.scaladsl.server
import com.neo.sk.hupuSpider.models.Dao.{commentTableDao, postTableDao}

import scala.concurrent.duration._
import scala.util.{Failure, Success}

import com.neo.sk.hupuSpider.Boot.executor


object ShowSportEquiService {

  private val log = LoggerFactory.getLogger(this.getClass)

}

trait ShowSportEquiService extends CirceSupport with ServiceUtils{
  import com.neo.sk.hupuSpider.ptcl
  import ShowSportEquiService.log
  import io.circe.generic.auto._
  import io.circe._
  import com.neo.sk.hupuSpider.Boot.{system,startOrStop}
  import scala.concurrent.duration._


  val showAreaRouter = pathPrefix("showArea") {
    (path("hello") & get){
      getFromResource("html/index.html")
    } ~ (path("postInfo") & get){
      getFromResource("html/index.html")
    } ~ (path("showSportEqui") & post) {
            entity(as[Either[Error, ptcl.ShowAreaReq]]) {
              case Right(p) =>{
                val com = for{
                  post <- postTableDao.getAreaPostInfo("运动装备","运动装备")
                  comm <- commentTableDao.getAreaComment("运动装备","运动装备")
                } yield {
                  complete(ptcl.ShowAreaRsp(post,comm))
                }
                dealFutureResult(com)
              }
              case Left(error) =>
                log.warn(s"some error: $error")
                complete(ptcl.ErrorRsp(1, s"error: $error"))

            }
    } ~ (path("getPostInfo") & post){
      entity(as[Either[Error, ptcl.ShowAreaReq]]) {
        case Right(p) =>{
          val com = for{
            post <- postTableDao.getAreaPostInfo("运动装备","运动装备")
            comm <- commentTableDao.getAreaComment("运动装备","运动装备")
          } yield {
            complete(ptcl.ShowAreaRsp(post,comm))
          }
          dealFutureResult(com)
        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    }

  }

}

