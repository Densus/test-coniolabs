package test.login.model

import com.google.gson.annotations.SerializedName

class Auth {
    @SerializedName("token_type")
    var tokenType = ""
    @SerializedName("access_token")
    var accessToken = ""
}