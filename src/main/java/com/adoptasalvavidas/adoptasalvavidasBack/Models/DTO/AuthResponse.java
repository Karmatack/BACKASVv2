package com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"username", "message", "rol","status", "jwt"})
public record AuthResponse(
        String username,
        String message,
        String rol,
        String jwt,
        Boolean status) {
}