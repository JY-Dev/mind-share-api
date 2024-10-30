package com.mindshare.api.application.auth.error

import com.mindshare.api.core.error.BusinessException
import com.mindshare.api.core.error.ErrorCode

class DuplicateLoginIdException(message: String) : BusinessException(message, ErrorCode.A01002) {
}