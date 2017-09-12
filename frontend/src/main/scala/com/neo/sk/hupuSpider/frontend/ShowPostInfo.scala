package com.neo.sk.hupuSpider.frontend


import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date

import com.neo.sk.hupuSpider.frontend.ShowSportEqui.{data, url, _}
import com.neo.sk.hupuSpider.frontend.StartPage.sportEquiComponent
import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc, Shortcut}
import com.neo.sk.hupuSpider.ptcl
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import dom.window.sessionStorage
import dom.window._

import scala.concurrent.ExecutionContext.Implicits.global
import scalatags.JsDom.short._

/**
  * Created by cwz on 2017/9/12.
  */
object ShowPostInfo extends Component[Div] {
  //  <div class="panel panel-default">
  //    <!-- Default panel contents -->
  //    <div class="panel-heading">Panel heading</div>
  //    <div class="panel-body">
  //      <p>...</p>
  //    </div>
  //
  //    <!-- Table -->
  //    <table class="table">
  //      ...
  //    </table>
  //  </div>
  private val postTitle = sessionStorage.getItem("postTitle")
  private val postContent = sessionStorage.getItem("postContent")
  private val postId = sessionStorage.getItem("postId").toLong

  private val postInfo = div(*.`class` := "panel panel-primary")(
    div(*.`class` := "panel-heading")(postTitle),
    div(*.`class` := "panel-body")(
      p(postContent)
    )
  ).render
  private val postCom = table(*.`class` := "table table-striped")(
    thead(
      tr(
        td(*.fontWeight := "800", *.size := "800")("内容"),
        td(*.fontWeight := "800", *.size := "800")("作者"),
        td(*.fontWeight := "800", *.size := "800")("楼层"),
        td(*.fontWeight := "800", *.size := "800")("回复")
      )
    )
  ).render
  private val combody = tbody().render
  //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor

  private val url = Routes.ShowArea.postComRoute
  private val data = ptcl.PostComReq(postId).asJson.noSpaces
  Http.postJsonAndParse[ptcl.PostComRsp](url, data).map {
    rsp =>
      //后台返回的处理
      if (rsp.errCode == 0) {
        val postComList = rsp.postCom.sortWith(_._3<_._3)
        for( i <- postComList ){
          val pcl = tr(td(i._1), td("@"+i._2), td(i._3), td(i._4)).render
          combody.appendChild(pcl)
        }
        postCom.appendChild(combody)
        postInfo.appendChild(postCom)

      } else {
        JsFunc.alert(s"error: ${rsp.msg}")
      }
  }



  override def render(): Div = {
    div(
      postInfo
    ).render
  }

}
