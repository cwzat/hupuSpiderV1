package com.neo.sk.hupuSpider.frontend

import java.text.{DateFormat, SimpleDateFormat}
import java.util.Date

import com.neo.sk.hupuSpider.frontend.ShowSportEqui.{board, subarea}
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


object ShowSportEqui extends Component[Div] {

  import scalatags.JsDom.short._

  private val bar = div(*.`class` := "navbar navbar-default navbar-fixed-top")(
    div(*.`class` := "container-fluid")(
      div(*.`class` := "navbar-header")(
        button(
          *.`class` := "btn btn-link btn-lg",
          *.role := "button",
          *.onclick := { e: MouseEvent =>
            dom.window.location.href = "/hupuSpider/start/hello"
          }
        )("虎扑社区")

      ), //头部
      div(*.`class` := "collapse navbar-collapse")(
        ul(*.`class` := "nav navbar-nav")(
          li(
            button(
              *.`class` := "btn btn-link btn-lg",
              *.role := "button",
              *.onclick := { e: MouseEvent =>
                sessionStorage.clear()
                sessionStorage.setItem("boardAreaName", "运动装备#运动装备")
                dom.window.location.href = "/hupuSpider/showArea/hello"
              }
            )("运动装备")

          ),
          li(
            button(
              *.`class` := "btn btn-link btn-lg",
              *.role := "button",
              *.onclick := { e: MouseEvent =>
                sessionStorage.clear()
                sessionStorage.setItem("boardAreaName", "运动装备#交易品(新区)")
                dom.window.location.href = "/hupuSpider/showArea/hello"
              }
            )("交易品(新区)")
          ),
          li(
            button(
              *.`class` := "btn btn-link btn-lg",
              *.role := "button",
              *.onclick := { e: MouseEvent =>
                sessionStorage.clear()
                sessionStorage.setItem("boardAreaName", "步行街#步行街主干道")
                dom.window.location.href = "/hupuSpider/showArea/hello"
              }
            )("步行街")
          ),
          li(
            button(
              *.`class` := "btn btn-link btn-lg",
              *.role := "button",
              *.onclick := { e: MouseEvent =>
                sessionStorage.clear()
                sessionStorage.setItem("boardAreaName", "NBA论坛#湿乎乎的话题")
                dom.window.location.href = "/hupuSpider/showArea/hello"
              }
            )("湿乎乎的话题")
          )
        )
      ) //分区
    )
  ).render

  //private var preDiv = div().render
  //路径
  private val data = ptcl.ShowAreaReq().asJson.noSpaces

  //分区
  private val mes = sessionStorage.getItem("boardAreaName")
  private val board = mes.split("#")(0)
  private val subarea = mes.split("#")(1)

  //导航栏


  //请求数据
  private val url = mes match {
    case "运动装备#运动装备" =>
      Routes.ShowArea.sportEquiRoute
    case "运动装备#交易品(新区)" =>
      Routes.ShowArea.shoppingGoodsRoute
    case "步行街#步行街主干道" =>
      Routes.ShowArea.buxingjieRoute
    case "NBA论坛#湿乎乎的话题" =>
      Routes.ShowArea.shihuhuRoute
  }

