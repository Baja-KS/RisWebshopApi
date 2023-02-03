package com.bajaks.RisWebshopApi.service;

import com.bajaks.RisWebshopApi.dto.MessageResponse;
import com.bajaks.RisWebshopApi.dto.ReviewReportItem;
import com.bajaks.RisWebshopApi.dto.ReviewRequest;
import com.bajaks.RisWebshopApi.model.Product;
import com.bajaks.RisWebshopApi.model.Review;
import com.bajaks.RisWebshopApi.model.User;
import com.bajaks.RisWebshopApi.repository.ReviewRepository;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MinioService minioService;

    public List<Review> forReport(Product product){
        return reviewRepository.findByProduct(product);
    }

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

    //Review report for a product
    public JasperPrint reviewReport(Product product){
        List<ReviewReportItem> items = forReport(product).stream().map(review -> ReviewReportItem.builder()
                .username(review.getUser().getUsername())
                .comment(review.getComment())
                .rating(review.getRating())
                .build()).toList();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(items);
        try {
            InputStream template = minioService.get("jasper-templates/review-report-template.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(template);
            template.close();
            Map<String, Object> params = new HashMap<>();
            params.put("product",product.getName());
            return JasperFillManager.fillReport(jasperReport,params,dataSource);
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException | JRException e) {
            throw new RuntimeException(e);
        }
    }
}
