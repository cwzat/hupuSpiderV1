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
import com.neo.sk.hupuSpider.frontend.StartPage._
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
          val preNum1 = p(
            a(*.href := "#" ,*.`class` :="list-group-item active")("现有数目统计"),
            a(*.`class` := "list-group-item",*.href := "#")(
              span(*.`class` := "badge")(rsp.lengthPost)
            )("主贴"),
            a(*.`class` := "list-group-item",*.href := "#")(
              span(*.`class` := "badge")(rsp.lengthComment)
            )("回帖"),
            a(*.`class` := "list-group-item",*.href := "#")(
              span(*.`class` := "badge")(rsp.lengthPost +rsp.lengthComment)
            )("共计")

          ).render
          StartPage.preNum.innerHTML = ""
          StartPage.preNum.appendChild(
            preNum1.render
          )

          StartPage.preNum.style.visibility = "visible"

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
          val nowNum1 = p(
            a(*.href := "#" ,*.`class` :="list-group-item active")("最新数目统计"),
            a(*.`class` := "list-group-item",*.href := "#")(
              span(*.`class` := "badge")(rsp.lengthPost)
            )("主贴"),
            a(*.`class` := "list-group-item",*.href := "#")(
              span(*.`class` := "badge")(rsp.lengthComment)
            )("回帖"),
            a(*.`class` := "list-group-item",*.href := "#")(
              span(*.`class` := "badge")(rsp.lengthPost +rsp.lengthComment)
            )("共计")

          ).render
          StartPage.nowNum.innerHTML = ""
          StartPage.nowNum.appendChild(
            nowNum1.render
          )

          StartPage.nowNum.style.visibility = "visible"


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
//      state,
//    tmpStart,
//      tmpLookNum,
//      StartPage.postNum
    ).render
  }

}
