import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.functions.{col, from_unixtime, to_date}

object Partitioner {
  def main(args: Array[String]) : Unit = {
    val spark = SparkSession.builder()
    .appName ("Partitioning parquet files")
    .master ("local")
    .getOrCreate ();

    val path = "/home/basel/IdeaProjects/Weather-Stations-Monitoring/base-central-station"
    val targetDir = s"$path/archive"
    val dir = s"$targetDir/" + java.time.LocalDate.now()
    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)

    if (!fs.exists(new Path(dir)))
      return
    val parquetFiles = spark.read.parquet(dir)
    val count = parquetFiles.count()
    val PF = parquetFiles.withColumn("date", to_date(from_unixtime(col("status_timestamp") / 1000)))
    PF.write
      .partitionBy("date", "station_id")
      .mode(SaveMode.Append)
      .parquet(targetDir)
    fs.delete(new Path(dir), true)
    println("old schema")
    parquetFiles.printSchema()
    println("new schema")
    parquetFiles.printSchema()
    println("processed " + count + " records")
  }
}
