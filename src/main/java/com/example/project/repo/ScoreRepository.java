package com.example.project.repo;

import com.example.project.entity.Score;
import org.springframework.data.repository.CrudRepository;

public interface ScoreRepository extends CrudRepository<Score, Integer> {
}
