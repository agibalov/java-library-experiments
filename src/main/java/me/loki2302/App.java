package me.loki2302;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.phonetic.DoubleMetaphoneFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class App {
    public static void main(String[] args) throws IOException, ParseException {
        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                final StandardTokenizer src = new StandardTokenizer();
                src.setMaxTokenLength(256);
                TokenStream tok = new StandardFilter(src);
                tok = new LowerCaseFilter(tok);
                tok = new StopFilter(tok, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
                tok = new DoubleMetaphoneFilter(tok, 256, true);
                return new TokenStreamComponents(src, tok) {
                    @Override
                    protected void setReader(final Reader reader) throws IOException {
                        src.setMaxTokenLength(256);
                        super.setReader(reader);
                    }
                };
            }
        };

        Directory directory = new RAMDirectory();

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        if (true) {
            Document document = new Document();
            document.add(new TextField("title", "bruce", Field.Store.YES));
            indexWriter.addDocument(document);
        }

        if (true) {
            Document document = new Document();
            document.add(new TextField("title", "loki", Field.Store.YES));
            indexWriter.addDocument(document);
        }
        indexWriter.close();

        Query query = new QueryParser("title", analyzer).parse("broos");
        DirectoryReader directoryReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
        ScoreDoc[] scoreDocs = indexSearcher.search(query, 1000).scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.printf("%s: %s\n", scoreDoc, indexSearcher.doc(scoreDoc.doc));
        }
    }
}
