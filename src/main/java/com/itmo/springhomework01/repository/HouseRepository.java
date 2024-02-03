package com.itmo.springhomework01.repository;

import com.itmo.springhomework01.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}
