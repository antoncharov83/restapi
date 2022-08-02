package ru.antoncharov.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.antoncharov.restapi.model.JobInfo;

import java.util.UUID;

@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo, UUID> {
}
