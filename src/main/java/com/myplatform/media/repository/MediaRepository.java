package com.myplatform.media.repository;

import com.myplatform.media.entity.Media;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MediaRepository extends MongoRepository<Media, String> {
    Optional<Media> findByTitle(String title);
}
