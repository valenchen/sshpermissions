/**
 * 
 */
package com.tgyt.lucene.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/** 
  * @ClassName: Indexer 
  * @Description: 写一个关于为数据库家里索引的工具。
  * @author sunqiang sunqiangbingxue@sina.com
  * @date 2012-8-6 下午1:40:19 
  *  
  */
public class Indexer {

	/**
	 * 建立索引
	 * 
	 * @param filePath
	 *            需要建立索引的文件的存放路径
	 * @throws IOException
	 */
	public static void createIndex(List<Document> documentList) throws IOException {
		// 在当前路径下创建一个叫indexDir的目录
		File indexDir = new File("./luceneDir");
		// 创建索引目录
		Directory directory = FSDirectory.open(indexDir);
		// 创建一个分词器
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
		// 创建索引配置器
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
				Version.LUCENE_36, analyzer);
		LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
		// 设置segment添加文档(Document)时的合并频率
		// 值较小,建立索引的速度就较慢
		// 值较大,建立索引的速度就较快,>10适合批量建立索引
		mergePolicy.setMergeFactor(50);
		// 设置segment最大合并文档(Document)数
		// 值较小有利于追加索引的速度
		// 值较大,适合批量建立索引和更快的搜索
		mergePolicy.setMaxMergeDocs(5000);
		// 启用复合式索引文件格式,合并多个segment
		mergePolicy.setUseCompoundFile(true);
		indexWriterConfig.setMergePolicy(mergePolicy);
		// 设置索引的打开模式
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		// 创建索引器
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		/*File fileDir = new File(filePath);
		for (File file : fileDir.listFiles()) {
			// Document是Lucene的文档结构，需要索引的对象都要转换为Document
			Document document = new Document();
			// 文件名,可查询,分词,存储到索引库记录中
			document.add(new Field("name", getFileName(file), Store.YES,
					Index.ANALYZED));
			// 文件路径,可查询,不分词,存储到索引库记录中
			document.add(new Field("path", file.getAbsolutePath(), Store.YES,
					Index.NOT_ANALYZED));
			// 大文本内容,可查询,不存储,实际上可根据文件路径去找到真正的文本内容
			// document.add(new Field("content",new FileReader(file)));

			// 小文本内容，可以存储到索引记录库
			document.add(new Field("content", getFileContent(file), Store.YES,
					Index.ANALYZED));
			// 把文档添加到索引库
			indexWriter.addDocument(document);
		}*/
		for(Document document : documentList){
			// 把文档添加到索引库
			indexWriter.addDocument(document);
		}
		// 提交索引到磁盘上的索引库,关闭索引器
		indexWriter.close();
	}
	

}
