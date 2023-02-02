package com.bajaks.RisWebshopApi.repository;

import com.bajaks.RisWebshopApi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
