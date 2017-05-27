package base;

import exception.ExtractException;
import exception.PersistException;
import org.jsoup.nodes.Element;

public class CrawlStep {

    private String id;

    private String name;

    private String ccsQuery;

    private boolean crawlFlag;

    private Extracter extracter;

    private Persister persister;

    public CrawlStep(String name, String ccsQuery) {
        this.name = name;
        this.ccsQuery = ccsQuery;
        this.crawlFlag = true;
    }

    public CrawlStep(String name, String ccsQuery, boolean crawlFlag, Extracter extracter) {
        this.name = name;
        this.ccsQuery = ccsQuery;
        this.crawlFlag = crawlFlag;
        this.extracter = extracter;
    }

    public CrawlStep(String name, String ccsQuery, boolean crawlFlag, Extracter extracter, Persister persister) {
        this.name = name;
        this.ccsQuery = ccsQuery;
        this.crawlFlag = crawlFlag;
        this.extracter = extracter;
        this.persister = persister;
    }

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

    public String getCcsQuery() {
        return ccsQuery;
    }

    public void setCcsQuery(String ccsQuery) {
        this.ccsQuery = ccsQuery;
    }

    public boolean isCrawlFlag() {
        return crawlFlag;
    }

    public void setCrawlFlag(boolean crawlFlag) {
        this.crawlFlag = crawlFlag;
    }

    public Extracter getExtracter() {
        return extracter;
    }

    public void setExtracter(Extracter extracter) {
        this.extracter = extracter;
    }

    public Persister getPersister() {
        return persister;
    }

    public void setPersister(Persister persister) {
        this.persister = persister;
    }

    @FunctionalInterface
    public interface Extracter<T> {

        T extract(Element e) throws ExtractException;
    }

    @FunctionalInterface
    public interface Persister<T> {

        void persist(T e) throws PersistException;
    }

    }
