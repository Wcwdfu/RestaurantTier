package com.site.restauranttier.service;

import com.site.restauranttier.DataNotFoundException;
import com.site.restauranttier.entity.Situation;
import com.site.restauranttier.repository.SituationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SituationService {

    private final SituationRepository situationRepository;

    public Situation getSituation(Integer id){
        Optional<Situation> situation = situationRepository.findById(id);
        if(situation.isPresent()){
            return situation.get();
        }
        else{
            throw  new DataNotFoundException("Situation not found");
        }
    }
}
