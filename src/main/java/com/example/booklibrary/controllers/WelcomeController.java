package com.example.booklibrary.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class WelcomeController {
  @GetMapping("/welcome")
  public ResponseEntity<Map<String, String>> welcome() {
    var response =
        Map.of(
            "message",
            "Welcome to the library!",
            "currentDate",
            new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()));
    return ResponseEntity.ok(response);
  }
}
