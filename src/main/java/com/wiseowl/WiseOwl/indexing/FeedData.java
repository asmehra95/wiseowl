
package com.wiseowl.WiseOwl.indexing;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
//import java.nio.file.Files;
import java.util.Scanner;
import com.wiseowl.WiseOwl.wikiClean.*;
import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
//import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.benchmark.byTask.feeds.DocData;
import org.apache.lucene.benchmark.byTask.feeds.EnwikiContentSource;
import org.apache.lucene.benchmark.byTask.feeds.NoMoreDataException;
import org.apache.lucene.benchmark.byTask.utils.Config;
public class FeedData {
	//static String indexPath="../toIndex/";
		//FeedData feedData=null;
		String indexType=null;
		String folder;
		long skip=0;
		private transient static Log log = LogFactory.getLog(FeedData.class);
		FeedData(String indexType,String folder,int skip)
		{
			this.indexType=indexType;
			this.folder=folder;
			this.skip=skip;
		}
		public static void main (String[] args) throws IOException, SolrServerException {
				
			if(args.length<2)
			{
				System.out.println("Require two arguments: \n <index type> <folder location> <documents Indexed>");
				return ;
			}
			FeedData feedData=null;
			if(args.length == 2)	
				feedData=new FeedData(args[0],args[1],0);
			else if(args.length==3)
				feedData=new FeedData(args[0],args[1],Integer.parseInt(args[2]));
			feedData.index();

		}
		public int index() throws IOException, SolrServerException {
		    return index(Integer.MAX_VALUE, 5);
		  }

		public int index(int numDocs, int batchSize) throws IOException, SolrServerException
		{
			SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/WiseOwl").build();
			File[] files = new File(folder).listFiles();
			if(files==null)
			{
				System.out.println(folder +" Folder Not Found Please Enter a correct folder name!");
				return 0;
			}
			int result=0;
			long id=this.readID();
			if(indexType.equalsIgnoreCase("other"))
			{
			ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
				for (File file : files) {
					   if (file.isFile()) { 	
					   req.setParam("literal.id", ""+this.readID());
					   req.addFile(new File(folder+file.getName()),"");
					   req.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
					   System.out.println("Sending "+file.getName()+" to WiseOwl to make him Wiser!");
					   client.request(req);
					   System.out.println("Wise Owl Learned about "+file.getName()+" and got Wiser!");
					   id++;
					  }
					}
			}else if(indexType.equalsIgnoreCase("wikipedia"))
			{
				for (File file : files) {
					
				    if (file.isFile() && file.getName().endsWith(".xml") ) {
				    	
				    	EnwikiContentSource contentSource = new EnwikiContentSource();
				    	WikiClean cleaner=new WikiCleanBuilder().build();
				        Properties properties = new Properties();
				        String filePath = file.getAbsolutePath();
				        properties.setProperty("docs.file", filePath);
				        properties.setProperty("doc.maker.forever", "false");
				        contentSource.setConfig(new Config(properties));
				        contentSource.resetInputs();
				        DocData docData1 = new DocData();
				        for(int j=0;j<skip;j++)
				        {
				        	try {
								docData1=contentSource.getNextDocData(docData1);
							} catch (NoMoreDataException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	System.out.println(docData1.getTitle());
				        }
				        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>(1000);
				        int i = 0;
				        SolrInputDocument sDoc = null;
				        long start = System.currentTimeMillis();
				        try {
				          DocData docData = new DocData();
				          while ((docData = contentSource.getNextDocData(docData)) != null && i < numDocs) 
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
				            //client.add(sDoc);
				            if (mod == batchSize - 1) {
				              log.info("Sending: " + docs.size() + " docs" + " total sent for this file: " + i);
				             client.add(docs);
					         client.commit();
				             this.writeID(id);
				              docs.clear();
				            }
				            
				            i++;
				          }
				        } catch (NoMoreDataException e) {

				        }
				        long finish = System.currentTimeMillis();
				        if (log.isInfoEnabled()) {
				          log.info("Indexing took " + (finish - start) + " ms");
				        }
				        if (docs.size() > 0) {
				        }
				        result = i + docs.size();
				        System.out.println("finished"+result);
				      }
					}
			}
			System.out.println("finished");
			this.writeID(id);
			client.commit();
	        client.optimize();
			return result;
		}
		public long readID() 
		{
			// function to open and read unique id from text file 
			long id=0;
			Scanner s=null;
			try {
			s=new Scanner(new File(folder+"settings/uniqueId.txt"));
			id=s.nextLong();
			
			}catch (FileNotFoundException e)
			{
				System.out.println("Can't find uniqueId.txt , It must contain starting point of doc Id as integer in ./toIndex/settings folder");
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
			          new FileOutputStream(folder+"settings/uniqueId.txt"), "utf-8"));
			    writer.write(""+id);
			} catch (IOException ex) {
			  // report
			} finally {
			   try {writer.close();}
			   catch (Exception ex) {
				   /*ignore*/
				   }
			}
		}

}
