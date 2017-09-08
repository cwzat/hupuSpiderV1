//package com.neo.sk.hupuSpider.frontend
//
//
//import org.scalajs.dom.html.Div
//import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc}
//import com.neo.sk.hupuSpider.ptcl
//import io.circe.generic.auto._
//import io.circe.syntax._
//
//import scala.scalajs.js.Date
//import scala.concurrent.ExecutionContext.Implicits.global
//
//
//
///**
//  * User: Taoz
//  * Date: 1/16/2017
//  * Time: 11:20 AM
//  */
//object ShowPost extends Component[Div]{
//
//  import scalatags.JsDom.short._
//  private val url = Routes.ShowArea.postInfo
//  private val data = ptcl.PostInfoReq.asJson.noSpaces
//
//
//  Http.postJsonAndParse[ptcl.ShowAreaRsp](url, data).map{
//    rsp =>
//      //后台返回的处理
//      if(rsp.errCode == 0) {
//
//
//
//      } else {
//        JsFunc.alert(s"error: ${rsp.msg}")
//      }
//  }
//
//
//
//
//
//  override def render(): Div = {
//    div(
//
//    ).render
//  }
//
//}
