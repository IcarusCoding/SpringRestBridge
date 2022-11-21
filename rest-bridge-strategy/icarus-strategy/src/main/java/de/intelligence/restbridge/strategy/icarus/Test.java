package de.intelligence.restbridge.strategy.icarus;

import de.intelligence.restbridge.api.annotation.RestClientController;
import de.intelligence.restbridge.api.http.HttpEncoding;
import de.intelligence.restbridge.strategy.icarus.annotation.Encoding;
import de.intelligence.restbridge.strategy.icarus.annotation.FormParam;
import de.intelligence.restbridge.strategy.icarus.annotation.HeaderParam;
import de.intelligence.restbridge.strategy.icarus.annotation.HttpHeader;
import de.intelligence.restbridge.strategy.icarus.annotation.PathParam;
import de.intelligence.restbridge.strategy.icarus.annotation.Post;

@RestClientController("http://sssss.de/{pisse}")
public interface Test {

    @Post("/test/{test}/test{test}")
    @HttpHeader("arsch:pisse")
    @HttpHeader("tretboot:kaktus")
    @Encoding(HttpEncoding.FORM_URL_ENCODING)
    String test(@PathParam("test") Object test, @FormParam("test") Object ratte, @HeaderParam("dsd") Object o);

}
