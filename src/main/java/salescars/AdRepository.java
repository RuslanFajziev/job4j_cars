package salescars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.function.Function;

public class AdRepository {
    private StandardServiceRegistry registry;
    private SessionFactory sf;

    public AdRepository() {
        this.registry = new StandardServiceRegistryBuilder().configure().build();
        this.sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final AdRepository INST = new AdRepository();
    }

    public static AdRepository instOf() {
        return AdRepository.Lazy.INST;
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

    public List<Announcement> selectDay() {
        return tx(session -> {
                    return session.createQuery("select distinct an from Announcement an "
                    + "join fetch an.car cr "
                    + "where an.created >= current_date at time zone 'UTC' - interval '1 days'", Announcement.class).list();
                }
        );
    }

    public List<Announcement> selectFoto() {
        return tx(session -> {
                    return session.createQuery("select distinct an from Announcement an "
                    + "join fetch an.car cr "
                    + "where cr.photo != ''", Announcement.class).list();
                }
        );
    }

    public List<Announcement> selectModel() {
        return tx(session -> {
                    return session.createQuery("select distinct an from Announcement an "
                            + "join fetch an.car cr "
                            + "where cr.model = :model", Announcement.class)
                            .setParameter("model", "BMW")
                            .list();
                }
        );
    }

    public static void main(String[] args) {
        AdRepository adRepository = AdRepository.instOf();
        adRepository.selectDay().forEach(System.out::println);
        adRepository.selectFoto().forEach(System.out::println);
        adRepository.selectModel().forEach(System.out::println);
    }
}