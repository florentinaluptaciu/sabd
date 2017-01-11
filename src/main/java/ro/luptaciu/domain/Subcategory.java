package ro.luptaciu.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Subcategory.
 */
@Entity
@Table(name = "subcategory")
public class Subcategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "subcategory_name")
    private String subcategoryName;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    private Category category;


    public Subcategory(Long id) {
        this.id=id;
    }

    public Subcategory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public Subcategory subcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
        return this;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Subcategory isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Category getCategory() {
        return category;
    }

    public Subcategory category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Subcategory subcategory = (Subcategory) o;
        if(subcategory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, subcategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Subcategory{" +
            "id=" + id +
            ", subcategoryName='" + subcategoryName + "'" +
            ", isActive='" + isActive + "'" +
            '}';
    }
}
