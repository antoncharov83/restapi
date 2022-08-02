package ru.antoncharov.restapi.model;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class GeologicalClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeologicalClass that = (GeologicalClass)o;

        if(id == null || !id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Optional.of(id).orElse(0L).intValue()*31 + Optional.of(name).orElse("").hashCode() + code.hashCode();
    }
}
