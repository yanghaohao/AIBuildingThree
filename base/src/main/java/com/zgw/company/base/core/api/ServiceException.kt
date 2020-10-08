package com.zgw.company.base.core.api

import java.lang.RuntimeException

class ServiceException(var code : String,override var message : String?) : RuntimeException()