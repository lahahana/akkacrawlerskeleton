package base;

/**
 * Created by daile on 2017/4/16.
 */
public class CrawlUrl {

    private String crawlTaskId;

    private String url;

    private String result;

    private long timeCost;

    private CrawlTask crawlTask;

    public CrawlUrl(String crawlTaskId, String url) {
        this.crawlTaskId = crawlTaskId;
        this.url = url;
    }

    public String getCrawlTaskId() {
        return crawlTaskId;
    }

    public String getUrl() {
        return url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }
}
