package com.neo.sk.hupuSpider.frontend

/**
  * Created by cwz on 2017/9/1.
  */
import java.text.{DateFormat, SimpleDateFormat}

import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc}
import com.neo.sk.hupuSpider.ptcl
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.html.Div
import java.util.concurrent.TimeUnit

import com.github.nscala_time.time.Imports._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.Date
import scalatags.JsDom.short._
class NumOnServer extends Component[Div]{
  private var tmpStart = div().render
  private var tmpLookNum = div().render
  private val state = p().render

  def startFun(start:String) = {
    val now1 = new Date().toTimeString()
    println("请求发送start"+ now1)
    val url = Routes.NumRoute.startTrueRoot //hupuSpider/start/start
    val data = ptcl.StartReq(start).asJson.noSpaces
    Http.postJsonAndParse[ptcl.StartRsp](url, data).map{
      rsp =>
        //后台返回的处理
        if(rsp.errCode == 0) {
          println("爬虫已经开始")
          val postLength = h2("主贴之前的数目是"+rsp.lengthPost).render
          val commentLength = h2("回帖之前的数目是"+rsp.lengthComment).render
          val total = rsp.lengthComment + rsp.lengthPost
          val totalLength = h2("总计"+total).render

//          val postLengthL = h2("主贴现在的数目是"+rsp.lengthPost).render
//          val commentLengthL = h2("回帖现在的数目是"+rsp.lengthComment).render
//          val totalL = rsp.lengthComment + rsp.lengthPost
//          val totalLengthL = h2("总计"+totalL).render

          state.innerHTML = ""
          state.appendChild(p("工作中").render)
          tmpStart.innerHTML = ""
          tmpStart.appendChild(postLength)
          tmpStart.appendChild(commentLength)
          tmpStart.appendChild(totalLength)
          tmpLookNum.innerHTML = ""

//          tmpLookNum.innerHTML = ""
//          tmpLookNum.appendChild(postLengthL)
//          tmpLookNum.appendChild(commentLengthL)
//          tmpLookNum.appendChild(totalLengthL)
          val now2 = new Date().toTimeString()
          println("数据返回start"+ now2)

        } else {
          JsFunc.alert(s"error: ${rsp.msg}")
        }
    }
  }

  def lookNumFuc() = {
    val url = Routes.NumRoute.lookNumRoot //hupuSpider/start/start
    val data = ptcl.LookNumReq().asJson.noSpaces
    val now1 = new Date().toTimeString()
    println("请求发送Num"+ now1)


    Http.postJsonAndParse[ptcl.NumRsp](url, data).map{
      rsp =>
        //后台返回的处理
        if(rsp.errCode == 0) {
          val now2 = new Date().toTimeString()

          println("数据返回Num"+ now2)

          val postLength = h2("主贴现在的数目是"+rsp.lengthPost).render
          val commentLength = h2("回帖现在的数目是"+rsp.lengthComment).render
          val total = rsp.lengthComment + rsp.lengthPost
          val totalLength = h2("总计"+total).render
          tmpLookNum.innerHTML = ""
          tmpLookNum.appendChild(postLength)
          tmpLookNum.appendChild(commentLength)
          tmpLookNum.appendChild(totalLength)

        } else {
          JsFunc.alert(s"error: ${rsp.msg}")
        }
    }
  }

  def stopFuc() = {
    val now1 = new Date().toTimeString()
    println("请求发送stop"+ now1)
    state.innerHTML = ""
    state.appendChild(p("已停止").render)
    val url = Routes.NumRoute.stopRoot
    val data = ptcl.StopReq().asJson.noSpaces
    Http.postJsonAndParse[ptcl.StartRsp](url, data).map{
      rsp =>
        //后台返回的处理
        if(rsp.errCode == 0) {
          val now2 = new Date().toTimeString()
          println("数据返回stop"+ now2)
        } else {
          JsFunc.alert(s"error: ${rsp.msg}")
        }
    }
  }


  override def render(): Div = {

    div(
      state,
    tmpStart,
      tmpLookNum
    ).render
  }

}
