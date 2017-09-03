package com.neo.sk.hupuSpider.models.Dao

import slick.jdbc.PostgresProfile.api._
import com.neo.sk.hupuSpider.models.SlickTables._
import com.neo.sk.utils.DBUtil.db

/**
  * Created by cwz on 2017/8/22.
  */
object timeTable {
  def rspLastTime(board: String, subarea: String) = db.run {
    tTimetable.filter(_.board === board).
      filter(_.subarea === subarea).
      map(_.last).
      result

  }

  def rspFarTime(board: String, subarea: String) = db.run {
    tTimetable.filter(_.board === board).
      filter(_.subarea === subarea).
      map(_.farthest).
      result
  }
  def updateFarTime(board:String,subarea:String,farTimeNew:String) = db.run{
    tTimetable.filter(_.board === board).
      filter(_.subarea === subarea).
      map( _.farthest ).
      update(farTimeNew)
  }
}




