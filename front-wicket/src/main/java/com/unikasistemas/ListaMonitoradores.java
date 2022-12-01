package com.unikasistemas;

import com.unikasistemas.model.entities.Monitorador;
import com.unikasistemas.model.servicos.MonitoradorServico;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ListaMonitoradores extends BasePage{
    private final MonitoradorServico servicoM = new MonitoradorServico();
    public ListaMonitoradores(){
        final WebMarkupContainer containerF = new WebMarkupContainer("divF");
        containerF.setVisible(false);
        containerF.setOutputMarkupPlaceholderTag(true);
        containerF.setOutputMarkupId(true);
        add(containerF);

        final WebMarkupContainer containerJ = new WebMarkupContainer("divJ");
        containerJ.setVisible(false);
        containerJ.setOutputMarkupPlaceholderTag(true);
        containerJ.setOutputMarkupId(true);
        add(containerJ);

        final ModalWindow modalF = new ModalWindow("modalF");
        containerF.add(modalF);
        final ModalWindow modalJ = new ModalWindow("modalJ");
        containerJ.add(modalJ);

        List<Monitorador> init = null;

        final PageableListView<Monitorador> listagemF = new PageableListView<Monitorador>("listagemF", init, 10) {
            @Override
            protected void populateItem(final ListItem<Monitorador> item) {
                final Monitorador m = item.getModelObject();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

                item.add(new Label("nome",m.getNome()));
                item.add(new Label("cpf",m.getCpf()));
                item.add(new Label("email",m.getEmail()));
                item.add(new Label("rg",m.getRg()));
                item.add(new Label("dataNascimento",(m.getDataNascimento() != null) ? formatter.format(Instant.parse(m.getDataNascimento())):""));
                item.add(new Label("ativo",m.isAtivo() ? "Sim":"Não"));
                
                item.add(new AjaxLink<Void>("linkEditar") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        setResponsePage(new Editar(item.getModelObject()));
                    }
                });

                item.add(new AjaxLink<Void>("linkDeletar") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        modalF.setContent(new DeletePanel(modalF.getContentId(), item.getModelObject(), modalF));
                        modalF.setInitialHeight(150);
                        modalF.setInitialWidth(400);
                        modalF.show(target);
                        modalF.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
                            @Override
                            public void onClose(AjaxRequestTarget target) {
                                setResponsePage(ListaMonitoradores.class);
                            }
                            
                        });
                        
                    }
                });

                item.add(new AjaxLink<Void>("linkEnderecos") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        setResponsePage(new Enderecos(item.getModelObject()));
                    }
                });

                IModel<File> fileModel = new AbstractReadOnlyModel() {
                    @Override
                    public Object getObject() {
                        return servicoM.excelId(item.getModelObject().getId());
                    }
                };

                DownloadLink dLink = new DownloadLink("linkDD", fileModel);
                dLink.setDeleteAfterDownload(true);

                item.add(dLink);
            }
            
        };

        final PageableListView<Monitorador> listagemJ = new PageableListView<Monitorador>("listagemJ", init, 10) {
            @Override
            protected void populateItem(final ListItem<Monitorador> item) {
                final Monitorador m = item.getModelObject();

                item.add(new Label("razaoSocial",m.getRazaoSocial()));
                item.add(new Label("cnpj",m.getCnpj()));
                item.add(new Label("email",m.getEmail()));
                item.add(new Label("inscricaoEstadual",m.getInscricaoEstadual()));
                item.add(new Label("ativo",m.isAtivo() ? "Sim":"Não"));

                item.add(new AjaxLink<Void>("linkEditar") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        setResponsePage(new Editar(item.getModelObject()));
                    }
                });

                item.add(new AjaxLink<Void>("linkDeletar") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        modalJ.setContent(new DeletePanel(modalJ.getContentId(), item.getModelObject(), modalJ));
                        modalJ.setInitialHeight(150);
                        modalJ.setInitialWidth(400);
                        modalJ.show(target);
                        modalJ.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
                            @Override
                            public void onClose(AjaxRequestTarget target) {
                                setResponsePage(ListaMonitoradores.class);
                            }

                        });

                    }
                });

                item.add(new AjaxLink<Void>("linkEnderecos") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        setResponsePage(new Enderecos(item.getModelObject()));
                    }
                });

                IModel<File> fileModel = new AbstractReadOnlyModel() {
                    @Override
                    public Object getObject() {
                        return servicoM.excelId(item.getModelObject().getId());
                    }
                };

                DownloadLink dLink = new DownloadLink("linkDD", fileModel);
                dLink.setDeleteAfterDownload(true);

                item.add(dLink);
            }

        };

        Form<String> form = new Form<>("formPesquisa");
        add(form);

        final TextField<String> searchBar = new TextField<>("barraBusca", Model.of(""));
        searchBar.add(StringValidator.maximumLength(30));

        IModel<File> excelFile = new AbstractReadOnlyModel<File>() {
            @Override
            public File getObject() {
                return servicoM.excelAll();
            }
        };

        DownloadLink dExcel = new DownloadLink("excelDd", excelFile);
        dExcel.setDeleteAfterDownload(true);

        IModel<File> pdfFile = new AbstractReadOnlyModel<File>() {
            @Override
            public File getObject() {
                return servicoM.pdf();
            }
        };

        DownloadLink dPdf = new DownloadLink("pdfDd", pdfFile);
        dPdf.setDeleteAfterDownload(true);


        AjaxButton uploadButton = new AjaxButton("upload", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                setResponsePage(new Upload());
            }
        };


        form.add(searchBar, dExcel, dPdf, uploadButton);

        List<String> tipos = Arrays.asList("Física", "Jurídica");
        DropDownChoice<String> dropDown = new DropDownChoice<>("ddown", new Model<>(), tipos);
        dropDown.setNullValid(false);

        dropDown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(dropDown.getModelObject().equals("Física")){
                    List<Monitorador> lista = servicoM.listarTodos().stream().filter(m -> m.getTipo().equals("Física")).collect(Collectors.toList());
                    listagemF.setList(lista);

                    containerF.setVisible(true);
                    containerJ.setVisible(false);

                    target.add(containerF, containerJ, dropDown);
                }
                else{
                    List<Monitorador> lista = servicoM.listarTodos().stream().filter(m -> m.getTipo().equals("Jurídica")).collect(Collectors.toList());
                    listagemJ.setList(lista);

                    containerF.setVisible(false);
                    containerJ.setVisible(true);

                    target.add(containerF, containerJ, dropDown);
                }
            }
        });

        AjaxButton botaoPesquisa = new AjaxButton("botaoPesquisa",form) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form){
                if(searchBar.getModelObject() == null){
                    if(listagemF.isVisible()){
                        List<Monitorador> lista = servicoM.listarTodos().stream().filter(m -> m.getTipo().equals("Física")).collect(Collectors.toList());
                        listagemF.setList(lista);
                        target.add(containerF);
                    }
                    else{
                        List<Monitorador> lista = servicoM.listarTodos().stream().filter(m -> m.getTipo().equals("Jurídica")).collect(Collectors.toList());
                        listagemJ.setList(lista);
                        target.add(containerJ);
                    }
                }
                else{
                    if(listagemF.isVisible()){
                        List<Monitorador> lista = listagemF.getModelObject().stream().filter(m -> m.getNome().toUpperCase(Locale.ROOT).contains(searchBar.getModelObject().toUpperCase(Locale.ROOT)) || m.getRg().contains(searchBar.getModelObject()) || m.getCpf().contains(searchBar.getModelObject())).collect(Collectors.toList());
                        listagemF.setList(lista);
                        target.add(containerF);
                    }
                    else{
                        List<Monitorador> lista = listagemJ.getModelObject().stream().filter(m -> m.getRazaoSocial().toUpperCase(Locale.ROOT).contains(searchBar.getModelObject().toUpperCase(Locale.ROOT)) || m.getInscricaoEstadual().contains(searchBar.getModelObject()) || m.getCnpj().contains(searchBar.getModelObject())).collect(Collectors.toList());
                        listagemJ.setList(lista);
                        target.add(containerJ);
                    }
                }
            }
        };

        form.add(dropDown,botaoPesquisa);

        containerF.add(listagemF);
        containerJ.add(listagemJ);

        PagingNavigator pF = new PagingNavigator("navigatorF", listagemF);
        PagingNavigator pJ = new PagingNavigator("navigatorJ", listagemJ);

        containerF.add(pF);
        containerJ.add(pJ);
    }

}
