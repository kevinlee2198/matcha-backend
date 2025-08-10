package com.wilton.matcha.common.security.service.dto;

import java.util.Set;

public class PermissionActionsOnResource {
    private final PermissionResource resource;
    private final Set<MatchaResourceAction> resourceActions;

    public PermissionActionsOnResource(PermissionResource resource, Set<MatchaResourceAction> resourceActions) {
        this.resource = resource;
        this.resourceActions = resourceActions;
    }

    public PermissionActionsOnResource(PermissionResource resource, MatchaResourceAction... resourceActions) {
        this.resource = resource;
        this.resourceActions = Set.of(resourceActions);
    }

    public PermissionResource getResource() {
        return resource;
    }

    public Set<MatchaResourceAction> getResourceActions() {
        return resourceActions;
    }

    public String[] getResourceActionsAsStrings() {
        return resourceActions.stream().map(MatchaResourceAction::toString).toArray(String[]::new);
    }
}
