package examples._2;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class LuceneService {

	
	private Analyzer analyzer =null;
	
	private Directory directory =null;
	
	
	//Create Index
	
	public void createIndex(){
		
		try {
			
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_36, analyzer);
			
			IndexWriter iw = new IndexWriter(directory, iwc);
			
			
			
			
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
}
