package com.paulfrmbrn.sharded.table;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "just_mongo")
public class JustModel {

    @Id
    private String id;

    private String value;

    public JustModel() {
    }

    public JustModel(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "JustModel{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';

    }
}
