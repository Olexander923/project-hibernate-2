package org.example.dao;

import org.example.domain.Category;
import org.hibernate.SessionFactory;

public class CategoryDAO extends MovieDAO<Category> {
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
