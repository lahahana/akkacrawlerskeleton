package analyze;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import base.*;
import exception.ExtractException;
import exception.PersistException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by daile on 2017/4/15.
 */
public class HtmlAnalyzeActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(HtmlAnalyzeActor.class);

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof  CrawlUrl) {
            analyze((CrawlUrl)message);
        }else {
            unhandled(message);
        }
    }

    protected void analyze(CrawlUrl crawlUrl) {
        Document doc = Jsoup.parse(crawlUrl.getResult());
        String crawlTaskId = crawlUrl.getCrawlTaskId();

        //get crawlTask from local cache， should get from remote cache in the future
        CrawlTask crawlTask = null;

        CrawlRule crawlRule = crawlTask.getRule();
        Elements elements = doc.getAllElements();

        UrlCrawlStep urlCrawlStep = crawlRule.getUrlCrawlStep();
        urlCrawlStep.setPersister(url -> {
            if(url != null){
                ActorSelection actorSelection = getContext().actorSelection("akka://Crawler-ActorSystem/user/master/httpMaster");
                actorSelection.tell(url, getSelf());
            }
        });
        executeCrawlStep(elements, urlCrawlStep);

        List<CrawlStep> crawlSteps = crawlRule.getCrawlSteps();
        crawlSteps.parallelStream()
                    .forEach(x -> {
                        executeCrawlStep(elements, x);
                    });
    }

    private void executeCrawlStep(Elements elements, CrawlStep x) {
        Elements elems = elements.select(x.getCcsQuery());
        if(x.isCrawlFlag()) {
            List list = elems.stream()
                    .map(ele -> {
                        try {
                            return x.getExtracter().extract(ele);
                        } catch (ExtractException e) {
                            logger.error(e.toString());
                        }
                        return null;
                    }).collect(Collectors.toList());
            list.stream()
                    .forEach(r ->
                    {
                        try {
                            if(r == null)
                                return;
                            x.getPersister().persist(r);
                        } catch (PersistException e) {
                            logger.error(e.toString());
                        }
                    });
        }
    }

}
