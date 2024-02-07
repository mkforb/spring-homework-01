package com.itmo.springhomework01.controller;

import com.itmo.springhomework01.entity.House;
import com.itmo.springhomework01.exception.ErrorException;
import com.itmo.springhomework01.service.HouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/house")
public class HouseController {
    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    @ResponseBody
    public List<House> list() {
        return houseService.list();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public House findById(@PathVariable long id) {
        try {
            return houseService.findById(id);
        } catch (ErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/page/{id}")
    public String pageFindById(@PathVariable long id, Model model) {
        try {
            House h = houseService.findById(id);
            model.addAttribute("house", h);
            return "house";
        } catch (ErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Void> add(@RequestBody @Valid House house) {
        long id = houseService.save(house);
        URI location = UriComponentsBuilder.fromPath("/house/{id}").build(id);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/add-image")
    @ResponseBody
    public ResponseEntity<Void> addImage(@PathVariable long id, @RequestPart MultipartFile image) {
        try {
            houseService.addImage(id, image);
            return ResponseEntity.accepted().build();
        } catch (ErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
