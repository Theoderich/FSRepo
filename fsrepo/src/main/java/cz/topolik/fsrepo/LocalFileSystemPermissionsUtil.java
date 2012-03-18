/**
 * Copyright (c) 2012 Tomáš Polešovský
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package cz.topolik.fsrepo;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.expando.model.ExpandoColumn;

/**
 *
 * @author Tomas Polesovsky
 */
public class LocalFileSystemPermissionsUtil {

    public static PermissionChecker getPermissionChecker() {
        return PermissionThreadLocal.getPermissionChecker();
    }

    public static void checkFolder(long groupId, long folderId, String actionId) throws PrincipalException {
        if (!containsFolder(groupId, folderId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static void checkFileEntry(long groupId, long fileEntryId, String actionId) throws PrincipalException {
        if (!containsFileEntry(groupId, fileEntryId, actionId)) {
            throw new PrincipalException();
        }
    }

    public static boolean containsFolder(long groupId, long folderId, String actionId) {
        return getPermissionChecker().hasPermission(groupId, DLFolder.class.getName(), folderId, actionId);
    }

    public static boolean containsFileEntry(long groupId, long fileEntryId, String actionId) {
        return getPermissionChecker().hasPermission(groupId, DLFileEntry.class.getName(), fileEntryId, actionId);
    }

    public static void initExpandoColumnPermissions(long companyId, ExpandoColumn col) throws PortalException, SystemException {
        // add default permissions on the expando attribute (user+guest: rw)
        ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, ExpandoColumn.class.getName(), ResourceConstants.SCOPE_COMPANY, String.valueOf(companyId),
                RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST).getRoleId(), ActionKeys.VIEW);
        ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, ExpandoColumn.class.getName(), ResourceConstants.SCOPE_COMPANY, String.valueOf(companyId),
                RoleLocalServiceUtil.getRole(companyId, RoleConstants.GUEST).getRoleId(), ActionKeys.UPDATE);
        ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, ExpandoColumn.class.getName(), ResourceConstants.SCOPE_COMPANY, String.valueOf(companyId),
                RoleLocalServiceUtil.getRole(companyId, RoleConstants.USER).getRoleId(), ActionKeys.VIEW);
        ResourcePermissionLocalServiceUtil.addResourcePermission(companyId, ExpandoColumn.class.getName(), ResourceConstants.SCOPE_COMPANY, String.valueOf(companyId),
                RoleLocalServiceUtil.getRole(companyId, RoleConstants.USER).getRoleId(), ActionKeys.UPDATE);

    }
}