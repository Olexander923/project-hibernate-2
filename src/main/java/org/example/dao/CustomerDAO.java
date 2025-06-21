package org.example.dao;

import org.example.domain.Customer;
import org.hibernate.SessionFactory;

public class CustomerDAO extends MovieDAO<Customer> {
    public CustomerDAO(SessionFactory sessionFactory) {
        super(Customer.class, sessionFactory);
    }
}
