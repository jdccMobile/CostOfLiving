package com.jdccmobile.data.location

interface PermissionChecker {
    enum class Permission { COARSE_LOCATION }

    fun check(permission: Permission): Boolean
}
