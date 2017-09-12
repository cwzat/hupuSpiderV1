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

trait ShowSportEquiService extends CirceSupport with ServiceUtils {

  import com.neo.sk.hupuSpider.ptcl
  import ShowSportEquiService.log
  import io.circe.generic.auto._
  import io.circe._
  import com.neo.sk.hupuSpider.Boot.{system, startOrStop}
  import scala.concurrent.duration._


  val showAreaRouter = pathPrefix("showArea") {
    (path("hello") & get) {
      getFromResource("html/index.html")
    } ~ (path("postInfo") & get) {
      getFromResource("html/index.html")
    } ~ (path("sportEqui") & post) {
        //运动装备
        entity(as[Either[Error, ptcl.ShowAreaReq]]) {
          case Right(p) => {
            val now1 = new Date()
            val df1 = DateFormat.getDateTimeInstance()
            println("请求后台sportEqui发送area"+ df1.format(now1))
            val com = for {
              length <- postTableDao.getAreaPostLength("运动装备", "运动装备")
              post <- postTableDao.getAreaPostInfo("运动装备", "运动装备")
            } yield {
              complete(ptcl.ShowAreaRsp(post.reverse,length))
            }
            dealFutureResult(com)
          }
          case Left(error) =>
            log.warn(s"some error: $error")
            complete(ptcl.ErrorRsp(1, s"error: $error"))

        }
      } ~ (path("shoppingGoods") & post) {
      entity(as[Either[Error, ptcl.ShowAreaReq]]) {
        case Right(p) => {
          val now1 = new Date()
          val df1 = DateFormat.getDateTimeInstance()
          println("请求后台shoppinggoogs发送area"+ df1.format(now1))
          val com = for {
            length <- postTableDao.getAreaPostLength("运动装备", "交易区(新品)")
            post <- postTableDao.getAreaPostInfo("运动装备", "交易区(新品)")
          } yield {
            complete(ptcl.ShowAreaRsp(post.reverse,length))
          }
          dealFutureResult(com)
        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    }~ (path("buxingjie") & post) {
      entity(as[Either[Error, ptcl.ShowAreaReq]]) {
        case Right(p) => {
          val now1 = new Date()
          val df1 = DateFormat.getDateTimeInstance()
          println("请求后台buxingjie发送area"+ df1.format(now1))
          val com = for {
            length <- postTableDao.getAreaPostLength("步行街", "步行街主干道")
            post <- postTableDao.getAreaPostInfo("步行街", "步行街主干道")
          } yield {
            complete(ptcl.ShowAreaRsp(post.reverse,length))
          }
          dealFutureResult(com)
        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    }~ (path("shihuhu") & post) {
      entity(as[Either[Error, ptcl.ShowAreaReq]]) {
        case Right(p) => {
          val now1 = new Date()
          val df1 = DateFormat.getDateTimeInstance()
          println("请求后台shiuhu发送area"+ df1.format(now1))
          val com = for {
            length <- postTableDao.getAreaPostLength("NBA论坛", "湿乎乎的话题")
            post <- postTableDao.getAreaPostInfo("NBA论坛", "湿乎乎的话题")
          } yield {
            complete(ptcl.ShowAreaRsp(post.reverse,length))
          }
          dealFutureResult(com)
        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    }~ (path("postCom") & post) {
      entity(as[Either[Error, ptcl.PostComReq]]) {
        case Right(p) => {
          val now1 = new Date()
          val df1 = DateFormat.getDateTimeInstance()
          println("请求后台postCom发送area"+ df1.format(now1))
          val com = for {
            res <- commentTableDao.getPostCom(p.postId)
          } yield {
            complete(ptcl.PostComRsp(res))
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

