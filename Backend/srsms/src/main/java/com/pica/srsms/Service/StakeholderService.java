package com.pica.srsms.Service;

import com.pica.srsms.Entity.Stakeholder;
import com.pica.srsms.Repository.StakeholderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StakeholderService {


    @Autowired
    private StakeholderRepository stakeholderRepository;

    public List<Stakeholder> getAllStakeholders() {
        return stakeholderRepository.findAll();
    }

}
