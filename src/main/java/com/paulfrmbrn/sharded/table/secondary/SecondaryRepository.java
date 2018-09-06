package com.paulfrmbrn.sharded.table.secondary;

import com.paulfrmbrn.sharded.table.JustModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecondaryRepository extends MongoRepository<JustModel, String> {
}
