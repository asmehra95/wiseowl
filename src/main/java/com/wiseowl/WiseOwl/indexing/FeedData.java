package com.wiseowl.WiseOwl.indexing;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
//import java.nio.file.Files;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.benchmark.byTask.feeds.DocData;
import org.apache.lucene.benchmark.byTask.feeds.EnwikiContentSource;
import org.apache.lucene.benchmark.byTask.feeds.NoMoreDataException;
import org.apache.lucene.benchmark.byTask.utils.Config;
import org.apache.solr.client.solrj.SolrClient;
//import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.List;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.SolrInputDocument;

import com.wiseowl.WiseOwl.wikiClean.WikiClean;
import com.wiseowl.WiseOwl.wikiClean.WikiCleanBuilder;

import indexGUI.stop_pause;
public class FeedData {
		public static String indexType=null;
		public static String url,filez="",name="";
		public static int skip=0,back=0;
		public static RandomAccessFile randomAccessFile = null;
        public static int i,numDocs,batchSize = 5;
		private transient static Log log = LogFactory.getLog(FeedData.class);
        public static boolean running = false,isPaused=false;    
        public static void reset()
        {
        	indexType=null;
        	filez="";	name="";
        	skip=0;		back=0;
        	running = false;	isPaused=false; 
        }
		public int index() throws IOException, SolrServerException
		{       numDocs = Integer.MAX_VALUE;
                        i=0;
			SolrClient client = new HttpSolrClient.Builder(url).build();
			int result=0;
			long id=this.readID();
			if(indexType.equalsIgnoreCase("others"))
			{
			ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
			File file = new File(filez);	
                       if (file.isFile()) { 	
					   req.setParam("literal.id", ""+this.readID());
					   req.addFile(file,"");
					   //new File(file.getName()),""
					   System.out.println("Before Action "+FeedData.filez);
					   req.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
					   System.out.println("After Action "+FeedData.filez);
					   System.out.println("Sending "+file.getName()+" to WiseOwl to make him Wiser!");
					   client.request(req);
					   System.out.println("Wise Owl Learned about "+file.getName()+" and got Wiser!");
					   id++;
					}
			}else if(indexType.equalsIgnoreCase("wikipedia"))
			{
            	System.out.println("in index wikipedia ");            	
				File file = new File(filez);
            	System.out.println("after list files ");
						if(file.isFile() && file.getName().endsWith(".xml") ) {
                    	System.out.println("in index .xml ");
				    	EnwikiContentSource contentSource = new EnwikiContentSource();
				    	WikiClean cleaner=new WikiCleanBuilder().build();
				        Properties properties = new Properties();
				        //fileName = config.get("docs.file", null);
				        String filePath = file.getAbsolutePath();
				        properties.setProperty("docs.file", filePath);
				        properties.setProperty("doc.maker.forever", "false");
				        contentSource.setConfig(new Config(properties));
				        contentSource.resetInputs();
				        //docMaker.openFile();
				        DocData docData1 = new DocData();
				        for(int j=0;j<FeedData.skip;j++)
				        {
				        	try {
								docData1=contentSource.getNextDocData(docData1);
							} catch (NoMoreDataException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	System.out.println(docData1.getTitle());
				        }
				        ArrayList<SolrInputDocument> docs = new ArrayList<SolrInputDocument>(1000);
				        i = FeedData.skip;
				        SolrInputDocument sDoc = null;
				        long start = System.currentTimeMillis();
				        try {
				          DocData docData = new DocData();

                          while ((docData = contentSource.getNextDocData(docData)) != null && i < numDocs && isPaused==false && running==true) 
				          {
				            int mod = i % batchSize;
				            sDoc = new SolrInputDocument();
				            docs.add(sDoc);
				            id++;
				            sDoc.addField("id",""+id);
				            sDoc.addField("content", cleaner.clean(docData.getBody()));
				            sDoc.addField("title", docData.getTitle());
				            sDoc.addField("author", docData.getName());
				            sDoc.addField("content_type", "Wikipedia Data");
                            String str = docData.getTitle();
                            indexGUI.stop_pause.area.append(str+"\n");
                            try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				            //client.add(sDoc);
				            if (mod == batchSize - 1) {
				            	System.out.println("Commiting ");
				              log.info("Sending: " + docs.size() + " docs" + " total sent for this file: " + i);
				             client.add(docs);
				             client.commit();
				             FeedData.skip=i;
				             System.out.println("now skip is  "+ FeedData.skip);
				             addData(FeedData.randomAccessFile.getFilePointer()-back-1);
				             System.out.println("now seek is  "+ (FeedData.randomAccessFile.getFilePointer()-back-1));
//                           this.writeID(id);
				              docs.clear();
				            }
				            i++;
				          }
                          System.out.println("Value of Ispaused : "+FeedData.isPaused);
				        } catch (NoMoreDataException e) {

				        }
				        long finish = System.currentTimeMillis();
				        if (log.isInfoEnabled()) {
				          log.info("Indexing took " + (finish - start) + " ms");
				        }
				        if (docs.size() > 0) {
				         // client.add(docs);
				        }
				        result = i + docs.size();
				        System.out.println("finished"+result);
				      
					}
				System.out.println("after for");
			}
			if(isPaused==false){
			System.out.println("finished");
			FeedData.skip=-1;
			}
			else{
				System.out.println("Indexing is Paused .");
				System.out.println("value of skip : "+FeedData.skip);
			}
			addData(FeedData.randomAccessFile.getFilePointer()-back-1);
			this.writeID(id);
			client.commit();
			FeedData.running=false;
	        client.optimize();
			return result;
		}
		public long readID() 
		{
			// function to open and read unique id from text file 
			long id=0;
			Scanner s=null;
			try {
			s=new Scanner(new File("toIndex/settings/uniqueId.txt"));
			id=s.nextLong();
			
			}catch (FileNotFoundException e)
			{
				System.out.println("Can't find uniqueId.txt , It must contain starting point of doc Id as integer in ./toIndex/settings filez");
			}finally
			{
				s.close();
			}
			return id;
		}
		public void writeID(long id)
		{
			Writer writer = null;

			try {
			    writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream("toIndex/settings/uniqueId.txt"), "utf-8"));
			    writer.write(""+id);
			} catch (IOException ex) {
			  // report
			} finally {
			   try {writer.close();} catch (Exception ex) {/*ignore*/}
			}
		}
		public static int readSkip() throws IOException
	    {//new File("toIndex/settings/indexed.txt"
				try {
					randomAccessFile=new RandomAccessFile("toIndex/settings/"+FeedData.indexType+".txt","rw");
				}catch (IOException e)
				{
					System.out.println("Can't find one of the files\nUsage MakeCorpus <sentence templates> <names> <output File>");
				}
				String value=null;
				long size = randomAccessFile.length();
				if(size==0)
					addData(size);
			    //Finding position of File Pointer
			    long positionOfFilePointer = randomAccessFile.getFilePointer();
			    value=searchData();
			    if(value==null)
			    	return -2;
			    else
	                return Integer.parseInt(value);
		}

	    public static void addData(long size) throws IOException {

	        //seeking position at end of file
	    	System.out.println("the seek is : "+size);
	        randomAccessFile.seek(size);
	        //char off[] = new char[8];
	        String s = ""+FeedData.skip;
	        int l=s.length(),i;
	        /*for(int i=0;i<7;i++)
	        {
	        	if(i<(s).length())
	        		off[i]=s.charAt(i);
	        	else
	        	off[i]=' ';
	        }
	        System.out.println(off);
	        String s1 = off.toString();*/
	        for(i=0;i<7-l;i++)
	        {
	        	s+=" ";
	        }
	        System.out.println(s);
	        StringBuffer stringBuffer = new StringBuffer();
	        	stringBuffer.append(FeedData.name).append(":").append(s);
	        back = stringBuffer.length();
	        /*if(randomAccessFile.getFilePointer() != 0){
	        randomAccessFile.writeBytes(System.getProperty("line.separator"));
	        }*/
	        randomAccessFile.writeBytes(stringBuffer.toString());
	        randomAccessFile.writeBytes(System.getProperty("line.separator"));
	      }

	    public static String searchData() throws IOException {
	        //Setting file pointer to start of file
	        randomAccessFile.seek(0);
	        System.out.println("in search data ");
	        String skip=null;
	        String data = randomAccessFile.readLine();
	        while (data != null ){
	          String[] recordToBeSearched = data.split(":");
	          String name = recordToBeSearched[0];
	          if(name != null && name.equals(FeedData.name)){
	        	  skip=recordToBeSearched[1].trim();
	        	  back = recordToBeSearched[0].length()+recordToBeSearched[1].length()+1;
	        	  break ;
	          }
	          data = randomAccessFile.readLine();
	        }
	        System.out.println("in search data "+skip);
	        return skip;
	        
	      }

}
