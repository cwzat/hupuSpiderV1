package com.neo.sk.hupuSpider.frontend

/**
  * User: Taoz
  * Date: 1/16/2017
  * Time: 6:49 PM
  */
object Routes {


  val baseUrl = "/hupuSpider"

  object NumRoute{
    val startRoot = baseUrl + "/start"
    val startTrueRoot = startRoot + "/start"
    val startHello = startRoot + "/hello"
    val lookNumRoot = startRoot + "/lookNum"
    val stopRoot = startRoot + "/stop"
  }
  object ShowArea{
    val showRoute = baseUrl + "/showArea"
    val sportEquiRoute = showRoute + "/sportEqui"
    val shoppingGoodsRoute = showRoute + "/shoppingGoods"
    val buxingjieRoute = showRoute + "/buxingjie"
    val shihuhuRoute = showRoute + "/shihuhu"
  }



}
