package salescars;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Announcements")
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @OneToOne
    @JoinColumn(name = "car_id")
    private Car car;

    private boolean sold;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return "Announcement{"
                + "id=" + id
                + ", description='" + description + '\''
                + ", car=" + car
                + ", sold=" + sold
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Announcement that = (Announcement) o;
        return id == that.id && sold == that.sold && Objects.equals(description, that.description)
                && Objects.equals(car, that.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, car, sold);
    }
}