package com.paulfrmbrn.sharded.table.primary;

import com.paulfrmbrn.sharded.table.JustModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "first_mongo")
public class PrimaryModel extends JustModel {
    public PrimaryModel(String id, String value) {
        super(id, value);
    }
}