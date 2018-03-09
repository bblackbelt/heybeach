package com.blackbelt.heybeach.data

data class TaskDescriptor(val url: String, val requestMethod: RequestMethod = RequestMethod.GET, val body: String? = null)