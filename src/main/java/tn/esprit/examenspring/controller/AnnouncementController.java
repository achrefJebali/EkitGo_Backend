package tn.esprit.examenspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Announcement;
import tn.esprit.examenspring.services.AnnouncementService;

import java.util.List;

@RestController
    @RequestMapping("/announcement")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/retrieve-all")
    public List<Announcement> getAnnouncements() {
        return announcementService.getAnnouncements();
    }

    @PostMapping("/add")
    public Announcement addAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.addAnnouncement(announcement);
    }

    @DeleteMapping("/remove/{announcement-id}")
    public void removeAnnouncement(@PathVariable("announcement-id") Integer id) {
        announcementService.deleteAnnouncement(id);
    }

    @PutMapping("/modify")
    public Announcement modifyAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.modifyAnnouncement(announcement);
    }
}
