package api.repository;


import api.domain.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

        List<Interaction> findAll();
}
