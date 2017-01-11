package ro.luptaciu.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Subcategory entity.
 */
public class SubcategoryModel {

    private Long id;
    private String subcategoryName;
    private Integer noOfQues;

    public SubcategoryModel(Long id, String subcategoryName, Integer noOfQues) {
        this.id = id;
        this.subcategoryName = subcategoryName;
        this.noOfQues = noOfQues;
    }

    public SubcategoryModel() {
    }


    public Integer getNoOfQues() {
        return noOfQues;
    }

    public void setNoOfQues(Integer noOfQues) {
        this.noOfQues = noOfQues;
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

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    @Override
    public String toString() {
        return "SubcategoryModel{" +
            "id=" + id +
            ", subcategoryName='" + subcategoryName + '\'' +
            ", noOfQues=" + noOfQues +
            '}';
    }
}
