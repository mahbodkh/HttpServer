package app.core.essential;

import app.core.error.InnerErrorException;
import app.core.http.HttpSession;
import app.core.util.TimeHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Ebrahim with ❤️ on 20 December 2019.
 */

public class HttpSessionManager {
    private AtomicLong count = new AtomicLong(1L);
    private Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    public HttpSession getSession(String sessionId) {
        HttpSession session = this.sessionMap.get(sessionId);
        if (session == null)
            throw new InnerErrorException();
        else
            return session;
    }

    public HttpSession initSession() {
        String sessionId = TimeHandler.getDate() + "" + Thread.currentThread().getId() + "" + count.getAndIncrement();
        HttpSession session = new HttpSession(sessionId);
        this.sessionMap.put(sessionId, session);
        return session;
    }
}