package org.example;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

public class Parser {
    public final String mainLink;
    private CopyOnWriteArraySet<String> siteMap;
    private final String[] filesFormat = {".pdf", ".doc", ".php", ".docx", ".webp", ".xlsx"};

    public Parser(String link) {
        this.mainLink = link;
        siteMap = new CopyOnWriteArraySet<>();
    }

    protected CopyOnWriteArraySet<String> getUrls(String parentUrl) {
        CopyOnWriteArraySet<String> parsedLinks = new CopyOnWriteArraySet<>();
        try {
            Thread.sleep(110);
            Connection.Response response = Jsoup.connect(parentUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML," +
                            " like Gecko) Chrome/104.0.0.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    .ignoreHttpErrors(true)
                    .execute();
            Document html = response.parse();
            Elements allPageUrls = html.getElementsByTag("a");
            for (Element element : allPageUrls) {
                String childUrl = element.attr("abs:href");
                boolean fileLink = isFile(childUrl);
                boolean suitableLink = suitableLink(childUrl);
                if (suitableLink && !fileLink) {
                    siteMap.add(childUrl);
                    parsedLinks.add(childUrl);
                }
                if (suitableLink && fileLink) {
                    siteMap.add("zzzFile - " + childUrl);
                }
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return parsedLinks;
    }

    private boolean isFile(String url) {
        url = url.toLowerCase();
        for (String s : filesFormat) {
            if (url.contains(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean suitableLink(String url) {
        return url.startsWith(mainLink)
                && !url.contains("#")
                && !siteMap.contains(url)
                && !url.contains("null");
    }
    public CopyOnWriteArraySet<String> getSiteMap() {
        return siteMap;
    }
}