  private val page = div(*.`class` := "nav",
    *.style := "aria-label: Page navigation",
    *.padding := "10px 0px 20px 500px")().render
  private val pageUl = ul(*.`class` := "pagination")().render
  private var maxpage = 0
  Http.postJsonAndParse[ptcl.ShowAreaRsp](url, data).map {
    rsp =>
      //后台返回的处理
      if (rsp.errCode == 0) {
        val now: Date = new Date()
        println("数据返回area", now)
        AreaInfo.postList = rsp.post.toList
        val pageNum = rsp.length / 100 + 1
        page.innerHTML = ""

        maxpage = pageNum
        if (maxpage <= 10) {
          for (i <- 1 to pageNum) {
            val pageI = li(
              a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, i, board, subarea) })(i)
            ).render
            pageUl.appendChild(pageI)
          }
        }
        else {
          for (i <- 1 to 10) {
            val pageI = li(
              a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, i, board, subarea) })(i)
            ).render
            pageUl.appendChild(pageI)
          }
          pageUl.appendChild(
            a()("...").render
          )
          pageUl.appendChild(
            li(
              a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, maxpage, board, subarea) })(maxpage)
            ).render
          )
        }

        page.appendChild(pageUl)
        getPostTable(AreaInfo.postList, 1, board, subarea)


        //r.posttitle ,r.content, r.posturl ,r.authorname ,r.authorurl , r.time
        //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor

      } else {
        JsFunc.alert(s"error: ${rsp.msg}")
      }
  }


  private val postTable = table(*.`class` := "table table-hover")(
    thead(
      tr(
        td(*.fontWeight := "800", *.size := "800")("#"),
        td(*.fontWeight := "800", *.size := "800")("标题"),
        td(*.fontWeight := "800", *.size := "800")("作者"),
        td(*.fontWeight := "800", *.size := "800")("发布时间")
      )
    )
  ).render
  private val tbpost = tbody().render

  //r.posttitle ,r.content, r.posturl ,r.authorname ,r.authorurl , r.time
  //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor
  def getPostTable(postList: List[(String, String, String, String, String, String)],
                   pagenum: Int,
                   board: String,
                   subarea: String):Unit = {
    if (maxpage <= 10) {
    }
    else {
      if(pagenum == 1){
        pageUl.innerHTML = ""
        pageUl.appendChild(
          li(*.`class` := "active")(
            span(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum, board, subarea) })(pagenum)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum+1, board, subarea) })(pagenum+1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum+2, board, subarea) })(pagenum+2)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, maxpage, board, subarea) })(maxpage)
          ).render
        )
        page.appendChild(pageUl)
      }else if(pagenum == 2 ){
        pageUl.innerHTML = ""
        pageUl.appendChild(
          li()(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, 1, board, subarea) })(1)
          ).render
        )
        pageUl.appendChild(
          li(*.`class` := "active")(
            span(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum, board, subarea) })(pagenum)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum+1, board, subarea) })(pagenum+1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, maxpage, board, subarea) })(maxpage)
          ).render
        )
        page.appendChild(pageUl)
      }else if(pagenum == maxpage){
        pageUl.innerHTML = ""
        pageUl.appendChild(
          li()(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, 1, board, subarea) })(1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum-1, board, subarea) })(pagenum-1)
          ).render
        )
        pageUl.appendChild(
          li(*.`class` := "active")(
            span(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum, board, subarea) })(pagenum)
          ).render
        )
      }else if(pagenum == maxpage-1){
        pageUl.innerHTML = ""
        pageUl.appendChild(
          li()(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, 1, board, subarea) })(1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum-1, board, subarea) })(pagenum-1)
          ).render
        )
        pageUl.appendChild(
          li(*.`class` := "active")(
            span(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum, board, subarea) })(pagenum)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, maxpage, board, subarea) })(maxpage)
          ).render
        )
        page.appendChild(pageUl)
      }else if(pagenum == maxpage-2){
        pageUl.innerHTML = ""
        pageUl.appendChild(
          li()(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, 1, board, subarea) })(1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum-1, board, subarea) })(pagenum-1)
          ).render
        )
        pageUl.appendChild(
          li(*.`class` := "active")(
            span(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum, board, subarea) })(pagenum)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, maxpage, board, subarea) })(maxpage)
          ).render
        )
        page.appendChild(pageUl)
      }
      else{
        pageUl.innerHTML = ""
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, 1, board, subarea) })(1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
          pageUl.appendChild(
            li(
              a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum-1, board, subarea) })(pagenum-1)
            ).render
          )

        pageUl.appendChild(
          li(*.`class` := "active")(
            span(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum, board, subarea) })(pagenum)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, pagenum+1, board, subarea) })(pagenum+1)
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => })("...")
          ).render
        )
        pageUl.appendChild(
          li(
            a(*.onclick := { e: MouseEvent => getPostTable(AreaInfo.postList, maxpage, board, subarea) })(maxpage)
          ).render
        )
        page.appendChild(pageUl)
      }

    }

    tbpost.innerHTML = ""
    var number = 0
    val curpagePostList = postList.slice((pagenum - 1) * 100, pagenum * 100)
    for (i <- curpagePostList) {
      number += 1
      val postI = tr(
        td(*.fontWeight := "800")(number),
        td(a(*.onclick := { e: MouseEvent => getDetail(i._3, i._2) })(i._1).render).render,
        td("@" + i._4).render,
        td(i._6).render
      ).render
      tbpost.appendChild(postI)
    }
    postTable.appendChild(tbpost)
  }


  def getDetail(postId: String, postContent: String) = {

  }

  override def render(): Div = {
    div(
      bar,
      br.render,
      br.render,
      br.render,
      br.render,
      postTable.render,
      page
    ).render
  }

}


