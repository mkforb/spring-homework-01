package com.itmo.springhomework01.controller;

import com.itmo.springhomework01.entity.House;
import com.itmo.springhomework01.exception.ErrorException;
import com.itmo.springhomework01.service.HouseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

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

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Void> add(@RequestBody @Valid House house) {
        long id = houseService.save(house);
        URI location = UriComponentsBuilder.fromPath("/house/{id}").build(id);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/add-image")
    @ResponseBody
    public ResponseEntity<Void> addImage(HttpServletRequest request, @PathVariable long id, @RequestPart MultipartFile image) {
        // Get house
        House h = findById(id);
        if (!h.getImagePath().isEmpty()) {
            File file = new File(h.getImagePath());
            file.delete();
            houseService.save(h);
        }
        // Get file extension
        String ext = "";
        int i = image.getOriginalFilename().lastIndexOf(".");
        if (i > 0) {
            ext = image.getOriginalFilename().substring(i+1);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is incorrect.");
        }
        // Get directory
        // ToDo: Пока не придумал другого решения, как взять из request.getServletContext()
        // Но она при каждом запуске приложения разная
        String dirName = request.getServletContext().getRealPath("/uploads/house");
        //System.out.println(dirName);
        // Check directory
        File dir = new File(dirName);
        if (!dir.exists()){
            boolean dirCreated = dir.mkdirs();
            if (!dirCreated) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Directory cannot be created.");
            }
        }
        // Generate file name
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "." + ext;
        //System.out.println(fileName);
        // Save file
        File file = new File(dirName+"/"+fileName);
        try {
            image.transferTo(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        h.setImagePath(dirName+"/"+fileName);
        houseService.save(h);
        return ResponseEntity.accepted().build();
    }
}
