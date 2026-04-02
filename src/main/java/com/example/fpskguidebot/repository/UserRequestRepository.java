package com.example.fpskguidebot.repository;

import com.example.fpskguidebot.model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    List<UserRequest> findByStatusOrderByRequestDateDesc(String status);
    
    @Query("SELECT COUNT(r) FROM UserRequest r WHERE r.chatId = :chatId AND r.requestDate > :since")
    long countByChatIdAndRequestDateAfter(@Param("chatId") Long chatId, @Param("since") LocalDateTime since);
}
