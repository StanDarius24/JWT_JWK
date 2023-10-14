package com.stannis.jwtgendec.model

class CertJsonBean {

    var privatekey: String? = null
    var payload: Map<String, Any>? = null
    var header: Map<String, Any>? = null

    override fun toString(): String {
        return "CertJsonBean{" +
                "privatekey='" + privatekey + '\'' +
                ", payload=" + payload +
                ", header=" + header +
                '}'
    }

}
