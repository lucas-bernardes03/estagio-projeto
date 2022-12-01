package com.unikasistemas;

import com.unikasistemas.model.entities.Endereco;
import com.unikasistemas.model.entities.Monitorador;
import com.unikasistemas.model.servicos.EnderecoServico;
import com.unikasistemas.model.servicos.MonitoradorServico;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

public class Enderecos extends BasePage{
    private final MonitoradorServico servico = new MonitoradorServico();

    public Enderecos(final Monitorador monitorador){
        //ENDERECOS

        Label eLabel = new Label("eLabel", Model.of("Endereços"));
        add(eLabel);

        Form<String> form = new Form<>("formAdicionar");
        add(form);

        AjaxButton botaoAdicionar = new AjaxButton("botaoAdicionar",form) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form){
                setResponsePage(new NovoEndereco(monitorador));
            }
        };

        form.add(botaoAdicionar);


        final WebMarkupContainer containerEnderecos = new WebMarkupContainer("divEnderecos");
        containerEnderecos.setVisible(false);
        containerEnderecos.setOutputMarkupPlaceholderTag(true);
        containerEnderecos.setOutputMarkupId(true);
        add(containerEnderecos);

        final ModalWindow modalWindow = new ModalWindow("modalWindow");
        containerEnderecos.add(modalWindow);

        final List<Endereco> listE = servico.listarEnderecos(monitorador.getId());

        final PageableListView<Endereco> enderecoTable = new PageableListView<Endereco>("enderecos", listE, 5) {
        
            @Override
            protected void populateItem(final ListItem<Endereco> item) {
                final Endereco e = item.getModelObject();
                
                item.add(new Label("endereco", e.getEndereco()));
                item.add(new Label("numero",e.getNumero()));
                item.add(new Label("cep",e.getCep()));
                item.add(new Label("bairro",e.getBairro()));
                item.add(new Label("telefone",e.getTelefone()));
                item.add(new Label("cidade",e.getCidade()));
                item.add(new Label("estado",e.getEstado()));
                item.add(new Label("principal",e.isPrincipal() ? "Sim":"Não"));
                item.add(new AjaxLink<Void>("linkEditar") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        setResponsePage(new EditarEndereco(item.getModelObject(),monitorador));
                    }
                });
                item.add(new AjaxLink<Void>("linkDeletar") {
                    @Override
                    public void onClick(AjaxRequestTarget target){
                        modalWindow.setContent(new DeletePanelEndereco(modalWindow.getContentId(), item.getModelObject(), modalWindow));
                        modalWindow.setInitialHeight(150);
                        modalWindow.setInitialWidth(400);
                        modalWindow.show(target);
                        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback(){
                            @Override
                            public void onClose(AjaxRequestTarget target) {
                                setResponsePage(new Enderecos(monitorador));
                            }
                            
                        });
                    }
                });
            }
        };


        containerEnderecos.add(enderecoTable);
        containerEnderecos.add(new PagingNavigator("navigator", enderecoTable));
        containerEnderecos.setVisible(true);

    }
}
