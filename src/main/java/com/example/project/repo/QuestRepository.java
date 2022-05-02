package com.example.project.repo;

import com.example.project.entity.Quest;
import org.springframework.data.repository.CrudRepository;

public interface QuestRepository extends CrudRepository<Quest,Integer> {
}
