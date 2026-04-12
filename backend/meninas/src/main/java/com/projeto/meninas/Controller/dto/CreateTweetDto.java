package com.projeto.meninas.Controller.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTweetDto(@NotBlank String content) {
}
