package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mrkotuk.PersoNet.service.GoogleDriveService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/google-drive")
public class GoogleDriveController {
    private final GoogleDriveService service;

    @GetMapping("/upload")
    public ResponseEntity<List<String>> uploadPhotos(@RequestParam List<MultipartFile> files) {
        return new ResponseEntity<>(service.uploadFiles(files), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePhoto(@RequestParam String url) {
        return new ResponseEntity<>(service.deleteFileByUrl(url), HttpStatus.OK);
    }
}
