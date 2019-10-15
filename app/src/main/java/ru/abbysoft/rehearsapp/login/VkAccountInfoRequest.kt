package ru.abbysoft.rehearsapp.login

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

/**
 * Get account info from vk sdk api
 *
 * @author apopov
 */
class VkAccountInfoRequest : VKRequest<VkUser>("account.getProfileInfo") {

    override fun parse(r: JSONObject): VkUser {
        val accountInfo = r.getJSONObject("response")

        return VkUser.parse(accountInfo)
    }
}