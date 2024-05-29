package com.example.booklibrary.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class WelcomeController {
  @GetMapping("/welcome")
  public Map<String, String> welcome() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Welcome to the library!");
    String currentDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
    response.put("currentDate", currentDate);
    return response;
  }
}
