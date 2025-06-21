package org.example.dao;

import org.example.domain.Address;
import org.hibernate.SessionFactory;

public class AddressDAO extends MovieDAO<Address>{
    public AddressDAO(SessionFactory sessionFactory) {
        super(Address.class, sessionFactory);
    }
}
