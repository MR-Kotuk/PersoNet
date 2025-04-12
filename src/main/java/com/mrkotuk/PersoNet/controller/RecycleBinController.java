package com.mrkotuk.PersoNet.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.domain.model.Person;
import com.mrkotuk.PersoNet.service.RecycleBinService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/recycle-bin")
@AllArgsConstructor
public class RecycleBinController {
    private final RecycleBinService recycleBinService;

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPersons() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity<>(recycleBinService.getPersons(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<String> returnFromRecycleBin(@RequestBody List<Integer> id) {
        recycleBinService.returnFromRecycleBin(id);
        return new ResponseEntity<>("Persons returned from the recycle bin successfully", HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> removeFromRecycleBin(@RequestBody List<Integer> id) {
        recycleBinService.removeFromRecycleBin(id);
        return new ResponseEntity<>("Persons removed from the recycle bin successfully", HttpStatus.OK);
    }

    @DeleteMapping("/clean")
    public ResponseEntity<String> cleanRecycleBin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        recycleBinService.cleanRecycleBin(authentication.getName());
        return new ResponseEntity<>("Recycle bin cleaned successfully", HttpStatus.OK);
    }
}
