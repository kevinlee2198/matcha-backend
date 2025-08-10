package com.wilton.matcha.common.security.service;

import com.wilton.matcha.common.security.service.dto.MatchaResourceAction;
import com.wilton.matcha.common.security.service.dto.PermissionActionsOnResource;
import com.wilton.matcha.common.security.service.dto.PermissionPrincipal;
import com.wilton.matcha.common.security.service.dto.PermissionResource;
import com.wilton.matcha.common.util.StreamUtil;
import dev.cerbos.sdk.CerbosBlockingClient;
import dev.cerbos.sdk.CheckResourcesRequestBuilder;
import dev.cerbos.sdk.builders.Principal;
import dev.cerbos.sdk.builders.Resource;
import dev.cerbos.sdk.builders.ResourceAction;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PermissionServiceImpl implements PermissionService {
    private final CerbosBlockingClient cerbosBlockingClient;

    @Autowired
    public PermissionServiceImpl(CerbosBlockingClient cerbosBlockingClient) {
        this.cerbosBlockingClient = cerbosBlockingClient;
    }

    @Override
    public Map<MatchaResourceAction, Boolean> hasPermission(
            PermissionPrincipal permissionPrincipal, PermissionActionsOnResource actionsOnResource) {
        Principal principal = initPrincipal(permissionPrincipal);
        permissionPrincipal
                .getAttributes()
                .forEach((key, value) -> principal.withAttribute(key, value.getAttributeValue()));

        Resource resource = initResource(actionsOnResource);
        actionsOnResource
                .getResource()
                .getAttributes()
                .forEach((key, value) -> resource.withAttribute(key, value.getAttributeValue()));

        Map<String, Boolean> resultMap = cerbosBlockingClient
                .check(principal, resource, actionsOnResource.getResourceActionsAsStrings())
                .getAll();
        return convertResultMap(resultMap);
    }

    @Override
    public Map<PermissionResource, Map<MatchaResourceAction, Boolean>> hasPermission(
            PermissionPrincipal permissionPrincipal, List<PermissionActionsOnResource> actionsOnResources) {
        Principal principal = initPrincipal(permissionPrincipal);

        CheckResourcesRequestBuilder requestBuilder = cerbosBlockingClient.batch(principal);
        ResourceAction[] resourceResourceActions = actionsOnResources.stream()
                .map(this::convertActionOnResourceToResourceAction)
                .toArray(ResourceAction[]::new);
        requestBuilder.addResources(resourceResourceActions);

        return StreamUtil.zip(
                        actionsOnResources.stream(), requestBuilder.check().results())
                .map(pair -> Pair.of(
                        pair.getFirst().getResource(),
                        convertResultMap(pair.getSecond().getAll())))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    private Principal initPrincipal(PermissionPrincipal permissionPrincipal) {
        return Principal.newInstance(
                permissionPrincipal.getId(), permissionPrincipal.getRoles().toArray(String[]::new));
    }

    private Resource initResource(PermissionActionsOnResource actionsOnResource) {
        return StringUtils.hasText(actionsOnResource.getResource().getId())
                ? Resource.newInstance(
                        actionsOnResource.getResource().getKind(),
                        actionsOnResource.getResource().getId())
                : Resource.newInstance(actionsOnResource.getResource().getKind());
    }

    private ResourceAction initResourceAction(PermissionActionsOnResource actionsOnResource) {
        return StringUtils.hasText(actionsOnResource.getResource().getId())
                ? ResourceAction.newInstance(
                        actionsOnResource.getResource().getKind(),
                        actionsOnResource.getResource().getId())
                : ResourceAction.newInstance(actionsOnResource.getResource().getKind());
    }

    private ResourceAction convertActionOnResourceToResourceAction(PermissionActionsOnResource actionsOnResource) {
        ResourceAction resourceAction =
                initResourceAction(actionsOnResource).withActions(actionsOnResource.getResourceActionsAsStrings());
        actionsOnResource
                .getResource()
                .getAttributes()
                .forEach((key, value) -> resourceAction.withAttribute(key, value.getAttributeValue()));
        return resourceAction;
    }

    private Map<MatchaResourceAction, Boolean> convertResultMap(Map<String, Boolean> resultMap) {
        return resultMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> MatchaResourceAction.fromStringIgnoreCase(entry.getKey()), Map.Entry::getValue));
    }
}
