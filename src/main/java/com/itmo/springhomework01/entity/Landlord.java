package com.itmo.springhomework01.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "landlord")
public class Landlord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
