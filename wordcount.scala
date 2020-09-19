// Databricks notebook source

/* 
    dbutils is a databricks utility that offers utility methods for writing and reading to dbfs
   dbfs is the databricks file system mounted into a databricks workspace
*/   


dbutils.fs.put("/home/spark/1.6/lines","""
Hello hello world
Hello how are you world
Writing to dbfs which is an improvement over hdfs
""", true)


// COMMAND ----------

import org.apache.spark.sql.functions._

// Load a text file and interpret each line as a java.lang.String
val ds = sqlContext.read.text("/home/spark/1.6/lines").as[String]
val result = ds
  .flatMap(_.split(" "))               // Split on whitespace
  .filter(_ != "")                     // Filter empty words
  .toDF()                              // Convert to DataFrame to perform aggregation / sorting
  .groupBy($"value")                   // Count number of occurences of each word
  .agg(count("*") as "numOccurances")
  .orderBy($"numOccurances" desc)      // Show most common words first

display(result)