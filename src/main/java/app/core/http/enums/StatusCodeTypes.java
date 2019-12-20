package app.core.http.enums;

/**
 * Created by Ebrahim with ❤️ on 14 December 2019.
 */

public enum StatusCodeTypes {

    // 1xx Informational responses
    _100("100 Continue"),
    _101("101 Switching Protocols"),
    _102("102 Processing"),

    // 2xx Success
    _200("200 OK"),
    _201("201 Created"),
    _202("202 Accepted"),
    _203("203 Non-Authoritative Information"),
    _204("204 No Content"),
    _205("205 Reset Content"),
    _206("206 Partial Content"),

    // 3xx Redirection
    _300("300 Multiple Choices"),
    _301("301 Moved Permanently"),
    _302("302 Found"),
    _303("303 See Other"),
    _304("304 Not Modified"),
    _305("305 Use Proxy"),
    _307("307 Temporary Redirect"),

    // 4xx Client errors
    _400("400 Bad Request"),
    _401("401 Unauthorized"),
    _402("402 Payment Required"),
    _403("403 Forbidden"),
    _404("404 Not Found"),
    _405("405 Method Not Allowed"),
    _406("406 Not Acceptable"),
    _407("407 Proxy Authentication Required"),
    _408("408 Request Time-out"),
    _409("409 Conflict"),
    _410("410 Gone"),
    _411("411 Length Required"),
    _412("412 Precondition Failed"),
    _413("413 Request Entity Too Large"),
    _414("414 Request-URI Too Large"),
    _415("415 Unsupported Media Type"),
    _416("416 Requested range not satisfiable"),
    _417("417 Expectation Failed"),

    // 5xx Server errors
    _500("500 Internal Server Error"),
    _501("501 Not Implemented"),
    _502("502 Bad Gateway"),
    _503("503 Service Unavailable"),
    _504("504 Gateway Time-out"),
    _505("505 HTTP Version not supported");

    private final String code;

    StatusCodeTypes(String code) {
        this.code = code;
    }

    public static StatusCodeTypes statusOf(final String code) {
        for (StatusCodeTypes status : StatusCodeTypes.values()) {
            if (status.code.toLowerCase().equals(code.toLowerCase())) {
                return status;
            }
        }
        throw new IllegalArgumentException("status " + code + " is not valid");
    }


	public String getCode() {
		return code;
	}
}
