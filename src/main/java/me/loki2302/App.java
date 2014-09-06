package me.loki2302;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException, FeedException {
        URL feedUrl = new URL("https://signalvnoise.com/posts.rss");

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));

        System.out.printf("feed title: %s\n", feed.getTitle());
        List<SyndEntry> entries = feed.getEntries();
        for(SyndEntry entry : entries) {
            System.out.printf("entry: %s\n", entry.getUri());
            System.out.printf("title: %s\n", entry.getTitle());
            System.out.printf("date: %s\n", entry.getPublishedDate());
            System.out.printf("description: %s\n", entry.getDescription().getValue());
            System.out.printf("link: %s\n", entry.getLink());
            System.out.printf("-----------------------------\n");
        }
    }
}
