package base;

/**
 * Created by daile on 2017/4/15.
 */
public class CrawlTask {

    private String id;

    private String name;

    private String entry;

    private String baseUrl;

    private CrawlRule rule;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public CrawlRule getRule() {
        return rule;
    }

    public void setRule(CrawlRule rule) {
        this.rule = rule;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String toString() {
        return "CrawlTask{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", entry='" + entry + '\'' +
                ", rule=" + rule +
                '}';
    }
}


