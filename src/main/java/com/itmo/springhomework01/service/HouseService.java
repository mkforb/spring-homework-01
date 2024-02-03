package com.itmo.springhomework01.service;

import com.itmo.springhomework01.entity.House;
import com.itmo.springhomework01.exception.ErrorException;
import com.itmo.springhomework01.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HouseService {
    private final HouseRepository houseRepository;

    @Autowired
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> list() {
        return houseRepository.findAll();
    }

    public House findById(long id) throws ErrorException {
        return houseRepository.findById(id).orElseThrow(() -> new ErrorException("House with id = " + id + " not found."));
    }

    public Long save(House house) {
        return houseRepository.save(house).getId();
    }
}
