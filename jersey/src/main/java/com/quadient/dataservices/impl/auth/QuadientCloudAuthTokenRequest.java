package com.quadient.dataservices.impl.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

class QuadientCloudAuthTokenRequest {

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Password")
    private String password;

    public QuadientCloudAuthTokenRequest(String email, char[] password) {
        this.email = email;
        this.password = new String(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
