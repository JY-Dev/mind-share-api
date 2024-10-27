package com.mindshare.api.application.auth.error

import com.mindshare.api.core.error.BusinessException
import com.mindshare.api.core.error.ErrorCode

class DuplicateNicknameException(message : String) : BusinessException(message, ErrorCode.A01001) {
}