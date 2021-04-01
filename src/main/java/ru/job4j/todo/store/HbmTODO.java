package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.function.Function;

public class HbmTODO implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory factory = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private <T> T tx(final Function<Session, T> command) {
        final Session session = factory.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Task add(Task task) {
        return this.tx(
                session -> {
                    System.out.println("start add(Task task)");
                    System.out.println(task);
                    session.save(task);
                    return task;
                }
        );
    }

    @Override
    public User addUser(User user) {
                return this.tx(
                session -> {
                    System.out.println("start addUser");
                    System.out.println(user);
                    session.save(user);
                    return user;
                }
        );
    }

    @Override
    public List<Task> findAll(User user) {
        return this.tx(
                session -> {
                    final Query query = session.createQuery(
                            "from ru.job4j.todo.model.Task where " + "user_id" + "= :value");
                    query.setParameter("value", user.getId());
                    return query.list();

                });
    }

    /*@Override
    public List<Task> findAll() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Task").list()
        );
    }*/

    @Override
    public List<Task> findByUndone(User user) {
        return this.tx(
                session -> {
                    final Query query = session.createQuery(
                            "from ru.job4j.todo.model.Task where "
                                    + "done" + "= :value1" + " and " + "user_id" + "= :value2" );
                    query.setParameter("value1", false);
                    query.setParameter("value2", user.getId());
                    return query.list();
                });
    }



    @Override
    public boolean done(Task task) {
        return this.tx(
                session -> {
                    try {
                        task.setDone(true);
                        session.update(task);
                    } catch (OptimisticLockException e) {
                        return false;
                    }
                    return true;
                });
    }

    @Override
    public User findByCredential(String email, String password) {
        return this.tx(
                session -> {
                    //Integer id = (Integer) Integer.parseInt(key);
                    Query queryObject = session.createQuery(
                            "from ru.job4j.todo.model.User where "
                                    + "email" + "= :value1" + " and " + "password" + "= :value2");
                    queryObject.setParameter("value1", email);
                    queryObject.setParameter("value2", password);
                    List<User> list = queryObject.list();
                     return list.isEmpty() ? null : list.get(0);                  
                });
    }

    @Override
    public Task findById(String key) {
        return this.tx(
                session -> {
                    Integer id = (Integer) Integer.parseInt(key);
                    Query queryObject = session.createQuery(
                            "from ru.job4j.todo.model.Task where " + "id" + "= :value");
                    queryObject.setParameter("value", id);
                    List<Task> list = queryObject.list();
                    return list.get(0);
                });
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
