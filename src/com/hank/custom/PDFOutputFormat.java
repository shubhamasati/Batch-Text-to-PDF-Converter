package com.hank.custom;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
//import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;

public class PDFOutputFormat extends FileOutputFormat<Text, PDFWritable>{
    
	TaskAttemptContext job;
	
	@Override
	public RecordWriter<Text, PDFWritable> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		this.job = job;
		return new PDFRecordWriter(job);
	}
	
	class PDFRecordWriter extends RecordWriter<Text, PDFWritable>{
		
		private final Log log = LogFactory.getLog(PDFRecordWriter.class);
		TaskAttemptContext job;
		Path file;
		FileSystem fs;
		int i = 0;
			
		public PDFRecordWriter(TaskAttemptContext job) {
			this.job = job;
		}

		@Override
		public void write(Text key, PDFWritable value) throws IOException, InterruptedException {
			Configuration conf = job.getConfiguration();
			Path name = getDefaultWorkFile(job, null);
			String outfilepath = name.toString();
			String keyname = key.toString();
			Path file = new Path((outfilepath.substring(0, outfilepath.length()-16)) + keyname + ".pdf");
			
			FileSystem fs = file.getFileSystem(conf);
			FSDataOutputStream fileOut = fs.create(file, false);
			
			try {
				Document doc = new Document();
				PdfCopy copy = new PdfCopy(doc, (OutputStream)fileOut);
				
				int i = 0;
				doc.open();
				while(i < value.reader.getNumberOfPages()) {
					i++;
					copy.addPage(copy.getImportedPage(value.reader, i));
				}
				doc.close();
		    }catch(Exception e) {
		    	log.info("PDFOutputFormat - exception during write - " + e.getStackTrace().toString());
		    }
			
			
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			
		}
	}
	

}
