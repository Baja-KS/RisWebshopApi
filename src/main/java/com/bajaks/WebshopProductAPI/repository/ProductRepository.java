package com.bajaks.WebshopProductAPI.repository;

import com.bajaks.WebshopProductAPI.model.Category;
import com.bajaks.WebshopProductAPI.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product,Long> {
//    @Query("select p from Product p where (p.name ilike %?1% or p.specification ilike %?1%)")
    public Page<Product> findByNameContainingIgnoreCaseOrSpecificationContainingIgnoreCase(String search,String alsoSearch,Pageable pageable);
//    public Page<Product> findAllBy(SearchData data, Pageable pageable);
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Product p WHERE (p.name ilike %:search% OR p.specification ilike %:search%) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:category IS NULL OR p.category = :category)" +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)" +
            "AND (:stock IS NULL OR p.stock >= :stock)")
    public Page<Product> search(@Param("search") String search, @Param("category")Category category,
                                @Param("minPrice")Float minPrice,@Param("maxPrice")Float maxPrice,
                                @Param("stock")Integer stock,Pageable pageable);
}
