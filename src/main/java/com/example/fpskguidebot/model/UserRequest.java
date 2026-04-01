package com.example.fpskguidebot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_requests")
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Size(max = 255)
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "message_text", nullable = false, length = Integer.MAX_VALUE)
    private String messageText;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Size(max = 20)
    @ColumnDefault("'PENDING'")
    @Column(name = "status", length = 20)
    private String status;

    @Size(max = 10)
    @Column(name = "language", length = 10)
    private String language;

    @Column(name = "operator_response", length = Integer.MAX_VALUE)
    private String operatorResponse;

    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Size(max = 255)
    @Column(name = "operator_name")
    private String operatorName;

}