package org.example.domain;

import org.example.dao.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Main {
    private final SessionFactory sessionFactory;
    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;


    public Main() {
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "validate");
        properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/movie");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "study-mysql");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(Actor.class)
                .addAnnotatedClass(Address.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Film.class)
                .addAnnotatedClass(FilmText.class)
                .addAnnotatedClass(Inventory.class)
                .addAnnotatedClass(Language.class)
                .addAnnotatedClass(Payment.class)
                .addAnnotatedClass(Rental.class)
                .addAnnotatedClass(Staff.class)
                .addAnnotatedClass(Store.class)
                .addProperties(properties)
                .buildSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }


    public static void main(String[] args) {
        Main executeMain = new Main();
        Customer customer = executeMain.createNewCustomer();
        executeMain.customerReturnRentedFilmToStore();
        executeMain.customerRentInventory(customer);
        executeMain.newFilmWasMade();

    }

    private Customer createNewCustomer() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            Store store = storeDAO.getItems(0, 1).get(0);

            City city = cityDAO.getByName("Acua");

            Address address = new Address();

            address.setAddress("Brooklyn street,89");
            address.setPhone("+7 999 888 44 22");
            address.setCity(city);
            address.setDistrict("erterw");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setActive(true);
            customer.setEmail("qwerty@gmail.com");
            customer.setAddress(address);
            customer.setStore(store);
            customer.setFirstName("Goga");
            customer.setLastName("Deysadje");
            customerDAO.save(customer);
            session.getTransaction().commit();
            return customer;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new Customer: " + e.getMessage());
        }
    }

    private void customerReturnRentedFilmToStore() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Rental rental = rentalDAO.getAnyUnreturntedRental();
            rental.setReturnDate(LocalDateTime.now());
            rentalDAO.save(rental);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to return a film: " + e.getMessage());
        }
    }

    private void customerRentInventory(Customer customer) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Film film = filmDAO.getFirstAvailableFilmForRent();
            Store store = storeDAO.getItems(0, 1).get(0);

            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Staff staff = store.getStaff();
            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rentalDAO.save(rental);

            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setCustomer(customer);
            payment.setAmount(BigDecimal.valueOf(30.30));
            payment.setStaff(staff);
            paymentDAO.save(payment);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed to rent inventory: " + e.getMessage());
        }
    }

    private void newFilmWasMade() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            Language language = languageDAO.getItems(0,20).stream().unordered().findAny().get();
            List<Category> categories = categoryDAO.getItems(0,5);
            List<Actor> actors = actorDAO.getItems(0,27);

            Film film = new Film();
            film.setActors(new HashSet<>(actors));
            film.setRating(Rating.NC17);
            film.setSpecialFeatures(Set.of(Feature.DELETED_SCENES,Feature.COMMENTARIES));
            film.setLength((short)120);
            film.setReplacementCost(BigDecimal.ONE);
            film.setRentalRate(BigDecimal.ZERO);
            film.setLanguage(language);
            film.setDescription("history film");
            film.setTitle("seventh sign");
            film.setRentalDuration((byte)90);
            film.setOriginalLanguage(language);
            film.setCategories(new HashSet<>(categories));
            film.setReleaseYear(Year.now());
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setFilm(film);
            filmText.setId(film.getId());
            filmText.setDescription("history film");
            filmText.setTitle("seventh sign");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        } catch (Exception e) {
            throw new RuntimeException("Failed made a film: " + e.getMessage());
        }
    }
}
