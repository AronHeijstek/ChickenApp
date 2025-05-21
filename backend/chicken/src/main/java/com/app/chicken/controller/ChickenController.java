package com.app.chicken.controller;

import com.app.chicken.dto.ChickenDto;
import com.app.chicken.service.ChickenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chicken")
@RequiredArgsConstructor
@Tag(name = "Chicken", description = "Virtual chicken operations")
@SecurityRequirement(name = "bearerAuth")
public class ChickenController {
    
    private final ChickenService chickenService;
    
    @GetMapping
    @Operation(summary = "Get user's chicken", description = "Retrieves the virtual chicken for the authenticated user")
    public ResponseEntity<ChickenDto> getChicken(Authentication authentication) {
        return ResponseEntity.ok(chickenService.getChicken(authentication.getName()));
    }
    
    @PostMapping("/feed")
    @Operation(summary = "Feed the chicken", description = "Feeds the virtual chicken to gain experience")
    public ResponseEntity<ChickenDto> feedChicken(Authentication authentication) {
        return ResponseEntity.ok(chickenService.feedChicken(authentication.getName()));
    }
} 