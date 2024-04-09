package com.br.lanchonete.lanchoneteapi.model;

import com.br.lanchonete.lanchoneteapi.model.abstracts.AbstractModel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AbstractModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Boolean active;
    private String tag;
    //todo implementar imagem para os produtos

}