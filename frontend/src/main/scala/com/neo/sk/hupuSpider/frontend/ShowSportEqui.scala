package com.neo.sk.hupuSpider.frontend

import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc}
import com.neo.sk.hupuSpider.ptcl
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom.html.Div
import org.scalajs.dom.{MouseEvent, window}

import scala.concurrent.ExecutionContext.Implicits.global



object ShowSportEqui extends Component[Div]{

  import scalatags.JsDom.short._

  private val title = h1(*.textAlign := "center")("查看分区--运动装备")
  private var preDiv = div().render

  private val url = Routes.ShowArea.sportEquiRoute
  private val data = ptcl.ShowAreaReq().asJson.noSpaces
  private var postDiv = div().render
  private val backButton = button("返回").render
  backButton.onclick = {e: MouseEvent =>
    postDiv.innerHTML = ""
    getComTable()
  }

    Http.postJsonAndParse[ptcl.ShowAreaRsp](url, data).map{
    rsp =>
      //后台返回的处理
      if(rsp.errCode == 0) {
        AreaInfo.sportEquiPost = rsp.post.toList
        AreaInfo.sportEquiCom = rsp.comment.toList
        //r.posttitle ,r.content, r.posturl ,r.authorname ,r.authorurl , r.time
        //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor
        getComTable()
      } else {
        JsFunc.alert(s"error: ${rsp.msg}")
      }
  }
   preDiv = postDiv
  //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor
  def getDetail(postId: String,content:String)= {
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
    var comList = List[(String,Long,String,Long)]()
    for( i <-  AreaInfo.sportEquiCom){
      val pattern = "https:\\/\\/bbs.hupu.com\\/([0-9]+?)(-[0-9]+)?.html".r
      val curId = pattern.findFirstMatchIn(i._1) match {
        case Some(r) =>
          r.group(1).toString
        case None =>
          window.alert("解析错误"+i._3)
          ""

      }
      if(curId == postId){
        comList = (i._2,i._3,i._4,i._5)::comList
      }
    }
    comList = comList.sortWith(_._2>_._2)
    for( i <- comList ){
      val comI = tr(
        td(i._1).render,
        td(i._2).render,
        td(i._3).render,
        td(i._4).render
      ).render
      postTable.appendChild(comI)
    }
    postDiv.appendChild(postTable)
    println("ddddddddddddddddddd")
  }
  def getComTable() = {
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
        td(a(*.onclick:={e:MouseEvent => getDetail(id,i._2)})(i._1).render).render,
        td(a(*.href:= i._3)(i._3).render).render,
        td(i._4).render,
        td(a(*.href:= i._5)(i._5).render).render,
        td(i._6).render
      ).render
      areaTable.appendChild(postI)


    }
    postDiv.appendChild(areaTable)
  }

  override def render(): Div = {
    div(
      title,
      backButton,
      postDiv.render

    ).render
  }

}
