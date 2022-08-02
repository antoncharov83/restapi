package ru.antoncharov.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antoncharov.restapi.model.GeologicalClass;

@Repository
public interface GeologicalClassRepository extends JpaRepository<GeologicalClass, Long> {
}
