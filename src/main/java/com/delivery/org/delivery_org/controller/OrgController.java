package com.delivery.orgservice.controller;

import com.delivery.orgservice.service.OrgService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orgs")
public class OrgController {

    private final OrgService orgService;

    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping
    public Map<String, Object> createOrg(@RequestBody Map<String, String> req)
            throws Exception {
        return orgService.createOrg(req.get("name"));
    }

    @GetMapping
    public List<Map<String, Object>> getOrgs() throws Exception {
        return orgService.getAllOrgs();
    }

    // ✅ NEW
    @GetMapping("/{orgId}")
    public Map<String, Object> getOrgById(@PathVariable String orgId)
            throws Exception {
        return orgService.getOrgById(orgId);
    }
}
