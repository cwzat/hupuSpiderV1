package com.neo.sk.hupuSpider.service
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



/**
  * Created by cwz on 2017/8/18.
  */
object StartService {

  private val log = LoggerFactory.getLogger(this.getClass)

}

trait StartService extends CirceSupport with ServiceUtils{
  import com.neo.sk.hupuSpider.ptcl
  import StartService.log
  import io.circe.generic.auto._
  import io.circe._
  import com.neo.sk.hupuSpider.Boot.{system,startOrStop}
  import scala.concurrent.duration._


  val numRouter = pathPrefix("start") {
    (path("hello") & get){
      getFromResource("html/index.html")
    } ~ (path("start") & post) {
      entity(as[Either[Error, ptcl.StartReq]]) {
        case Right(p) =>{
          val com = for{
            postL <- postTableDao.lengthPost
            commentL <- commentTableDao.lengthComment
          } yield {
            Count.postLength = postL
            Count.commentLength = commentL
            complete(ptcl.NumRsp(postL,commentL))
          }
//          system.scheduler.scheduleOnce(8.seconds,startOrStop,StartStopRequireTrans("start"))
//          context.system.scheduler.schedule(0 second,10800.seconds,tmp,r)
//          start ! StartRequireTrans("start")
          dealFutureResult(com)
        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    } ~ (path("lookNum") & post ){
      entity(as[Either[Error, ptcl.LookNumReq]]) {
        case Right(p) =>{
          val now1 = new Date()
          val df1 = DateFormat.getDateTimeInstance()
          log.error("请求收到area"+ df1.format(now1))
          val pl = Count.postLength
          val cl = Count.commentLength
          complete(ptcl.NumRsp(pl,cl))
          //complete(ptcl.NumRsp(0,0))

        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    } ~ (path("stop") & post ){
      entity(as[Either[Error, ptcl.StopReq]]) {
        case Right(p) =>{
          startOrStop ! StartStopRequireTrans("stop")
          complete(ptcl.StopRsp())
        }
        case Left(error) =>
          log.warn(s"some error: $error")
          complete(ptcl.ErrorRsp(1, s"error: $error"))

      }
    }

  }
}

