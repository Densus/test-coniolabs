package test.login.model

import com.bluelinelabs.logansquare.annotation.JsonField

class LoginUser() {
    @JsonField(name = ["email"])
    var email = ""
    @JsonField(name = ["password"])
    var password = ""
}