//package hupuSpider.service
//
//import akka.actor.Actor
///**
//  * Created by cwz on 2017/8/21.
//  */
//class getNextPagePostUrl extends Actor{
//  def receive = {
//    case transAreaUrlList => {
//        getCurPagePostUrlFun()
//      }
//    case _ =>
//      context.stop(self)
//      println("已经获取此板块下所有分区的url error")
//  }
//  def getCurPagePostUrlFun() = {
//    if(transformTool.transAreaUrlList.state=="已经得到该板块下所有分区的url"){
//
//    }
//    else{
//      context.stop(self)
//      println("transformTool.transAreaUrlList.state 写入错误")
//    }
//    //var url = List(1)
////    for (elem <- doc.select("td[class=p_title]")) {
////      val tmp: String = elem.select("a[id='']").attr("href") + "\n" //空id的写法
////      writer.write("https://bbs.hupu.com");
////      writer.write(tmp);
////
////    }//写的是当前页面的所有帖子的url
//  }
//}
