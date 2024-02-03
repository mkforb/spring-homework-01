package com.itmo.springhomework01.service;

import com.itmo.springhomework01.entity.Landlord;
import com.itmo.springhomework01.exception.ErrorException;
import com.itmo.springhomework01.repository.LandlordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LandlordService {
    private final LandlordRepository landlordRepository;

    @Autowired
    public LandlordService(LandlordRepository landlordRepository) {
        this.landlordRepository = landlordRepository;
    }

    public List<Landlord> list() {
        return landlordRepository.findAll();
    }

    public Landlord findById(long id) throws ErrorException {
        return landlordRepository.findById(id).orElseThrow(() -> new ErrorException("Landlord with id = " + id + " not found."));
    }

    public Long save(Landlord landlord) {
        return landlordRepository.save(landlord).getId();
    }
}
