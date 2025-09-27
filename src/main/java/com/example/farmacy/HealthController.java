package com.example.farmacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class HealthController {

    @Value("${spring.application.name:farmacy}")
    private String applicationName;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "¡Hola! El servidor está funcionando correctamente");
        response.put("application", applicationName);
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        response.put("status", "UP");

        return ResponseEntity.ok(response);
    }
}