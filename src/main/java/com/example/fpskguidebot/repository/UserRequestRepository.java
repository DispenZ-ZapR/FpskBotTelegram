package com.example.fpskguidebot.repository;

import com.example.fpskguidebot.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    List<UserRequest> findByStatusOrderByRequestDateDesc(String status);
}
