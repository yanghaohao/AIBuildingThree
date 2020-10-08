package com.zgw.company.base.core.domain.request;


import java.io.Serializable


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
class TokenRequestParams(var activityId: String? = "",
                         var accountKey: String? = "",
                         var appKey: String? = "",
                         var externalUserId: String? = "",
                         var uatId: Long? = 0) : Serializable