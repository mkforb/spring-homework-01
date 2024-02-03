package com.itmo.springhomework01.controller;

import com.itmo.springhomework01.entity.Landlord;
import com.itmo.springhomework01.exception.ErrorException;
import com.itmo.springhomework01.service.LandlordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/landlord")
public class LandlordController {
    private final LandlordService landlordService;

    @Autowired
    public LandlordController(LandlordService landlordService) {
        this.landlordService = landlordService;
    }

    @GetMapping
    @ResponseBody
    public List<Landlord> list() {
        return landlordService.list();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Landlord findById(@PathVariable long id) {
        try {
            return landlordService.findById(id);
        } catch (ErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Void> add(@RequestBody @Valid Landlord landlord) {
        long id = landlordService.save(landlord);
        URI location = UriComponentsBuilder.fromPath("/landlord/{id}").build(id);
        return ResponseEntity.created(location).build();
    }
}
