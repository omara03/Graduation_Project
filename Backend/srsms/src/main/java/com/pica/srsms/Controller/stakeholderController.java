package com.pica.srsms.Controller;

import com.pica.srsms.Entity.Stakeholder;
import com.pica.srsms.Service.StakeholderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/stakeholder")
public class stakeholderController {
    @Autowired
    private StakeholderService stakeholderService;

    @GetMapping("getallstakeholders")
    public List<Stakeholder> getallstakeholder() {
        return stakeholderService.getAllStakeholders();
    }

}