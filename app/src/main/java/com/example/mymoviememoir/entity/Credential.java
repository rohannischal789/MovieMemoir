package com.example.mymoviememoir.entity;

public class Credential {
    private Integer credentialid;
    private String username;
    private String passwordhash;
    private Person person;

    public Credential(Integer credentialid, String username, String passwordhash) {
        this.credentialid = credentialid;
        this.username = username;
        this.passwordhash = passwordhash;
    }

    public Integer getCredentialid() {
        return credentialid;
    }

    public void setCredentialid(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
