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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        // Как я понял, по умоланию картинки берутся из папки src/main/resources/static, которая находится внутри приложения.
        // При запуске через jar-файл загруженные картинки будут недоступны.
        // Подсмотрел в Интернет, что можно перенаправить на папку вне приложения через WebMvcConfigurer
        // ToDo: Перенести сохранение файла в сервис
        // Get directory
        String dirName = "upload/house";
        // Get house
        House h = findById(id);
        if (!h.getImagePath().isEmpty()) {
            File file = new File(dirName+"/"+h.getImagePath());
            file.delete();
            h.setImagePath("");
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
        // Generate file name
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "." + ext;
        //System.out.println(fileName);
        // Save file
        File file = new File(dirName+"/"+fileName);
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(image.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        h.setImagePath(fileName);
        houseService.save(h);
        return ResponseEntity.accepted().build();
    }
}
