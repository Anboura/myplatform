package com.myplatform.media.repository;

import com.myplatform.media.entity.MediaType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaTypeRepository extends MongoRepository<MediaType, String> {
    Optional<MediaType> findByType(String type);
}
