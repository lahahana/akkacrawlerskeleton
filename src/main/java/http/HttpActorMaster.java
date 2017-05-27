package http;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import base.CrawlUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static akka.actor.Props.create;

/**
 * Created by daile on 2017/4/16.
 */
public class HttpActorMaster extends UntypedActor{

    private static final Logger logger = LoggerFactory.getLogger(HttpActor.class);

    private volatile AtomicInteger benchmark = new AtomicInteger(1000);

    protected Router router;

    public HttpActorMaster() {
        super();
        init();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof CrawlUrl){
            router.route(message, getSelf());
        }else if(message instanceof Terminated) {
            ActorRef terminatedActor = ((Terminated) message).actor();
            router.removeRoutee(terminatedActor);
            logger.error("Actor terminated: {}", terminatedActor.path());
            return;
        }else {
            unhandled(message);
        }

    }

    public void expendActors(int number) {
        int prevSize = benchmark.getAndAdd(number);
        for(int i = prevSize; i < benchmark.get(); i++) {
            ActorRef worker = createHttpActor(i);
            getContext().watch(worker);
            router.addRoutee(new ActorRefRoutee(worker));
        }
    }

    private void init() {
        ArrayList<Routee> routes = new ArrayList<>();
        for(int i = 0; i < benchmark.get(); i ++) {
            ActorRef worker = createHttpActor(i);
            getContext().watch(worker);
            routes.add(new ActorRefRoutee(worker));
        }
        /**
         * RoundRobinRoutingLogic
         * BroadcastRoutingLogic
         * RandomRoutingLogic
         * SmallestMailboxRoutingLogic
         */
        router = new Router(new RoundRobinRoutingLogic(), routes);
        logger.info("HttpActorMaster init done");
    }

    protected ActorRef createHttpActor(int sequence) {
       return getContext().actorOf(create(HttpActor.class), "httpActor-" + sequence);
    }

    public int getBenchmark() {
        return benchmark.get();
    }

    public void setBenchmark(int benchmark) {
        this.benchmark.set(benchmark);
    }
}
