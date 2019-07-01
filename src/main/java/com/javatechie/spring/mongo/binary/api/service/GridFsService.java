package com.javatechie.spring.mongo.binary.api.service;

import com.javatechie.spring.mongo.binary.api.domain.Interaction;
import com.javatechie.spring.mongo.binary.api.utils.PocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GridFsService {

    @Autowired
    private PocUtils pocUtils;

    public void indexInteractionData(Interaction interaction) {
        importInteractionFile(interaction);
    }

    private void importInteractionFile(Interaction interaction) {

        //import files
    }


}
