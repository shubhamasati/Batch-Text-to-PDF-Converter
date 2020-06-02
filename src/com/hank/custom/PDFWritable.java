package com.hank.custom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFWritable implements Writable{
	
	private static final Log log = LogFactory.getLog(PDFWritable.class);
	
	private byte[] bytes;
	public PdfReader reader = null;
	
	public PDFWritable() {
		
	}
	
	public PDFWritable(byte[] bytes) {
		this.bytes = bytes;
	}
    
	@Override
	public void write(DataOutput out) throws IOException {
		Document document = new Document(PageSize.LETTER, 40,40,40,40);
		
		try {
			
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, output);
			document.open();
			
			Paragraph p = new Paragraph(new String(bytes));
			document.add(p);
			
			document.close();
			WritableUtils.writeVInt(out, output.size());
			out.write(output.toByteArray(), 0, output.size());
		} catch( Exception e) {
			log.error("PDFWritable write - " + getStackTrace(e));
		}
		
		
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		
		int newLength = WritableUtils.readVInt(in);
		bytes = new byte[newLength];
		in.readFully(bytes, 0, newLength);
		
		try {
			reader = new PdfReader(bytes);
		}catch(Exception e) {
			log.error("PDFWritable readFiels - " + getStackTrace(e));
		}
		
	}

	private String getStackTrace(Exception e) {
		// TODO Auto-generated method stub
		return e.getStackTrace().toString();
	}

}
