package com.itmo.springhomework01.repository;

import com.itmo.springhomework01.entity.Landlord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandlordRepository extends JpaRepository<Landlord, Long> {

}
