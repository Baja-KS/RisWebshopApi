package com.bajaks.RisWebshopApi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false)
    @Min(value = 0,message = "Price cannot be less than 0")
    private Float price;

    private String specification;
    private String img;

    @Min(value = 0,message = "Discount cannot be less than 0%")
    @ColumnDefault("0")
    private Float discount;


    @Min(value = 0,message = "Discount cannot be less than 0%")
    @ColumnDefault("0")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id",nullable = false)
    @JsonManagedReference
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<Review> reviews;
}
