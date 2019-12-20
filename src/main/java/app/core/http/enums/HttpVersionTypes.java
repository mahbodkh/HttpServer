package app.core.http.enums;

/**
 * Created by Ebrahim with ❤️ on 14 December 2019.
 */

public enum HttpVersionTypes {
  Http1_0 {
    @Override public boolean isHttp1_0() { return true; }
  },
  Http1_1 {
    @Override public boolean isHttp1_1() { return true; }
  },
  Http2_0 {
    @Override public boolean isHttp2_0() { return true; }
  };

  public static final String HTTP_1_0 = "HTTP/1.0";
  public static final String HTTP_1_1 = "HTTP/1.1";
  public static final String HTTP_2_0 = "HTTP/2.0";

  public static HttpVersionTypes from(final String version) {
    switch (version) {
      case HTTP_1_0: return Http1_0;
      case HTTP_1_1: return Http1_1;
      case HTTP_2_0: return Http2_0;
    }
    throw new IllegalArgumentException("Unsupported HTTP/version: " + version);
  }

  public boolean isHttp1_0() { return false; }
  public boolean isHttp1_1() { return false; }
  public boolean isHttp2_0() { return false; }

  @Override
  public String toString() {
    if (this.isHttp1_0()) {
      return HTTP_1_0;
    } else if (this.isHttp1_1()) {
      return HTTP_1_1;
    } else if (this.isHttp2_0()) {
      return HTTP_2_0;
    }
    return "HTTP/version unsupported";
  }
}
