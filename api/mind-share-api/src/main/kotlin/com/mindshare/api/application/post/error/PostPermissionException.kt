package com.mindshare.api.application.post.error

import com.mindshare.api.core.error.BusinessAuthorizationException
import com.mindshare.api.core.error.ErrorCode

class PostPermissionException(message: String) : BusinessAuthorizationException(message, ErrorCode.DEFAULT) {
}