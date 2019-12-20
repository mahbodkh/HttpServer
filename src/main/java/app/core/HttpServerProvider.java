package app.core;


import app.core.aop.Filter;
import app.core.config.InitStartup;
import app.core.essential.ThreadPoolHandler;
import app.core.http.ExchangeHandler;
import app.core.http.HttpRequest;
import app.core.http.HttpResponse;
import app.core.http.enums.HttpVersionTypes;
import app.core.http.enums.StatusCodeTypes;
import app.core.log.Logger;
import app.core.parser.HttpParser;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static app.core.config.ConfigLoader.load;


/**
 * Created by Ebrahim with ❤️ on 13 December 2019.
 */


public class HttpServerProvider implements Runnable {
    private static final Logger log = Logger.getLogger(HttpServerProvider.class);
    //
    private Map<SocketChannel, HttpParser> requestParserMap = new HashMap<SocketChannel, HttpParser>();
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private Filter filter;
    private ThreadPoolHandler doService;
    //

    public static void starter() {
        HttpServerProvider starter = new HttpServerProvider();
        starter.init();
        new Thread((starter)).start();
    }

    private void init() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(load().getHost(), load().getPort()));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            System.out.printf("\n\t----------------------------------------------------------\n\t" +
                            "Application '%s' is running!\n\t" +
                            "Access URL: http://%s:%d\n\t" +
                            "Thread Pool: '%d'\n\t" +
                            "Log policy write to disk: '%s'\n\t" +
                            "Log Address: '%s'\n\t" +
                            "----------------------------------------------------------\n\n"
                    , load().getAppName()
                    , load().getHost()
                    , load().getPort()
                    , load().getPoolSize()
                    , load().getLogPolicy()
                    , load().getLogAddress()
            );


            log.info(String.format("server is boot: %s ", serverSocketChannel.toString()));
            doService = new ThreadPoolHandler(load().getPoolSize());
            log.info(String.format("The pool initialized with size: %s", load().getPoolSize()));
            InitStartup startup = new InitStartup();
            log.info(String.format("Start up time: " + "'%d'" + " Millisecond", System.currentTimeMillis() - load().getStart()));


            // prepare
            filter = new Filter();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }


    @Override
    public void run() {
        while (true) {
            try {
                int n = selector.select();
                if (n <= 0)
                    continue;
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (!key.isValid())
                    continue;
                try {
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                    if (key.isWritable()) {
                        write(key);
                    }
                } catch (Exception e) {
                    if (key != null && key.isValid()) {
                        key.cancel();
                        log.error(String.format("SelectionKey is not null but, there is an issue: {%s} ", e.getMessage()));
                        try {
                            key.channel().close();
                        } catch (IOException e1) {
                            log.error(String.format("exception to close channel: {%s}", e1.getMessage()));
                        }
                    }
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = channel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        HttpParser httpParser = requestParserMap.get(channel);
        if (httpParser == null) {
            httpParser = new HttpParser(key, requestParserMap);
            requestParserMap.put(channel, httpParser);
        }
        HttpResponse response = new HttpResponse();
        response.addHeader("Server", load().getAppName());
        try {
            if (httpParser.parse()) {
                HttpRequest request = httpParser.getRequest();
                ExchangeHandler exchange = new ExchangeHandler(request, response, key, this.filter, this.selector);
                doService.execute(exchange);
                key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
                httpParser.clear();
            }
        } catch (Exception e) {
            httpParser.clear();
            response.setStatusCode(StatusCodeTypes._400);
            response.setHttpVersion(HttpVersionTypes.Http1_1);
            response.setBody("request syntax error");
            try {
                response.getOutput().write(StatusCodeTypes._400.name().getBytes(StandardCharsets.ISO_8859_1));
                byte[] bodyBytes = response.getOutput().toByteArray();
                response.addHeader("Content-Length", Integer.toString(bodyBytes.length));
                byte[] headerBytes = response.headersToBytes();
                ByteBuffer responseBuffer = ByteBuffer.allocate(headerBytes.length + bodyBytes.length);
                responseBuffer.put(headerBytes).put(bodyBytes);
                responseBuffer.flip();
                key.attach(responseBuffer);
                key.interestOps(SelectionKey.OP_WRITE);
                selector.wakeup();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void write(SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        if (buffer == null || !buffer.hasRemaining())
            return;
        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.write(buffer);
        if (!buffer.hasRemaining()) {
            key.interestOps(SelectionKey.OP_READ);
            buffer.clear();
        }
    }


}
