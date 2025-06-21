package org.example.dao;

import org.example.domain.Country;
import org.hibernate.SessionFactory;

public class CountryDAO extends MovieDAO<Country> {
    public CountryDAO(SessionFactory sessionFactory) {
        super(Country.class, sessionFactory);
    }
}
