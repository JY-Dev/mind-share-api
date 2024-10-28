package com.mindshare.api.core.util

import org.slf4j.helpers.MessageFormatter

fun String.format(vararg objects: Any?): String {
    return MessageFormatter.arrayFormat(this, objects).message
}