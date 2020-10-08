package com.sp.Dao;

import com.sp.Control.SmartConfiguration;
import com.sp.Model.ConfigSession;
import com.sp.Model.Protocol;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SmartSessionManager {


    private SessionFactory sessionFactory;

    public SmartSessionManager() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration().configure(new ClassPathResource("hibernate.cfg.xml").getFile()).buildSessionFactory();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
    DB Actions
     */
    public void save(Object entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit(); // Flush happens automatically
        }
        catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        }
        finally {
            session.close();
        }

    }

    public void delete(Object entity) {
        sessionFactory.openSession().delete(entity);
    }

    public List<ConfigSession> getAllCSessions() {
        return sessionFactory.openSession().createCriteria(ConfigSession.class).list();
    }


    /*
    API service
     */
    public List<ConfigSession> queryCSessionByHost(String host) {
        return getAllCSessions().stream().filter(e -> e.getHost().equals(host)).collect(Collectors.toList());
    }

    public List<ConfigSession> queryCSessionByHostUser(String host, String user) {
        return getAllCSessions().stream().filter(e -> e.getHost().equals(host) && e.getUser().equals(user)).collect(Collectors.toList());
    }

    public ConfigSession queryCSessionByHostUserProtocol(String host, String user, Protocol protocal) {
        return getAllCSessions().stream().filter(e -> e.getHost().equals(host)
                && e.getUser().equals(user)
                && e.getProtocol() == protocal).findAny().orElse(null);
    }

    public ConfigSession queryCSessionBySession(ConfigSession csession) {
        return getAllCSessions().stream().filter(e -> e.getId() == csession.getId()).findAny().orElse(null);
    }


    public void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }


}
