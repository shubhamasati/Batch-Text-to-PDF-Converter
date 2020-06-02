package com.hank.ttp;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.hank.custom.PDFInputFormat;
import com.hank.custom.PDFOutputFormat;
import com.hank.custom.PDFWritable;

public class Main {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if( otherArgs.length != 2) {
			System.err.println("Usage: warning count <in> <ont>");
			System.exit(2);
		}
		Job job = new Job(conf, "Text-to-PDF");
		job.setJarByClass(Main.class);
		job.setMapperClass(PDFMapper.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(PDFWritable.class);
		job.setInputFormatClass(PDFInputFormat.class);
		job.setOutputFormatClass(PDFOutputFormat.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		
		
		
	}

}
