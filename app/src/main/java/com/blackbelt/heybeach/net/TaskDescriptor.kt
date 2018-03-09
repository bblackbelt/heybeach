package com.blackbelt.heybeach.net

data class TaskDescriptor(val url: String, val requestMethod: RequestMethod = RequestMethod.GET, val body: String? = null)