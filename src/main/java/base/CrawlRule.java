package base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CrawlRule {

    private String id;

    private String name;

    private List<CrawlStep> crawlSteps;

    private UrlCrawlStep urlCrawlStep;

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

    public List<CrawlStep> getCrawlSteps() {
        return crawlSteps;
    }

    public void setCrawlSteps(List<CrawlStep> crawlSteps) {
        this.crawlSteps = crawlSteps;
    }

    public void addCrawlStep(CrawlStep crawlStep) {
        crawlSteps.add(crawlStep);
    }

    public UrlCrawlStep getUrlCrawlStep() {
        return urlCrawlStep;
    }

    public void setUrlCrawlStep(UrlCrawlStep urlCrawlStep) {
        this.urlCrawlStep = urlCrawlStep;
    }

    public static class Builder {

        private CrawlRule crawlRule;

        private UrlCrawlStep urlCrawlStep;

        private List<CrawlStep> crawlSteps;

        public Builder() {
            this.crawlRule = new CrawlRule();
            this.crawlSteps = new ArrayList<>();
        }

        public Builder id(String id) {
            crawlRule.id = id;
            return this;
        }

        public Builder name(String name) {
            crawlRule.name = name;
            return this;
        }

        public Builder addCrawlStep(CrawlStep crawlStep) {
            crawlSteps.add(crawlStep);
            return this;
        }


        public Builder addCrawlSteps(Collection<CrawlStep> crawlSteps) {
            crawlRule.crawlSteps.addAll(crawlSteps);
            return this;
        }

        public Builder addUrlCrawlStep(UrlCrawlStep crawlStep) {
            urlCrawlStep =  crawlStep;
            return this;
        }

        public CrawlRule build() {
            crawlRule.crawlSteps = crawlSteps;
            crawlRule.urlCrawlStep = urlCrawlStep;
            return crawlRule;
        }
    }
}