//  private var postDiv = div().render
//  private val backButton = button("返回").render
//  backButton.onclick = { e: MouseEvent =>
//    postDiv.innerHTML = ""
//    getComTable()
//  }
//  preDiv = postDiv
//
//  //r.posturl,r.commentcontent,r.commentfloor,r.commentername,r.replyfloor
//  def getDetail(postId: String, content: String) = {
//    println("ggggggggggggggggg")
//    postDiv.innerHTML = ""
//    val con = h2(content).render
//    postDiv.appendChild(con)
//    val postTable = table().render
//    val guide = tr(
//      td("评论的内容").render,
//      td("楼层").render,
//      td("作者名称").render,
//      td("回复楼层").render
//    ).render
//    postTable.appendChild(guide)
//    var comList = List[(String, Long, String, Long)]()
//    for (i <- AreaInfo.sportEquiCom) {
//      val pattern = "https:\\/\\/bbs.hupu.com\\/([0-9]+?)(-[0-9]+)?.html".r
//      val curId = pattern.findFirstMatchIn(i._1) match {
//        case Some(r) =>
//          r.group(1).toString
//        case None =>
//          window.alert("解析错误" + i._3)
//          ""
//
//      }
//      if (curId == postId) {
//        comList = (i._2, i._3, i._4, i._5) :: comList
//      }
//    }
//    comList = comList.sortWith(_._2 > _._2)
//    for (i <- comList) {
//      val comI = tr(
//        td(i._1).render,
//        td(i._2).render,
//        td(i._3).render,
//        td(i._4).render
//      ).render
//      postTable.appendChild(comI)
//    }
//    postDiv.appendChild(postTable)
//    println("ddddddddddddddddddd")
//  }

//  def getComTable() = {
//    val areaTable = table().render
//    val guide = tr().render
//    guide.appendChild(td("标题").render)
//    guide.appendChild(td("帖子的链接").render)
//    guide.appendChild(td("作者名称").render)
//    guide.appendChild(td("作者链接").render)
//    guide.appendChild(td("发布时间").render)
//    areaTable.appendChild(guide)
//    println("iiiiiiiii" + AreaInfo.sportEquiPost(1)._1)
//    println("iiiiiiiii" + AreaInfo.sportEquiPost(2)._1)
//
//    for (i <- AreaInfo.sportEquiPost) {
//      val pattern = "https:\\/\\/bbs\\.hupu\\.com\\/([0-9]+).html".r
//      val id = pattern.findFirstMatchIn(i._3) match {
//        case Some(r) =>
//          r.group(1).toString
//        case None =>
//          window.alert("解析错误" + i._3)
//          ""
//      }
//      val postI = tr(
//        td(a(*.onclick := { e: MouseEvent => getDetail(id, i._2) })(i._1).render).render,
//        td(a(*.href := i._3)(i._3).render).render,
//        td(i._4).render,
//        td(a(*.href := i._5)(i._5).render).render,
//        td(i._6).render
//      ).render
//      areaTable.appendChild(postI)
//
//
//    }
//    postDiv.appendChild(areaTable)
//  }