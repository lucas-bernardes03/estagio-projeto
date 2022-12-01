package com.unikasistemas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import com.unikasistemas.model.entities.Endereco;
import com.unikasistemas.model.servicos.EnderecoServico;

public class DeletePanelEndereco extends Panel{
    final private EnderecoServico servico = new EnderecoServico();
    public DeletePanelEndereco(String id, final Endereco endereco, final ModalWindow window) {
        super(id);
        
        Form<String> formDelete = new Form<String>("formDelete");
        add(formDelete);

        AjaxButton botaoDeletar = new AjaxButton("botaoDeletar", formDelete) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                int response = servico.deletar(endereco.getId());
                if(response==204) window.close(target);
            }
        };

        AjaxButton botaoCancelar = new AjaxButton("botaoCancelar", formDelete) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                window.close(target);
            }
        };
        formDelete.add(botaoDeletar,botaoCancelar);
    }

    
    
}
