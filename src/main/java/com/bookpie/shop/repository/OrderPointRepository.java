package com.bookpie.shop.repository;

import com.bookpie.shop.domain.OrderPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPointRepository extends JpaRepository<OrderPoint, Long> {
}
