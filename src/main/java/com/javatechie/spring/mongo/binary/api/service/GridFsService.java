package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import org.springframework.stereotype.Service;

@Service
public class GridFsService {


    public void indexInteractionData(Interaction interaction) {

        importInteractionFile(interaction);
    }

    private void importInteractionFile(Interaction interaction) {

        //import files
    }


}
