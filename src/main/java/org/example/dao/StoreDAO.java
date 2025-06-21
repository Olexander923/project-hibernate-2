package org.example.dao;

import org.example.domain.Store;
import org.hibernate.SessionFactory;

public class StoreDAO extends MovieDAO<Store> {
    public StoreDAO(SessionFactory sessionFactory) {
        super(Store.class, sessionFactory);
    }
}
