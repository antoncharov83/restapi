package ru.antoncharov.restapi.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Section {

    private Long id;

    private String name;

    private List<GeologicalClass> geologicalClasses = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "section_geological",
                joinColumns = @JoinColumn(name="section_id"),
                inverseJoinColumns = @JoinColumn(name = "geological_id"))
    public List<GeologicalClass> getGeologicalClasses() {
        return geologicalClasses;
    }

    public void setGeologicalClasses(List<GeologicalClass> geologicalClasses) {
        this.geologicalClasses = geologicalClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section that = (Section)o;

        if(id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id == null ? 0 : id.intValue();
        result = 31*result + name.hashCode();
        return result;
    }
}
