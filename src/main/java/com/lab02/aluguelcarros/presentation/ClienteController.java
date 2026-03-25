package com.lab02.aluguelcarros.presentation;

import com.lab02.aluguelcarros.application.ClienteService;
import com.lab02.aluguelcarros.application.CpfJaExisteException;
import com.lab02.aluguelcarros.presentation.dto.ClienteForm;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;
import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller("/clientes")
@ExecuteOn(TaskExecutors.BLOCKING)
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @Get("/")
    @Produces(MediaType.TEXT_HTML)
    @View("cliente/list")
    public Map<String, Object> listar() {
        Map<String, Object> model = new HashMap<>();
        model.put("clientes", service.listarTodos());
        return model;
    }

    @Get("/novo")
    @Produces(MediaType.TEXT_HTML)
    @View("cliente/form")
    public Map<String, Object> formularioCriacao() {
        Map<String, Object> model = new HashMap<>();
        model.put("form", new ClienteForm());
        model.put("action", "/clientes");
        model.put("titulo", "Novo Cliente");
        return model;
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Object criar(@Body ClienteForm form) {
        try {
            service.criar(form);
            return HttpResponse.seeOther(URI.create("/clientes/"));
        } catch (CpfJaExisteException e) {
            return formularioComErro(form, "/clientes", "Novo Cliente", e.getMessage());
        } catch (IllegalArgumentException e) {
            return formularioComErro(form, "/clientes", "Novo Cliente", e.getMessage());
        }
    }

    @Get("/{id}")
    @Produces(MediaType.TEXT_HTML)
    @View("cliente/detail")
    public HttpResponse<?> detalhar(@PathVariable UUID id) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("cliente", service.buscarPorId(id));
            return HttpResponse.ok(model);
        } catch (EntityNotFoundException e) {
            return HttpResponse.notFound();
        }
    }

    @Get("/{id}/editar")
    @Produces(MediaType.TEXT_HTML)
    @View("cliente/form")
    public HttpResponse<?> formularioEdicao(@PathVariable UUID id) {
        try {
            ClienteForm form = ClienteForm.fromEntity(service.buscarPorId(id));
            Map<String, Object> model = new HashMap<>();
            model.put("form", form);
            model.put("action", "/clientes/" + id);
            model.put("titulo", "Editar Cliente");
            return HttpResponse.ok(model);
        } catch (EntityNotFoundException e) {
            return HttpResponse.notFound();
        }
    }

    @Post("/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Object atualizar(@PathVariable UUID id, @Body ClienteForm form) {
        try {
            service.atualizar(id, form);
            return HttpResponse.seeOther(URI.create("/clientes/"));
        } catch (CpfJaExisteException e) {
            return formularioComErro(form, "/clientes/" + id, "Editar Cliente", e.getMessage());
        } catch (IllegalArgumentException e) {
            return formularioComErro(form, "/clientes/" + id, "Editar Cliente", e.getMessage());
        } catch (EntityNotFoundException e) {
            return HttpResponse.notFound();
        }
    }

    @Post("/{id}/deletar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> deletar(@PathVariable UUID id) {
        try {
            service.deletar(id);
        } catch (EntityNotFoundException ignored) {}
        return HttpResponse.seeOther(URI.create("/clientes/"));
    }

    private ModelAndView<Map<String, Object>> formularioComErro(ClienteForm form, String action,
                                                                 String titulo, String erro) {
        Map<String, Object> model = new HashMap<>();
        model.put("form", form);
        model.put("action", action);
        model.put("titulo", titulo);
        model.put("erro", erro);
        return new ModelAndView<>("cliente/form", model);
    }
}
