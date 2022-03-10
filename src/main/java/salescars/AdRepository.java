package salescars;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class AdRepository {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Session session = sf.openSession();
        try {
            session.beginTransaction();

            var lst = session.createQuery(
                            "select distinct an from Announcement an "
                                    + "join fetch an.car cr "
                                    + "where an.created >= current_date at time zone 'UTC' - interval '1 days'").list();
            lst.forEach(System.out::println);

            var lst2 = session.createQuery(
                            "select distinct an from Announcement an "
                                    + "join fetch an.car cr "
                                    + "where cr.photo != ''").list();
            lst2.forEach(System.out::println);

            var lst3 = session.createQuery(
                    "select distinct an from Announcement an "
                            + "join fetch an.car cr "
                            + "where cr.model = :model")
                    .setParameter("model", "BMW")
                    .list();
            lst3.forEach(System.out::println);

             session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}