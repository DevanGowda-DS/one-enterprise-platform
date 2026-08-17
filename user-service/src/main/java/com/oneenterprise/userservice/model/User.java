package com.oneenterprise.userservice.model;


public class User {

    private final Long id;
    private final String name;
    private final String email;
    private final String internalAuditFlag; // deliberately never exposed via the API

    public User(Long id, String name, String email, String internalAuditFlag) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.internalAuditFlag = internalAuditFlag;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getInternalAuditFlag() {
        return internalAuditFlag;
    }
}
