package com.example.mymoviememoir.entity;

public class Credential {
    private Integer credentialid;
    private String username;
    private String passwordhash;
    private Person personid;

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
        return personid;
    }

    public void setPerson(Person person) {
        this.personid = person;
    }

    public void setPersonID(int personID) {
        this.personid = new Person();
        personid.setPersonid(personID);
    }
}
