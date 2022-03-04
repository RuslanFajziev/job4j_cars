package hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.function.Function;

public class DbStore {
    private StandardServiceRegistry registry;
    private SessionFactory sf;

    public DbStore() {
        this.registry = new StandardServiceRegistryBuilder().configure().build();
        this.sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final DbStore INST = new DbStore();
    }

    public static DbStore instOf() {
        return Lazy.INST;
    }

    private <T> T tx(final Function<Session, T> command) {
        Session session = sf.openSession();
        session.beginTransaction();
        try {
            T rsl = command.apply(session);
            session.getTransaction().commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

//    public void add(String name, int experience, int salary) {
//        tx(session -> {
//                    session.createQuery("INSERT INTO Candidate (name, experience, salary) name = :nameKey, = :experiencekey, = :salaryKey")
//                            .setParameter("nameKey", name)
//                            .setParameter("experiencekey", experience)
//                            .setParameter("salaryKey", salary)
//                            .executeUpdate();
//                    return true;
//                }
//        );
//    }

    public Candidate save(Candidate candidate) {
        tx(session -> session.save(candidate));
        return candidate;
    }

        public Candidate select(String name) {
        Candidate cnd = tx(session -> {
                    return session.createQuery("FROM Candidate can WHERE can.name = :nameKey", Candidate.class)
                            .setParameter("nameKey", name).uniqueResult();
                }
        );
        return cnd;
    }

    public Candidate selectId(int id) {
        return tx(session -> session.createQuery("FROM Candidate can WHERE can.id = :idKey", Candidate.class)
                            .setParameter("idKey", id).uniqueResult());

    }

    public List<Candidate> selectAll() {
        return tx(session -> session.createQuery("FROM Candidate").list());
    }

    public void update(int id) {
        tx(session -> session.createQuery("UPDATE Candidate can SET can.salary = :salaryKey,"
                        + " can.experience = :experienceKey WHERE can.id = :idKey")
                .setParameter("salaryKey", 45)
                .setParameter("experienceKey", 3000000)
                .setParameter("idKey", id)
                .executeUpdate());
    }

    public void delete(int id) {
        tx(session -> session.createQuery("DELETE Candidate can WHERE can.id = :idKey")
                .setParameter("idKey", 1)
                .executeUpdate());
    }
}