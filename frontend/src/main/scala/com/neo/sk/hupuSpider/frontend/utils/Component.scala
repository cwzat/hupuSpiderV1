package com.neo.sk.hupuSpider.frontend.utils

import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.HTMLElement


/**
  * User: Taoz
  * Date: 12/26/2016
  * Time: 1:36 PM
  */
trait Component[T <: HTMLElement] {
  def render: T



}

trait Page extends Component[Div] {
  
  private[this] var selfDom: Div = _

  def locationHash: String

  def mounted(): Unit = {}

  def unMounted(): Unit = {}

  def get: Div = {
    if(selfDom == null){
      selfDom = render
    }
    selfDom
  }

}
