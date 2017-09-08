package com.neo.sk.hupuSpider.frontend

import com.neo.sk.hupuSpider.frontend.utils.{Component, Http, JsFunc,Shortcut}
import com.neo.sk.hupuSpider.ptcl
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.html.Div
import io.circe.generic.auto._
import io.circe.syntax._


/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 11:20 AM
  */
object StartPage extends Component[Div]{

  import scalatags.JsDom.short._
  import com.neo.sk.hupuSpider.frontend.NumOnServer

  private val title = h1(*.textAlign := "center")("hello")
  private val content = p(*.textAlign := "center")("虎扑爬虫")

  private val showArea = a(*.href := "http://localhost:9000/hupuSpider/showArea/hello")("查看运动装备的分区")

  private val startService = new NumOnServer()

  private val StartButton = button("开启爬虫").render
  StartButton.onclick = { e: MouseEvent =>
    startService.startFun("start")
  }
  private val LookNumButton = button("查看现在的主贴与回帖的数目").render
  LookNumButton.onclick = {e: MouseEvent =>
    startService.lookNumFuc()
  }
  private val stopButton = button("停止爬虫").render
  stopButton.onclick = {e: MouseEvent =>
    startService.stopFuc()
  }




  override def render(): Div = {
    div(
      title,
      content,
      showArea,
      StartButton.render,
      LookNumButton,
      stopButton,
      startService.render()
    ).render
  }

}
