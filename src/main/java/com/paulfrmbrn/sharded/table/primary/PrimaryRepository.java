package com.paulfrmbrn.sharded.table.primary;

import com.paulfrmbrn.sharded.table.JustModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrimaryRepository extends MongoRepository<JustModel, String> {
}
