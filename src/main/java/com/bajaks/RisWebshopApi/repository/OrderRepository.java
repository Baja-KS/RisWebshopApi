package com.bajaks.RisWebshopApi.repository;

import com.bajaks.RisWebshopApi.model.Order;
import com.bajaks.RisWebshopApi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Transactional(readOnly = true)
    @Query("SELECT o FROM Order o WHERE o.address ilike %:address% " +
            "AND (:user IS NULL OR  o.user = :user) " +
            "AND (:from IS NULL OR o.timestamp >= :from) " +
            "AND (:to IS NULL OR o.timestamp <= :to) " +
            "AND (:minTotal IS NULL OR " +
            "(SELECT SUM (oi.quantity*oi.unitPrice) FROM OrderItem oi WHERE oi.order = o) >= :minTotal) " +
            "AND (:maxTotal IS NULL OR " +
            "(SELECT SUM (oi.quantity*oi.unitPrice) FROM OrderItem oi WHERE oi.order = o) <= :maxTotal) ")
    public Page<Order> search(@Param("from")Date from, @Param("to") Date to, @Param("address") String address,
                              @Param("minTotal") Float minTotal, @Param("maxTotal") Float maxTotal, @Param("user")User user, Pageable pageable);
}
