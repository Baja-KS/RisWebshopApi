package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.MessageResponse;
import com.bajaks.RisWebshopApi.dto.ReviewRequest;
import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.Review;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public MessageResponse add(Product product, User user, ReviewRequest request) {
        Review review = Review.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .product(product)
                .user(user)
                .build();
        reviewRepository.save(review);
        return new MessageResponse("Review successfully added");
    }

    public Page<Review> forProduct(Product product,Integer page,Integer perPage) {
        Pageable pageable = PageRequest.of(page,perPage);
        return reviewRepository.findByProduct(product,pageable);
    }
    public Page<Review> forProduct(Product product){
        return forProduct(product,0,5);
    }
    public Page<Review> forProduct(Product product,Integer page) {
        return forProduct(product,page,5);
    }
}
