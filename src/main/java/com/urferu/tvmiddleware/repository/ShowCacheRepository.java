package com.urferu.tvmiddleware.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.urferu.tvmiddleware.model.ShowCache;

public interface ShowCacheRepository extends MongoRepository<ShowCache, Long> {
}
