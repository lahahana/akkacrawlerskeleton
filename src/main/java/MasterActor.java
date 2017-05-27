import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import analyze.HtmlAnalyzeActorMaster;
import base.CrawlTask;
import base.CrawlUrl;
import http.HttpActorMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by daile on 2017/4/15.
 */
public class MasterActor extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(MasterActor.class);

    public ActorRef httpMasterRef;

    public ActorRef htmlMasterRef;

    public MasterActor() {
        super();
        httpMasterRef = getContext().actorOf(Props.create(HttpActorMaster.class), "httpMaster");
        getContext().watch(httpMasterRef);
        htmlMasterRef = getContext().actorOf(Props.create(HtmlAnalyzeActorMaster.class), "htmlMaster");
        getContext().watch(htmlMasterRef);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof CrawlTask){
            CrawlTask crawlTask = (CrawlTask)message;
            logger.info(crawlTask.toString());
            CrawlUrl crawlUrl = new CrawlUrl(crawlTask.getId(), crawlTask.getEntry());
            httpMasterRef.tell(crawlUrl, this.getSelf());
        }else if(message instanceof Terminated) {
            ActorRef terminatedActor = ((Terminated) message).actor();
            logger.error("Actor terminated: {}", terminatedActor.path());
            getContext().system().shutdown();
            return;
        }else {
            unhandled(message);
        }
    }
}
