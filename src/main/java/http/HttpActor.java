package http;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import base.CrawlUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import proxy.Proxy;
import proxy.ProxySelector;
import util.ReqFaker;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by daile on 2017/4/14.
 */
public class HttpActor extends UntypedActor{

    private static final Logger logger = LoggerFactory.getLogger(HttpActor.class);

    private static long connectionTimeout = 3000L;

    private static int maxTryTimes = 10;

    private static ProxySelector proxySelector;

    OkHttpClient client = new OkHttpClient.Builder()
                                .proxy(null)
                                .connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                                .build();

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof CrawlUrl) {
            CrawlUrl crawlUrl = (CrawlUrl)message;
            String content = sendRequest(crawlUrl);
            crawlUrl.setResult(content);

            //invoke htmlActorMaster analyze result
            ActorSelection htmlMasterActorSelection = getContext().system().actorSelection("akka://Crawler-ActorSystem/user/master/htmlMaster");
            htmlMasterActorSelection.tell(crawlUrl, getSelf());
        }else {
            unhandled(message);
        }
    }

    String sendRequest(CrawlUrl crawlUrl) throws Exception {
        logger.info("Crawl url: {}", crawlUrl.getUrl());
        int tryCount = 0;
        Request request = new Request.Builder()
                                     .url(crawlUrl.getUrl())
                                     .build();

        request = ReqFaker.enrichUserAgent(request);
        String content = "";
        while(tryCount ++ < maxTryTimes) {
            Response response = client.newCall(request).execute();

            long requestDuration = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
            crawlUrl.setTimeCost(requestDuration);

            byte[] contentBytes = response.body().bytes();
            MediaType mediaType = response.body().contentType();
            content = new String(contentBytes, mediaType.charset());
            if(isIpBlocked(response, content)) {
                refreshProxySetting();
            }else {
                break;
            }
            response.close();
        }

        return content;
    }

    private boolean isIpBlocked(Response response, String content) {
        if(!response.isSuccessful() || content == null || content.trim().length() == 0) {
            return true;
        }
        return false;
    }

    private void refreshProxySetting() throws Exception {
        Proxy proxy = proxySelector.selectProxy();
        client = client.newBuilder().
                proxy(new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(proxy.getIp(),proxy.getPort())))
                .build();
    }

}
