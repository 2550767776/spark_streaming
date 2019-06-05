package com.train.util

import java.util.Date

import org.apache.commons.lang3.time.FastDateFormat

/**
  * 日期时间工具类
  */
object DateUtils {
  val complitTime = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
  val targetTime = FastDateFormat.getInstance("yyyyMMddHHmmss")

  def getTime(time: String) = {
    complitTime.parse(time).getTime
  }

  def parseToMinute(time: String) = {
    targetTime.format(new Date(getTime(time)))
  }
}
