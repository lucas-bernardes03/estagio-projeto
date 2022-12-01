package com.unikasistemas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

public class AcaoConcluida extends Panel{
    public AcaoConcluida(String id, final ModalWindow modalWindow){
        super(id);
        
        Form<String> formSucesso = new Form<>("formSucesso");
        add(formSucesso);

        AjaxButton botaoOk = new AjaxButton("botaoOk",formSucesso) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form){
                modalWindow.close(target);
            }
        };
        formSucesso.add(botaoOk);
    }
}
