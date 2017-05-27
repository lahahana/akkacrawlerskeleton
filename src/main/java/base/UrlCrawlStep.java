package base;

/**
 * Created by daile on 2017/5/4.
 */
public class UrlCrawlStep extends CrawlStep {

    public UrlCrawlStep(String name, String ccsQuery) {
        super(name, ccsQuery);
    }

    public UrlCrawlStep(String name, String ccsQuery, boolean crawlFlag, Extracter extracter) {
        super(name, ccsQuery, crawlFlag, extracter);
    }
}
