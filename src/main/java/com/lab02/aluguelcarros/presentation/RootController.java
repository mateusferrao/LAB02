package com.lab02.aluguelcarros.presentation;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.net.URI;

@Controller("/")
public class RootController {

    @Get
    public HttpResponse<?> index() {
        return HttpResponse.seeOther(URI.create("/clientes/"));
    }
}
