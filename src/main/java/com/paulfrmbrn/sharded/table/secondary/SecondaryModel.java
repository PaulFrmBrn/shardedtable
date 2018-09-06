package com.paulfrmbrn.sharded.table.secondary;

import com.paulfrmbrn.sharded.table.common.JustModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "second_mongo")
public class SecondaryModel extends JustModel {

    public SecondaryModel(String id, String value) {
        super(id, value);
    }
}
