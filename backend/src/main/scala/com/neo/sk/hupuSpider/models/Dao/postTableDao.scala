package com.neo.sk.hupuSpider.models.Dao

import com.neo.sk.hupuSpider.models.SlickTables
import slick.jdbc.PostgresProfile.api._
import com.neo.sk.hupuSpider.models.SlickTables._
import com.neo.sk.utils.DBUtil.db

/**
  * Created by cwz on 2017/8/18.
  */
object postTableDao {
  def insert(board:String,subarea:String,id:Long,postUrl:String,
            postTitle:String,authorName:String,authorUrl:String, content:String,time:String )= db.run{
    tPosttable += SlickTables.rPosttable(
      board,subarea,id,
      postUrl, postTitle,authorName,
      authorUrl, content,time)
  }
  def search(postUrl:String) = db.run{
    tPosttable.filter(_.posturl === postUrl).exists.result
  }
  def searchMaxId (board:String,subarea:String)= db.run{
    tPosttable.filter(_.board === board ).
      filter(_.subarea === subarea).
      map(r => r.id).
      max.result
  }
  def searchMinId (board:String,subarea:String)= db.run{
    tPosttable.filter(_.board === board ).filter(_.subarea === subarea).map(r => r.id).min.result
  }
  def areaLength(board:String, subarea:String) = db.run {
    tPosttable.filter(_.board === board ).filter(_.subarea === subarea).length.result
  }
  def getTime (postUrl:String) = db.run {
    tPosttable.filter(_.posturl === postUrl).map(_.time).result
  }
  def getPostNumInTimeRange (board:String,subarea:String) = db.run{
    tPosttable.filter(_.board === board).
      filter(_.subarea === subarea).
      map( r => r.time).
      result
  }
  def lengthPost() = db.run{
    tPosttable.length.result
  }
}