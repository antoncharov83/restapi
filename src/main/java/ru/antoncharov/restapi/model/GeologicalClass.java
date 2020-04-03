package ru.antoncharov.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GeologicalClass {

    private long id;

    private String name;

    private String code;

    private List<Section> sections = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, insertable = true, updatable = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(unique = true)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "section_geological",
            joinColumns = @JoinColumn(name="geological_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id"))
    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeologicalClass that = (GeologicalClass)o;

        if(id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int)id;
        result = 31*result + name.hashCode() + code.hashCode();
        return result;
    }
}
