package com.adoptasalvavidas.adoptasalvavidasBack.Models.DTO;

import jakarta.validation.constraints.NotBlank;

public record AuthValidateCodRequest(
    @NotBlank String email,
    @NotBlank String code
) {     
} 
    

