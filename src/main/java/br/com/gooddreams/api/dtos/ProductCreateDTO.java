package br.com.gooddreams.api.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductCreateDTO (String name,
                                String description,
                                BigDecimal price,
                                String category,
                                String imageUrl){ }
