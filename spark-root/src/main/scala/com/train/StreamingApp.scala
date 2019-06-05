package com.train

import com.train.dao.CourseClickCountDAO
import com.train.entity.{ClickLog, CourseClickCount}
import com.train.util.DateUtils
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

import scala.collection.mutable.ListBuffer

object StreamingApp {
  def main(args: Array[String]): Unit = {
    if (args.length != 4) {
      print("Usage: StreamingApp <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }
    val Array(zkQuorum, group, topics, numThreads) = args

    val sparkConf = new SparkConf().setAppName("StreamingApp").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(60))

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val messages = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap)
    //1.数据接受测试
    //messages.map(_._2).count().print
    //2.数据清洗
    val logs = messages.map(_._2)
    val cleanData = logs.map(line => {
      val infos = line.split("\t")
      //infos(2) : "GET /course/148.html HTTP/1.1"
      val url = infos(2).split(" ")(1)
      var courseId = 0
      if (url.startsWith("/class")) {
        val courseIdHtml = url.split("/")(2)
        courseId = courseIdHtml.substring(0, courseIdHtml.lastIndexOf(".")).toInt
      }

      ClickLog(infos(0),DateUtils.parseToMinute(infos(1)),courseId,infos(3).toInt,infos(4))
    }).filter(clickLog => clickLog.courseId != 0)
    //3.统计访问量
    cleanData.map(x=> {
      (x.time.substring(0,8)+"_"+x.courseId,1)
    }).reduceByKey(_+_).foreachRDD(rdd => {
      rdd.foreachPartition(partionRecords => {
        val list = new ListBuffer[CourseClickCount]
        partionRecords.foreach(pair => {
          list.append(CourseClickCount(pair._1,pair._2))
        })
        CourseClickCountDAO.save(list)
      })
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
