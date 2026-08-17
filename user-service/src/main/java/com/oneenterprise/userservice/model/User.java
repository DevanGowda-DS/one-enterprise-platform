package com.oneenterprise.userservice.model;

/**
 * Internal representation of a user.
 *
 * Day 2 lesson: this internal model is intentionally NOT returned directly
 * from the API. The controller maps it to {@link com.oneenterprise.userservice.dto.UserResponse}
 * so that internal fields (e.g. createdBy, internalFlag) never leak into the public contract.
 */
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
