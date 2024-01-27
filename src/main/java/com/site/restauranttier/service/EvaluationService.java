package com.site.restauranttier.service;

import com.site.restauranttier.entity.*;
import com.site.restauranttier.repository.EvaluationItemScoreRepository;
import com.site.restauranttier.repository.EvaluationRepository;
import com.site.restauranttier.repository.SituationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final SituationRepository situationRepository;
    private final EvaluationItemScoreRepository evaluationItemScoreRepository;
    public Evaluation getByUserAndRestaurant(User user, Restaurant restaurant) {
        Optional<Evaluation> evaluation = evaluationRepository.findByUserAndRestaurant(user, restaurant);
        return evaluation.orElse(null);
    }

    public void deleteItemScoresAll(Evaluation evaluation) {
        List<EvaluationItemScore> evaluationItemScoreList = evaluation.getEvaluationItemScoreList();
        for (EvaluationItemScore itemScore : evaluationItemScoreList) {
            Situation situation = itemScore.getSituation();
            situation.getEvaluationItemScoreList().remove(itemScore);
            situationRepository.save(situation);
            evaluationItemScoreRepository.delete(itemScore);
        }
        evaluation.getEvaluationItemScoreList().clear();

    }
}
