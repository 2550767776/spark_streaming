package com.train.dao

import com.train.entity.CourseClickCount
import com.train.util.HBaseUtils
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable.ListBuffer

object CourseClickCountDAO {
  val tableName = "imooc_course_clickcount"
  val cf = "info"
  val qulifer = "click_count"

  /**
    * 保存数据
    */
  def save(list: ListBuffer[CourseClickCount]): Unit = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    for (ele <- list) {
      table.incrementColumnValue(Bytes.toBytes(ele.day_course),
        Bytes.toBytes(cf),
        Bytes.toBytes(qulifer),
        ele.click_count)
    }
  }

  /**
    * 根据RowKey查询
    */
  def count(day_course: String): Long = {
    val table = HBaseUtils.getInstance().getTable(tableName)
    val get = new Get(Bytes.toBytes(day_course))
    val value = table.get(get).getValue(cf.getBytes, qulifer.getBytes)
    if (value == null) {
      0L
    } else {
      Bytes.toLong(value)
    }
  }


/*  def main(args: Array[String]): Unit = {
    val list = new ListBuffer[CourseClickCount]
    list.append(CourseClickCount("20190605_1",5))
    list.append(CourseClickCount("20190605_2",10))
    list.append(CourseClickCount("20190605_3",15))

    save(list)

    print(count("20190605_1")+"-")
    print(count("20190605_2")+"-")
    print(count("20190605_3")+"-")
  }
*/

}
