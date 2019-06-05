package com.train.entity

/**
  * 清洗后日志信息
  *
  * @param ip         IP地址
  * @param time       访问时间
  * @param courseId   课程ID
  * @param statusCode 访问状态码
  * @param referer    访问referer
  */
case class ClickLog(
                     ip: String,
                     time: String,
                     courseId: Int,
                     statusCode: Int,
                     referer: String
                   )
