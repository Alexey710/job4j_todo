package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Task;
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
                    session.save(task);
                    return task;
                }
        );
    }

    @Override
    public List<Task> findAll() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Task").list()
        );
    }

    @Override
    public List<Task> findByUndone() {
        return this.tx(
                session -> {
                    final Query query = session.createQuery(
                            "from ru.job4j.todo.model.Task where " + "done" + "= :value");
                    query.setParameter("value", false);
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
