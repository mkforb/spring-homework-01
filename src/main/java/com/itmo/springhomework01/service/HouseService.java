package com.itmo.springhomework01.service;

import com.itmo.springhomework01.entity.House;
import com.itmo.springhomework01.exception.ErrorException;
import com.itmo.springhomework01.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

@Service
public class HouseService {
    public static final String DIR_UPLOAD = "upload/house";
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

    public long save(House house) {
        return houseRepository.save(house).getId();
    }

    public void addImage(long id, MultipartFile image) throws ErrorException {
        // Как я понял, по умоланию картинки берутся из папки src/main/resources/static, которая находится внутри приложения.
        // При запуске через jar-файл загруженные картинки будут недоступны.
        // Подсмотрел в Интернет, что можно перенаправить на папку вне приложения через WebMvcConfigurer
        House h = findById(id);
        if (image == null || image.isEmpty()) {
            throw new ErrorException("Image file is null or empty.");
        }
        if (image.getOriginalFilename() == null || image.getOriginalFilename().isEmpty()) {
            throw new ErrorException("File name is incorrect.");
        }
        // Get file extension
        String ext = "";
        int i = image.getOriginalFilename().lastIndexOf(".");
        if (i > 0) {
            ext = image.getOriginalFilename().substring(i+1);
        } else {
            throw new ErrorException("File name is incorrect.");
        }
        // Delete old file
        if (!h.getImagePath().isEmpty()) {
            File file = new File(getUploadPath(h.getImagePath()));
            boolean deleted = false;
            if (file.exists()) {
                deleted = file.delete();
            } else {
                deleted = true;
            }
            if (deleted) {
                h.setImagePath("");
                save(h);
            }
        }
        // Generate file name
        UUID uuid = UUID.randomUUID();
        String filename = uuid + "." + ext;
        // Save file
        File file = new File(getUploadPath(filename));
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(image.getBytes());
        } catch (IOException e) {
            throw new ErrorException(e.getMessage());
        }
        h.setImagePath(filename);
        save(h);
    }

    private String getUploadPath(String filename) throws ErrorException {
        if (filename == null || filename.isEmpty()) {
            throw new ErrorException("File name is null or empty.");
        }
        return DIR_UPLOAD + "/" + filename;
    }
}
