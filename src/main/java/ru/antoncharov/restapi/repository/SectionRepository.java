package ru.antoncharov.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antoncharov.restapi.model.Section;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByGeologicalClasses_codeLikeIgnoreCase(String code);
}
