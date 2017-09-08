package com.neo.sk.hupuSpider.service.transformTool

import org.jsoup.nodes.Document

/**
  * Created by cwz on 2017/8/22.
  */
sealed trait ActorMessages

case class NextAreaPageTrans(nextAreaPageUrlTmp:String) extends ActorMessages

case class GetAreaPostUrlTrans(l:String, board:String,childCount:String,state:String) extends ActorMessages

case class GetAreaNextPageUrlTrans(areaUrl:String, count:Int) extends ActorMessages

case class GetAreaPostUrlContentTrans(cur:String) extends ActorMessages

case class GetAreaPostUrlCommentTrans(cur:String) extends ActorMessages

case class UpdateComTrans(boardName:String,areaName:String,count:String)extends ActorMessages

case class UpdateComDoTrans(boardName:String,areaName:String,url:String) extends ActorMessages
case class Task (url:String) extends ActorMessages

case class TalkGetContentOrComment(url:String, boardName:String, areaName:String,state:String,id:String) extends ActorMessages

case class GetComment(postUrl:String,doc:Document) extends ActorMessages

case class StartStopRequireTrans(start:String) extends ActorMessages

case class StartRequireBoardAreaTrans(board:String) extends ActorMessages

case class StopTrans(stop:String) extends ActorMessages

case class GetPostEachPge(curPageUrl:String,postNum:Int,boardName:String,areaName:String) extends ActorMessages