package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Contribution;
import tn.esprit.examenspring.services.IContributionService;

import java.util.List;

@RestController
@RequestMapping("/Contribution")

public class ContributionController {
    @Autowired
    private IContributionService contributionService;

    @GetMapping("/retrieve-all-contribution")
    public List<Contribution> getContribution() {
        return contributionService.getContributions();
    }

    @PostMapping("/add-contribution")
    public Contribution addContribution(@RequestBody Contribution e) {
        return contributionService.addContribution(e);
    }
    @DeleteMapping("/remove-contribution/{contribution-id}")
    public void removeContribution(@PathVariable("contribution-id") Integer eid) {
        contributionService.deleteContribution(eid);
    }
    @PutMapping("/modify-contribution")
    public Contribution modifyContribution(@RequestBody Contribution e) {
        return contributionService.modifyContribution(e);
    }


}
