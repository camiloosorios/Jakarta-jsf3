package org.aguzman.webapp.jsf3.controllers;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.aguzman.webapp.jsf3.entities.Categoria;
import org.aguzman.webapp.jsf3.entities.Producto;
import org.aguzman.webapp.jsf3.services.ProductoService;

import java.util.List;
import java.util.ResourceBundle;

@Model
public class ProductoController {

    private Long id;

    @Inject
    private ProductoService service;

    @Inject
    private FacesContext facesContext;

    @Inject
    private ResourceBundle bundle;

    private Producto producto;

    private List<Producto> listado;

    private String textoBuscar;

    @PostConstruct
    public void init() {
        this.listado = service.listar();
        producto = new Producto();
    }

    @Produces
    @Model
    public String titulo() {
        return bundle.getString("producto.texto.titulo");
    }

    @Produces
    @RequestScoped
    @Named("listado")
    public List<Producto> findAll() {
        return service.listar();
    }

//    @Produces
//    @Model
    public Producto producto() {
        this.producto = new Producto();
        if (id != null && id > 0) {
            service.porId(id).ifPresent(p -> this.producto = p);
        }
        return this.producto;
    }

    @Produces
    @Model
    public List<Categoria> categorias() {
        return service.listarCategorias();
    }

    public void guardar() {
        if (producto.getId() != null && producto.getId() > 0) {
            facesContext.addMessage(null,
                    new FacesMessage(String.format(bundle.getString("producto.mensaje.editar"),
                    producto.getNombre())));
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(String.format(bundle.getString("producto.mensaje.crear"),
                    producto.getNombre())));
        }
        service.guardar(this.producto);
        listado = service.listar();
        producto = new Producto();
    }

    public void editar(Long id) {
        this.id = id;
        this.producto();
    }

    public void eliminar(Long id) {
        service.eliminar(id);
        facesContext.addMessage(null,
                new FacesMessage(bundle.getString("producto.mensaje.eliminar")));
        listado = service.listar();
    }

    public void buscar() {
        this.listado = service.buscarPorNombre(this.textoBuscar);
    }

    public void cerrarDialogo() {
        producto = new Producto();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Producto> getListado() {
        return listado;
    }

    public void setListado(List<Producto> listado) {
        this.listado = listado;
    }

    public String getTextoBuscar() {
        return textoBuscar;
    }

    public void setTextoBuscar(String textoBuscar) {
        this.textoBuscar = textoBuscar;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
