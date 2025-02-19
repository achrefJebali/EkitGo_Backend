package tn.esprit.examenspring.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.RessourceRepository;
import tn.esprit.examenspring.entities.Ressource;

import java.util.List;

@Service
public class RessourceServiceImpl implements IRessourceService{
    @Autowired
    private RessourceRepository ressourceRepository;

    @Override
    public Ressource addRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @Override
    public List<Ressource> getRessources() {
        return ressourceRepository.findAll();
    }

    @Override
    public Ressource modifyRessource(Ressource ressource) {
        ressourceRepository.save(ressource);
        return ressource;
    }

    @Override
    public void deleteRessource(Integer id) {
        ressourceRepository.deleteById(id);


    }
}
