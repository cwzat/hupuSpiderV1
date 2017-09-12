package com.neo.sk.hupuSpider.frontend

import com.neo.sk.hupuSpider.frontend.StartPage.sportEquiComponent
import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc, Shortcut}
import com.neo.sk.hupuSpider.ptcl
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import io.circe.generic.auto._
import io.circe.syntax._
import org.scalajs.dom
import dom.window.sessionStorage


/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 11:20 AM
  */
object StartPage extends Component[Div] {

  import scalatags.JsDom.short._
  import com.neo.sk.hupuSpider.frontend.NumOnServer


  private val startService = new NumOnServer()

  private val hupuSpider = div(*.`class` := "jumbotron",
    *.style := "background:url('/hupuSpider/static/img/back3.jpg');background-size:100%;height:300px;")(
    h1(*.padding := "0px 0px 0px 30px", *.font := "Helvetica Neue", *.fontSize := "50")("Exciting HuPu").render,
    h2(*.padding := "0px 0px 6px 30px", *.font := "Verdana, sans-serif", *.size := "600", *.fontSize := "600")("Information and Life").render,
    h4(*.padding := "0px 0px 0px 30px")(
      a(*.padding := "5px 15px 5px 15px",
        *.`class` := "btn btn-primary btn-lg",
        *.href := "#introdution",
        *.role := "button")
      ("Learn More").render
    ).render
  ).render


  //注意参数 col-md-3 padding = 0px 10px 10px 10px
  private val sportEquiComponent = div(
    div(*.`class` := "col-md-3", *.name := "introdution")(
      div(*.`class` := "thumbnail", *.padding := "0px 10px 10px 10px")(
        img(*.src := "/hupuSpider/static/img/运动装备.jpg"),
        h3("运动装备"),
        p("篮球鞋，Sneaker文化，球星卡，各种篮球用品，前瞻与交流，经典收藏赏析",br,br,br),

        div(
          button(
            *.`class` := "btn btn-primary",
            *.role := "button",
            *.onclick := {e:MouseEvent =>
              sessionStorage.clear()
              sessionStorage.setItem("boardAreaName", "运动装备#运动装备")
              dom.window.location.href = "http://localhost:9000/hupuSpider/showArea/hello"
            }
          )("运动装备")
        )
      )
    )
  ).render
  private val shoppingGoodsComponnet = div(
    div(*.`class` := "col-md-3", *.name := "introdution")(
      div(*.`class` := "thumbnail", *.padding := "0px 10px 10px 10px")(
        img(*.src := "/hupuSpider/static/img/shoppingGoods.jpg"),
        h3("交易品新区"),
        p("小心假货卖家,特征;无意义id及实拍id图,码数全,价过低",br,br,br,br),
        div(
          button(
            *.`class` := "btn btn-primary",
            *.role := "button",
            *.onclick := {e:MouseEvent =>
              sessionStorage.clear()
              sessionStorage.setItem("boardAreaName", "运动装备#交易品(新区)")
              dom.window.location.href = "http://localhost:9000/hupuSpider/showArea/hello"
            }
          )("交易品新区")
        )
      )
    )
  ).render
  private val buxingjieComponent = div(
    div(*.`class` := "col-md-3", *.name := "introdution")(
      div(*.`class` := "thumbnail", *.padding := "0px 10px 10px 10px")(
        img(*.src := "/hupuSpider/static/img/buxingjie.jpg"),
        h3("步行街主干道"),
        p("逛虎扑步行街极易上瘾，请各位注意控制时间哟 ",br,br,br,br),
        div(
          button(
            *.`class` := "btn btn-primary",
            *.role := "button",
            *.onclick := {e:MouseEvent =>
              sessionStorage.clear()
              sessionStorage.setItem("boardAreaName", "步行街#步行街主干道")
              dom.window.location.href = "http://localhost:9000/hupuSpider/showArea/hello"
            }
          )("步行街主干道")
        )
      )
    )
  ).render
  private val shihuhuComponent = div(
    div(*.`class` := "col-md-3", *.name := "introdution")(
      div(*.`class` := "thumbnail", *.padding := "0px 10px 10px 10px")(
        img(*.src := "/hupuSpider/static/img/shihuhu.jpg"),
        h3("湿乎乎的话题"),
        p("工业时代是干巴巴的，我们在虚拟世界的关系是湿乎乎的。每一个话题，都会让我们更多一些交集，更多一些黏着力O(∩_∩)O~ "),
        div(
          button(
            *.`class` := "btn btn-primary",
            *.role := "button",
            *.onclick := {e:MouseEvent =>
              sessionStorage.clear()
              sessionStorage.setItem("boardAreaName", "NBA论坛#湿乎乎的话题")
              dom.window.location.href = "http://localhost:9000/hupuSpider/showArea/hello"
            }
          )("湿乎乎的话题")
        )
      )
    )
  ).render

  val preNum = div(*.`class` := "list-group", *.style := "width:380px")(
  ).render
  preNum.style.visibility = "hidden"
  val nowNum = div(*.`class` := "list-group", *.style := "width:380px")(
  ).render
  nowNum.style.visibility = "hidden"
  private val contextpart = div(
    div(*.`class` := "row")(
      div(*.`class` := "col-md-5 ", *.alignSelf := "middle",*.style := "padding:0px 0px 2px 30px")(
        p(*.alignSelf := "right")(preNum)
      ),
      div(*.`class` := "col-md-5 ", *.alignSelf := "middle")(
        p(*.alignSelf := "right")(nowNum)
      )
    ),
    sportEquiComponent,
    shoppingGoodsComponnet,
    buxingjieComponent,
    shihuhuComponent

  ).render
  private val startbutton = button(*.`type` := "button",
    *.id := "startbutton",
    *.`class` := "btn btn-success ",
    *.role := "button"
    )("Start Spider").render
  startbutton.onclick = { e: MouseEvent =>
    startbutton.setAttribute("class","btn btn-success disabled")//开始键按下以后开始键不可点击 停止键可以点击
    stopbutton.setAttribute("class","btn btn-warning ")
    startService.startFun("start")
  }
  private val stopbutton =  button(*.`type` := "button",
    *.`class` := "btn btn-warning disabled "//停止键默认不可点击
  )("Stop Spider").render
  stopbutton.onclick = { e: MouseEvent =>
    stopbutton.setAttribute("class","btn btn-warning disabled")
    startbutton.setAttribute("class","btn btn-success ")
    startService.stopFuc()
  }

  private val buttonPart = ul(*.alignItems := "middle")(
    a(*.alignSelf := "middle")(
      startbutton
    ),
    p(
      br()
    ),
    p(*.alignSelf := "middle")(
      stopbutton
    ),
    p(
      br()
    ),
    p(*.alignSelf := "middle")(
      button(*.`type` := "button",
        *.`class` := "btn btn-info  ",
        *.onclick := { e: MouseEvent =>
          startService.lookNumFuc()
        }
      )("Get Number")
    )
  ).render


  private val allpart = div(*.`class` := "row")(
    div(*.`class` := "col-md-2", *.alignSelf := "middle")(
      buttonPart
    ),
    div(*.`class` := "col-md-9")(
      contextpart
    )
  ).render


  override def render(): Div = {
    div(
      hupuSpider,
      allpart
    ).render
  }

}
