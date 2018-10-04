/***
 * Excerpted from "Seven Concurrency Models in Seven Weeks",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/pb7con for more book information.
***/
package com.paulbutcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class WikipediaContributors extends Configured implements Tool {

  public static class Map extends Mapper<Object, Text, IntWritable, LongWritable> {

    public void map(Object key, Text value, Context context)
      throws IOException, InterruptedException {

      Contribution contribution = new Contribution(value.toString());
      context.write(new IntWritable(contribution.getContributorId()), 
        new LongWritable(contribution.getTimestamp()));
    }
  }

  public static class Reduce extends Reducer<IntWritable, LongWritable, IntWritable, Text> {

    static DateTimeFormatter dayFormat = ISODateTimeFormat.yearMonthDay();
    static DateTimeFormatter monthFormat = ISODateTimeFormat.yearMonth();

    public void reduce(IntWritable key, Iterable<LongWritable> values, Context context)
      throws IOException, InterruptedException {

      HashMap<DateTime, Integer> days = new HashMap<DateTime, Integer>();
      HashMap<DateTime, Integer> months = new HashMap<DateTime, Integer>();

      for (LongWritable value: values) {
        DateTime timestamp = new DateTime(value.get());
        DateTime day = timestamp.withTimeAtStartOfDay();
        DateTime month = day.withDayOfMonth(1);
        incrementCount(days, day);
        incrementCount(months, month);
      }
      for (Entry<DateTime, Integer> entry: days.entrySet())
        context.write(key, new Text(dayFormat.print(entry.getKey()) + "\t" + entry.getValue()));
      for (Entry<DateTime, Integer> entry: months.entrySet())
        context.write(key, new Text(monthFormat.print(entry.getKey()) + "\t" + entry.getValue()));
    }

    private void incrementCount(HashMap<DateTime, Integer> counts, DateTime key) {
      Integer currentCount = counts.get(key);
      if (currentCount == null)
        counts.put(key, 1);
      else
        counts.put(key, currentCount + 1);
    }
  }

  public int run(String[] args) throws Exception {
    Configuration conf = getConf();

    Job job = Job.getInstance(conf, "wikicontributors");
    job.setJarByClass(WikipediaContributors.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setMapOutputValueClass(LongWritable.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    boolean success = job.waitForCompletion(true);
    return success ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new WikipediaContributors(), args);
    System.exit(res);
  }
}
