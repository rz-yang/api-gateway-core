import io.netty.channel.Channel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import personal.rezy.SessionServer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ApiTest {

    private final Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test() throws ExecutionException, InterruptedException {
        SessionServer server = new SessionServer();
        Future<Channel> future = Executors.newFixedThreadPool(2).submit(server);
        Channel channel = future.get();

        if (channel == null) throw new RuntimeException("netty server start error channel is null");

        while (!channel.isActive()) {
            logger.info("Netty server启动服务中...");
            Thread.sleep(500);
        }

        logger.info("Netty server启动成功 {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);

    }
}
