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
    public Ressource modifyRessource(Integer id, Ressource ressource) {
        Ressource existingRessource = ressourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ressource not found with id: " + id));

        existingRessource.setTitle(ressource.getTitle());
        existingRessource.setType(ressource.getType());
        existingRessource.setDescription(ressource.getDescription());
        existingRessource.setFileUrl(ressource.getFileUrl());
        existingRessource.setFormation(ressource.getFormation());

        return ressourceRepository.save(existingRessource);
    }

    @Override
    public void deleteRessource(Integer id) {
        ressourceRepository.deleteById(id);


    }

    @Override
    public List<Ressource> getRessourcesByFormation(Integer formationId) {
        return ressourceRepository.findByFormationId(formationId);
    }
}
