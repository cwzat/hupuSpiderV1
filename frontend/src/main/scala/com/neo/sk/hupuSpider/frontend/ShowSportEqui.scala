package com.neo.sk.hupuSpider.frontend

import com.neo.sk.hupuSpider.frontend.StartPage.startService
import org.scalajs.dom.html.Div
import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc}
import com.neo.sk.hupuSpider.ptcl
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.raw.MouseEvent
import org.scalajs.dom.{MouseEvent, window}

import scala.scalajs.js.Date
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex
import scalatags.JsDom.short.td



object ShowSportEqui extends Component[Div]{

  import scalatags.JsDom.short._

  private val title = h1(*.textAlign := "center")("查看分区--运动装备")

  private val url = Routes.ShowArea.sportEquiRoute
  private val data = ptcl.ShowAreaReq().asJson.noSpaces
  private var postDiv = div().render
  private val backButton = button("返回").render
  backButton.onclick = {e: MouseEvent =>
    postDiv.innerHTML = ""
    postDiv = preDiv
  }

    Http.postJsonAndParse[ptcl.ShowAreaRsp](url, data).map{
    rsp =>
      //后台返回的处理
      if(rsp.errCode == 0) {
        AreaInfo.sportEquiPost = rsp.post.toList
        AreaInfo.sportEquiCom = rsp.comment.toList
        //r.posttitle ,r.content, r.posturl ,r.authorname ,r.authorurl , r.time
        //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor

        val areaTable = table().render
        val guide = tr().render
        guide.appendChild(td("标题").render)
        guide.appendChild(td("帖子的链接").render)
        guide.appendChild(td("作者名称").render)
        guide.appendChild(td("作者链接").render)
        guide.appendChild(td("发布时间").render)
        areaTable.appendChild(guide)
        println("iiiiiiiii" + AreaInfo.sportEquiPost(1)._1)
        println("iiiiiiiii" + AreaInfo.sportEquiPost(2)._1)

        for( i <- AreaInfo.sportEquiPost ){
          val pattern = "https:\\/\\/bbs\\.hupu\\.com\\/([0-9]+).html".r
          val id = pattern.findFirstMatchIn(i._3) match {
            case Some(r) =>
              r.group(1).toString
            case None =>
              window.alert("解析错误"+i._3)
              ""
          }
          val postI = tr(
            td(a(*.onclick:={e:MouseEvent => getDeatail(id,i._2)})(i._1).render).render,
            td(a(*.href:= i._3)(i._3).render).render,
            td(i._4).render,
            td(a(*.href:= i._5)(i._5).render).render,
            td(i._6).render
          ).render
          areaTable.appendChild(postI)


        }
        postDiv.appendChild(areaTable)


      } else {
        JsFunc.alert(s"error: ${rsp.msg}")
      }
  }
  private val preDiv = postDiv
  //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor
  def getDeatail(postId: String,content:String)= {
    println("ggggggggggggggggg")
    postDiv.innerHTML = ""
    val con = h2(content).render
    postDiv.appendChild(con)
    val postTable = table().render
    val guide = tr(
      td("评论的内容").render,
      td("楼层").render,
      td("作者名称").render,
      td("回复楼层").render
    ).render
    postTable.appendChild(guide)
    for( i <-  AreaInfo.sportEquiCom){
      val pattern = "https:\\/\\/bbs.hupu.com\\/([0-9]+?)(-[0-9]+)?.html".r
      val curId = pattern.findFirstMatchIn(i._1) match {
        case Some(r) =>
          r.group(1).toString
        case None =>
          window.alert("解析错误"+i._3)
          ""

      }
      println(curId + "ssss" + postId)
      if(curId == postId){
        println("==============")
        val comI = tr(
          td(i._2).render,
          td(i._3).render,
          td(i._4).render,
          td(i._5).render
        ).render
        postTable.appendChild(comI)
        println("ccccccccccccc")
      }
    }
    postDiv.appendChild(postTable)
    println("ddddddddddddddddddd")
  }

  override def render(): Div = {
    div(
      title,
      backButton,
      postDiv.render

    ).render
  }

}
