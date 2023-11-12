package mp.sitili.modules.role.adapters;

import mp.sitili.modules.role.entities.Role;
import mp.sitili.modules.role.use_cases.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('Admin')")
    @PostMapping({"/createNewRole"})
    public Role createNewRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }
}
