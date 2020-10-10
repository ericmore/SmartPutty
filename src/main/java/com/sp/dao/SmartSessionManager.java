package com.sp.dao;

import com.sp.entity.ConfigSession;
import com.sp.model.Protocol;
import com.sp.entity.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.stream.Collectors;
@SuppressWarnings({"deprecation","unchecked"})
public class SmartSessionManager {


    private SessionFactory sessionFactory;

    public SmartSessionManager() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();

        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /*
    DB Actions
     */
    public List<Object> getAll(Class clazz) {
        return sessionFactory.openSession().createCriteria(clazz).list();
    }

    public void save(Object entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    public void update(Object entity){
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        } finally {
            session.close();
        }
    }

    public void delete(Object entity) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(entity);
            tx.commit(); // Flush happens automatically
        } catch (RuntimeException e) {
            tx.rollback();
            throw e; // or display error message
        } finally {
            session.close();
        }
    }






    /*
    API service
     */
    public List<SystemConfig> getAllSystemConfigs() {
        return sessionFactory.openSession().createCriteria(SystemConfig.class).list();
    }

    public List<ConfigSession> getAllCSessions() {
        return sessionFactory.openSession().createCriteria(ConfigSession.class).list();
    }

    public void updateSystemConfig(String key, String newValue){
        List<SystemConfig> list = getAllSystemConfigs();
        SystemConfig config = list.stream().filter(e-> StringUtils.equals(e.getKey(), key)).findAny().orElse(null);
        config.setValue(newValue);
        update(config);
    }

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
