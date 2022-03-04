package hql;

public class TestRun {
    public static void main(String[] args) {
        Candidate candidate = Candidate.of("Petr", 15, 150000);
        Candidate candidate2 = Candidate.of("Petr2", 16, 175000);
        Candidate candidate3 = Candidate.of("Petr3", 17, 200000);
        var db = DbStore.instOf();
        db.save(candidate);
        db.save(candidate2);
        db.save(candidate3);
        System.out.println(db.select("Petr"));
        System.out.println(db.selectId(2));
        db.selectAll().forEach(System.out::println);
        db.update(3);
        System.out.println(db.select("Petr3"));
        db.delete(1);
        db.selectAll().forEach(System.out::println);
    }
}