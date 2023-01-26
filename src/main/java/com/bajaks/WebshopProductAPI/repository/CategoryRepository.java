package com.bajaks.WebshopProductAPI.repository;

import com.bajaks.WebshopProductAPI.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
