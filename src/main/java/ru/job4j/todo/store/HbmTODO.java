package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Task;

import javax.persistence.OptimisticLockException;
import java.util.List;

public class HbmTODO implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Task add(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    @Override
    public List<Task> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        try {
            List<Task> result = session.createQuery("from ru.job4j.todo.model.Task").list();
            session.getTransaction().commit();
            return result;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Task> findByUndone() {
        Session session = sf.openSession();
        try {
            String queryString = "from ru.job4j.todo.model.Task where " + "done" + "= :value";
            Query queryObject = session.createQuery(queryString);
            queryObject.setParameter("value", false);
            List<Task> list = queryObject.list();
            return list;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }

    }

    @Override
    public boolean done(Task task) {
        System.out.println("method done");
        task.setDone(true);
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
            session.close();
        } catch (OptimisticLockException e) {
            session.close();
            return false;
        }
        return true;
    }

    public Task findById(String key) {
        Integer id = (Integer) Integer.parseInt(key);
        System.out.println("id integer = " +  id);
        Session session = sf.openSession();
        try {
            String queryString = "from ru.job4j.todo.model.Task where " + "id" + "= :value";
            Query queryObject = session.createQuery(queryString);
            queryObject.setParameter("value", id);
            List<Task> list = queryObject.list();
            return list.get(0);
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    public static void main(String[] args) {
//        new HbmTracker().add(new Item("HB Item1"));
//        System.out.println(new HbmTracker().replace("1742442", new Item("HB Item1 Replace 2")));
//        System.out.println(new HbmTracker().delete("1742438"));
//        System.out.println(new HbmTracker().findAll());
//        System.out.println(new HbmTracker().findByName("HB Item1 Replace 2"));
//        System.out.println(new HbmTracker().findById("1742442"));
    }
}
