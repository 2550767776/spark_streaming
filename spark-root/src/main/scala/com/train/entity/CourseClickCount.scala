package com.train.entity

/**
  * 实战课程点击数
  * @param day_course 对应hbase的rowkey
  * @param click_count 点击数
  */
case class CourseClickCount(day_course:String, click_count:Long)
