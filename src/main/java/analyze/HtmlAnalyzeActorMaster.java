package analyze;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import base.CrawlUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by daile on 2017/4/16.
 */
public class HtmlAnalyzeActorMaster extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(HtmlAnalyzeActorMaster.class);

    protected Router router;

    private volatile AtomicInteger benchmark = new AtomicInteger(1000);

    public HtmlAnalyzeActorMaster() {
        super();
        init();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof CrawlUrl) {
            router.route(message, getSelf());
        }else if(message instanceof Terminated) {
            ActorRef terminatedActor = ((Terminated) message).actor();
            logger.error("Actor terminated: {}", terminatedActor.path());
            router.removeRoutee(terminatedActor);
            return;
        }else {
            unhandled(message);
        }
    }

    public void expendActors(int number) {
        int prevSize = benchmark.addAndGet(number);
        for(int i = prevSize; i < benchmark.get(); i++) {
            ActorRef worker = createHtmlAnalyzeActor(i);
            getContext().watch(worker);
            router.addRoutee(new ActorRefRoutee(worker));
        }
    }

    protected ActorRef createHtmlAnalyzeActor(int sequence) {
        return getContext().actorOf(Props.create(HtmlAnalyzeActor.class), "analyzeActor-" + sequence);
    }

    private void init() {
        ArrayList<Routee> routes = new ArrayList<>();
        for(int i = 0; i < benchmark.get(); i++) {
            ActorRef worker = createHtmlAnalyzeActor(i);
            getContext().watch(worker);
            routes.add(new ActorRefRoutee(worker));
        }

        router = new Router(new SmallestMailboxRoutingLogic(), routes);
        logger.info("HtmlActorMaster init done");
    }

    public int getBenchmark() {
        return benchmark.get();
    }

    public void setBenchmark(int benchmark) {
        this.benchmark.set(benchmark);
    }
}
