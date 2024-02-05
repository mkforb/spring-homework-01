package com.itmo.springhomework01.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "house")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @NotNull
    @Size(min = 3, max = 100)
    private String address;
    @Column(nullable = false)
    @NotNull
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private boolean archived;
    @Column(nullable = false)
    private String imagePath;
    @ManyToOne
    @JoinColumn(nullable = false, name = "landlord_id")
    private Landlord landlord;

    public long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Landlord getLandlord() {
        return landlord;
    }
}
