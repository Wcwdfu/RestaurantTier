package com.site.restauranttier.service;

import com.site.restauranttier.entity.Feedback;
import com.site.restauranttier.entity.User;
import com.site.restauranttier.repository.FeedbackRepository;
import com.site.restauranttier.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final UserRepository userRepository;
    private final FeedbackRepository feedbackRepository;

    public String addFeedback(String feedbackBody, Principal principal) {
        Optional<User> userOptional = userRepository.findByUserTokenId(principal.getName());

        if (userOptional.isEmpty()) {
            return "fail";
        }

        Feedback newFeedback = new Feedback();
        newFeedback.setFeedbackContent(feedbackBody);
        newFeedback.setUser(userOptional.get());
        newFeedback.setCreatedAt(LocalDateTime.now());

        feedbackRepository.save(newFeedback);
        return "success";
    }
}
