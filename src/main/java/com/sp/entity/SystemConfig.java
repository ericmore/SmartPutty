package com.sp.entity;

import javax.persistence.*;

@Entity
public class SystemConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String key;
    private String value;

    public SystemConfig(Long id, String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public SystemConfig(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SystemConfig() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
