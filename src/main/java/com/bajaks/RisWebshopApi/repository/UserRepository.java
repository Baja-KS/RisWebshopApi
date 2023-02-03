package com.bajaks.RisWebshopApi.repository;

import com.bajaks.RisWebshopApi.model.Role;
import com.bajaks.RisWebshopApi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String username);

    @Transactional(readOnly = true)
    @Query("SELECT u FROM User u WHERE (u.username ilike %:search% OR u.firstName ilike %:search% OR u.lastName ilike %:search%) " +
            "AND (:role IS NULL OR u.role = :role)")
    public Page<User> search(@Param("search")String search, @Param("role")Role role, Pageable pageable);
}
