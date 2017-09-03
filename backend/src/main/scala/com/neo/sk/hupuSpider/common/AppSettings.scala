package com.neo.sk.hupuSpider.common

import java.util.concurrent.TimeUnit

import com.neo.sk.utils.SessionSupport.SessionConfig
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

/**
  * User: Huangshanqi
  * Date: 2015/6/2
  * Time: 15:22
  */
object AppSettings {
  private[this] val logger = LoggerFactory.getLogger(this.getClass)

  private implicit class RichConfig(config: Config) {
    val noneValue = "none"

    def getOptionalString(path: String): Option[String] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getString(path))

    def getOptionalLong(path: String): Option[Long] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getLong(path))

    def getOptionalDurationSeconds(path: String): Option[Long] =
      if (config.getAnyRef(path) == noneValue) None
      else Some(config.getDuration(path, TimeUnit.SECONDS))
  }

  val config = ConfigFactory.parseResources("product.conf").withFallback(ConfigFactory.load())

  //slick db
  val slickConfig =config.getConfig("slick.db")
  val slickUrl = slickConfig.getString("url")
  val slickUser = slickConfig.getString("user")
  val slickPassword = slickConfig.getString("password")
  val slickMaximumPoolSize = slickConfig.getInt("maximumPoolSize")
  val slickConnectTimeout = slickConfig.getInt("connectTimeout")
  val slickIdleTimeout = slickConfig.getInt("idleTimeout")
  val slickMaxLifetime = slickConfig.getInt("maxLifetime")

  //jsoupSettings
  val jsoupSet = config.getConfig("jsoupSettings")
  val jsoupTimeOut = jsoupSet.getInt("timeout")

  //task
  val task = config.getConfig("task")
  val taskDelay = task.getDouble("delay")

  val appConfig = config.getConfig("app")

  val httpInterface = appConfig.getString("http.interface")
  val httpPort = appConfig.getInt("http.port")

  val sessionConfig = {
    val sConf = config.getConfig("session")
    SessionConfig(
      cookieName = sConf.getString("cookie.name"),
      serverSecret = sConf.getString("serverSecret"),
      domain = sConf.getOptionalString("cookie.domain"),
      path = sConf.getOptionalString("cookie.path"),
      secure = sConf.getBoolean("cookie.secure"),
      httpOnly = sConf.getBoolean("cookie.httpOnly"),
      maxAge = sConf.getOptionalDurationSeconds("cookie.maxAge"),
      sessionEncryptData = sConf.getBoolean("encryptData")
    )


  }

//  val config = ConfigFactory.parseResources("product.conf").withFallback(ConfigFactory.load())

//  //proxy
//  val proxyConfig = config.getConfig("proxy")
//  //代理获取延时(second)
//  val fetchProxyDelay = proxyConfig.getInt("fetchProxyDelay")
//  //代理获取周期(second)
//  val fetchProxyInterval = proxyConfig.getInt("fetchProxyInterval")
//  //代理获取接口
//  val fetchProxyUrl = proxyConfig.getString("fetchProxyUrl")
//  //代理获取数量
//  val fetchProxyNum = proxyConfig.getInt("fetchProxyNum")
//
//  //crawler
//  val crawlerConfig = config.getConfig("crawler")
//  val crawlerInterval = crawlerConfig.getInt("crawlerInterval")

}

