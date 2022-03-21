package com.moyisuiying.demo

/**
 * Classname:study_demo1
 *
 * @description:{description }
 * @author: 陌意随影
 * @Date: 2022-01-24 20:50
 */

class Person{
  val name = "lisi";
  val age = 1;
}
object Test1 {
  def main(args: Array[String]): Unit = {
    var a = 1;
    val b = 2;
    println(a)
    println(b)
    val person = new Person
    println(person)
  }
}
