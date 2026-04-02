package com.example.fpskguidebot.service;

import com.example.fpskguidebot.model.UserState;
import com.example.fpskguidebot.repository.UserStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserStateService {

    private final UserStateRepository userStateRepository;

    public static final String STATE_WAITING_FOR_REQUEST = "WAITING_FOR_REQUEST";

    @Transactional
    public void setUserState(Long chatId, String state) {
        UserState userState = userStateRepository.findByChatId(chatId)
                .orElse(new UserState());
        userState.setChatId(chatId);
        userState.setState(state);
        userStateRepository.save(userState);
        log.debug("Set state {} for user {}", state, chatId);
    }

    @Transactional(readOnly = true)
    public Optional<String> getUserState(Long chatId) {
        return userStateRepository.findByChatId(chatId)
                .map(UserState::getState);
    }

    @Transactional
    public void clearUserState(Long chatId) {
        userStateRepository.deleteByChatId(chatId);
        log.debug("Cleared state for user {}", chatId);
    }

    @Transactional(readOnly = true)
    public boolean isUserWaitingForRequest(Long chatId) {
        return getUserState(chatId)
                .map(STATE_WAITING_FOR_REQUEST::equals)
                .orElse(false);
    }
}
