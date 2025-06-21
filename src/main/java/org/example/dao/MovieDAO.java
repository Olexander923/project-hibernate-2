package org.example.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public abstract class MovieDAO<T> {
    private final Class<T> clazz;
    private SessionFactory sessionFactory;

    public MovieDAO(final Class<T> clazzToSet,SessionFactory sessionFactory) {
           this.clazz = clazzToSet;
           this.sessionFactory = sessionFactory;
    }

    public T getById(final long id) {
        return (T) getCurrentSession().get(clazz,id);
    }

    public List<T> getItems(int offset,int count) {
        Query query = getCurrentSession().createQuery("from " + clazz.getName());
        query.setFirstResult(offset);
        query.setMaxResults(count);
        return query.getResultList();
    }

    public List<T> findAll() {
        return getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public T create(final T entity) {
        getCurrentSession().saveOrUpdate(entity);
                return entity;
    }

    public T save(final T entity) {
        getCurrentSession().save(entity);
        return entity;
    }

    public T update(final T entity) {
        return (T) getCurrentSession().merge(entity);
    }

    public void delete(final T entity) {
        getCurrentSession().delete(entity);
    }

    public void deleteById(final long entityId) {
        final T entity = getById(entityId);
        delete(entity);
    }
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
