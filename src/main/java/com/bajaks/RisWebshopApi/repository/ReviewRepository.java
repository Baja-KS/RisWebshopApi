package com.bajaks.RisWebshopApi.repository;

import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    public Page<Review> findByProduct(Product product, Pageable pageable);
    public List<Review> findByProduct(Product product);
}
