package com.app.quantitymeasurement.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique=true,nullable=false)
    private String email;

    private String provider; // LOCAL or GOOGLE

    public User(){}

    public User(String name,String email,String provider){
        this.name=name;
        this.email=email;
        this.provider=provider;
    }

    public Long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }


    public String getProvider(){
        return provider;
    }

    public void setProvider(String provider){
        this.provider=provider;
    }
}
