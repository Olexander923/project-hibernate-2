package org.example.dao;

import org.example.domain.FilmText;
import org.hibernate.SessionFactory;

public class FilmTextDAO extends MovieDAO<FilmText> {
    public FilmTextDAO(SessionFactory sessionFactory) {
        super(FilmText.class, sessionFactory);
    }
}
