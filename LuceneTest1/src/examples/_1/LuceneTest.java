package examples._1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * @author Administrator
 *
 */
public class LuceneTest {
	
	//分词器
	private Analyzer analyzer = null;
	
	private Directory directory = null;
	
	public  LuceneTest(){
		analyzer = new IKAnalyzer(true);
		
		//创建一个索引目录
		File file = new File("indexs");
		
		
		
		try {
			directory = FSDirectory.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
//		new LuceneTest().testCreateIndex();
		new LuceneTest().testSearchBook();
	}
	
	public void testCreateIndex() throws IOException {
        
        //建立一个IndexWriter配置,指定匹配的版本,以及分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36,analyzer);
         
        //创建IndexWriter,它负责索引的创建和维护
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
         
        //获取图书信息
        Book book1 = new Book();
        book1.setId(1);
        book1.setName("Java编程思想");
        book1.setAuthor("Bruce Eckel");
        book1.setContent("Thinking in Java should be read cover to cover by every Java programmer, then kept close at hand for frequent reference.");
         
        Book book2 = new Book();
        book2.setId(2);
        book2.setName("建筑的永恒之道");
        book2.setAuthor("亚历山大");
        book2.setContent("《建筑的永恒之道》提出了一个关于建筑设计、建筑和规划的新的理论、思想，该理论的核心是社会成员按照他们自己的存在状态设定他们生活的世界秩序，这一古老方式从根本上构成了新的后工业时代建筑的基础，这些建筑由人们创造。");
         
        //建立Document
        Document doc1 = new Document();
         
        //Store指定Field是否需要存储,Index指定Field是否需要分词索引
        doc1.add(new Field("id",book1.getId().toString(),Store.YES,Index.NOT_ANALYZED));
        doc1.add(new Field("title",book1.getName(),Store.YES,Index.ANALYZED));
        doc1.add(new Field("author",book1.getAuthor(),Store.YES,Index.ANALYZED));
        doc1.add(new Field("content",book1.getContent(),Store.YES,Index.ANALYZED));
         
        //建立Document
        Document doc2 = new Document();
         
        //Store指定Field是否需要存储,Index指定Field是否需要分词索引
        doc2.add(new Field("id",book2.getId().toString(),Store.YES,Index.NOT_ANALYZED));
        doc2.add(new Field("title",book2.getName(),Store.YES,Index.ANALYZED));
        doc2.add(new Field("author",book2.getAuthor(),Store.YES,Index.ANALYZED));
        doc2.add(new Field("content",book2.getContent(),Store.YES,Index.ANALYZED));
         
        //把Document加入到索引中
        indexWriter.addDocument(doc1);
        indexWriter.addDocument(doc2);
         
        //提交改变到索引,然后关闭
        indexWriter.close();
    }
	
	 /**
     * 搜索图书
     * @throws ParseException 
     * @throws IOException 
     * @throws CorruptIndexException 
     * @throws InvalidTokenOffsetsException 
     */
    public void testSearchBook() throws ParseException, CorruptIndexException, IOException, InvalidTokenOffsetsException {
        //搜索的关键词
        String queryKeyWord = "思想";
         
        //创建查询分析器,把查询关键词转化为查询对象Query(单个Field中搜索)
        //QueryParser queryParser = new QueryParser(Version.LUCENE_36,"author",analyzer);//在作者的索引中搜索
         
         
        String[] fields = {"title","content"};
        //(在多个Filed中搜索)
        QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_36,fields,analyzer);
        Query query = queryParser.parse(queryKeyWord);
         
        //获取访问索引的接口,进行搜索
        IndexReader indexReader  = IndexReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
         
        //TopDocs 搜索返回的结果
        TopDocs topDocs = indexSearcher.search(query, 100);//只返回前100条记录
         
        int totalCount = topDocs.totalHits; // 搜索结果总数量
        System.out.println("搜索到的结果总数量为：" + totalCount);
         
        ScoreDoc[] scoreDocs = topDocs.scoreDocs; // 搜索的结果列表
         
        //创建高亮器,使搜索的关键词突出显示
//        Formatter formatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
//        Scorer fragmentScore = new QueryScorer(query);
//        Highlighter highlighter = new Highlighter(formatter,fragmentScore);
//        Fragmenter fragmenter = new SimpleFragmenter(100);
//        highlighter.setTextFragmenter(fragmenter);
         
        List<Book> books = new ArrayList<Book>();
        //把搜索结果取出放入到集合中
        for(ScoreDoc scoreDoc : scoreDocs) {
            int docID = scoreDoc.doc;//当前结果的文档编号
            float score = scoreDoc.score;//当前结果的相关度得分
            System.out.println("score is : "+score);
             
            Document document = indexSearcher.doc(docID);
            Book book = new Book();
            book.setId(Integer.parseInt(document.get("id")));
             
            //高亮显示title
            String title =  document.get("title");
            String highlighterTitle = null;//highlighter.getBestFragment(analyzer, "title", title);
             
            //如果title中没有找到关键词
            if(highlighterTitle == null) {
                highlighterTitle = title;
            }
            book.setName(highlighterTitle);
             
            book.setAuthor(document.get("author"));
             
            //高亮显示content
            String content =  document.get("content");
            String highlighterContent = null;//highlighter.getBestFragment(analyzer, "content", content);
             
            //如果content中没有找到关键词
            if(highlighterContent == null) {
                highlighterContent = content;
            }
            book.setContent(highlighterContent);
             
            books.add(book);
        }
        //关闭
        indexReader.close();
        indexSearcher.close();
        for(Book book : books) {
            System.out.println("book'id is : "+book.getId());
            System.out.println("book'title is : "+book.getName());
            System.out.println("book'author is : "+book.getAuthor());
            System.out.println("book'content is : "+book.getContent());
        }
    }
	
}
