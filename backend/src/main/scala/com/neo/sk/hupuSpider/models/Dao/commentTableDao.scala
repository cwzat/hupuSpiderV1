package com.neo.sk.hupuSpider.models.Dao

import com.neo.sk.hupuSpider.models.SlickTables
import com.neo.sk.hupuSpider.models.SlickTables._
import com.neo.sk.hupuSpider.service.GetAreaPostUrl
import slick.jdbc.PostgresProfile.api._
import com.neo.sk.utils.DBUtil.db
/**
  * Created by cwz on 2017/8/18.
  */
object commentTableDao {
  def insert(postUrl:String,commentId:Long,commentGlobalUrl:String,
             commentFloor:Long, commentContext:String,commenterId:String,
             commenterName:String, lights:Long,
             replyFloor:Long,boardName:String,areaName:String ) = db.run{
    tCommenttable += SlickTables.rCommenttable(
      postUrl,
      commentId,
      commentGlobalUrl,
      commentFloor,
      commentContext,
      commenterId,
      commenterName,
      lights,
      replyFloor,
      boardName,
      areaName
      )
  }

  def searchMaxId(postUrl:String) = db.run{
    tCommenttable.filter(_.posturl === postUrl).map(_.commentid).max.result
  }
  def lengthComment() = db.run{
    tCommenttable.length.result
  }
  def getAreaComment(board:String,subarea:String) = db.run{
    tCommenttable.filter(_.boardname === board).
      filter(_.areaname === subarea).
      map( r => (r.posturl,
        r.commentcontent,
        r.commentfloor,
        r.commentername,
        r.replyfloor)).
      result
  }
  def getAreaComPs(board:String,areaName:String) = db.run(
    tPosttable.filter(_.board === board).
      filter(_.subarea === areaName).
      map(_.posturl).
      result

  )

}
