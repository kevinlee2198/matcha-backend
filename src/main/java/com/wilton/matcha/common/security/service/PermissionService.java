package com.wilton.matcha.common.security.service;

import com.wilton.matcha.common.security.service.dto.MatchaResourceAction;
import com.wilton.matcha.common.security.service.dto.PermissionActionsOnResource;
import com.wilton.matcha.common.security.service.dto.PermissionPrincipal;
import com.wilton.matcha.common.security.service.dto.PermissionResource;
import java.util.List;
import java.util.Map;

public interface PermissionService {

    /**
     * Check if a user (principal) has permission to perform actions on a resource
     *
     * @param permissionPrincipal the user performing the actions
     * @param actionsOnResource   the actions being performed on the resource
     * @return key is the action, value is true if accessible, false if not accessible
     */
    Map<MatchaResourceAction, Boolean> hasPermission(
            PermissionPrincipal permissionPrincipal, PermissionActionsOnResource actionsOnResource);

    /**
     * Check if a user (principal) has permission to perform actions on a resource
     *
     * @param permissionPrincipal the user performing the actions
     * @param actionsOnResources  the actions being performed on the resources
     * @return the key is the resource that is being acted upon (kind and id)
     *         the value is the map of the permissions - true if accessible, false if not accessible
     */
    Map<PermissionResource, Map<MatchaResourceAction, Boolean>> hasPermission(
            PermissionPrincipal permissionPrincipal, List<PermissionActionsOnResource> actionsOnResources);
}
