package com.unikasistemas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import com.unikasistemas.model.entities.Monitorador;
import com.unikasistemas.model.servicos.MonitoradorServico;
import com.unikasistemas.validators.NumberFieldValidator;

import java.time.Instant;
import java.util.Date;

public class Editar extends BasePage{
    private final MonitoradorServico servicoM = new MonitoradorServico();
    
    public Editar(final Monitorador monitorador){
        add(new Label("editar","Editar"));
        CompoundPropertyModel<Monitorador> compoundPropertyModelMonitorador = new CompoundPropertyModel<Monitorador>(monitorador);
        Form<Monitorador> form = new Form<Monitorador>("formularioEdicao",compoundPropertyModelMonitorador){};
            
        add(form);

        final TextField<String> inputNome = new TextField<String>("nome");
        inputNome.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputRazao = new TextField<String>("razaoSocial");
        inputRazao.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputCPF = new TextField<String>("cpf");
        inputCPF.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputCNPJ = new TextField<String>("cnpj");          
        inputCNPJ.setOutputMarkupPlaceholderTag(true);
        
        final TextField<String> inputRG = new TextField<String>("rg"); 
        inputRG.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputInscricao = new TextField<String>("inscricaoEstadual");    
        inputInscricao.setOutputMarkupPlaceholderTag(true);

        final DateTextField inputDate = new DateTextField("dataNascimento", "yyyy-MM-dd");
        inputDate.setOutputMarkupPlaceholderTag(true);
        
        if(monitorador.getTipo().equals("Física")){
            inputNome.setVisible(true);
            inputCPF.setVisible(true);
            inputRG.setVisible(true);
            inputDate.setVisible(true);
            inputCNPJ.setVisible(false);
            inputRazao.setVisible(false);
            inputInscricao.setVisible(false);
        }
        else{
            inputNome.setVisible(false);
            inputCPF.setVisible(false);
            inputRG.setVisible(false);
            inputDate.setVisible(false);
            inputCNPJ.setVisible(true);
            inputRazao.setVisible(true);
            inputInscricao.setVisible(true);
        }

        final TextField<String> inputEmail = new TextField<String>("email");    
         
        final CheckBox inputAtivo = new CheckBox("ativo");

        form.add(inputNome,inputRazao,inputCPF,inputCNPJ,inputEmail,inputRG,inputInscricao,inputDate,inputAtivo);

        inputNome.setLabel(Model.of("Nome Completo")).setRequired(true).add(StringValidator.maximumLength(30));
        inputCPF.setLabel(Model.of("CPF")).setRequired(true).add(StringValidator.exactLength(11)).add(new NumberFieldValidator(inputCPF.getLabel().toString()));
        inputRG.setLabel(Model.of("RG")).setRequired(true).add(StringValidator.exactLength(7)).add(new NumberFieldValidator(inputRG.getLabel().toString()));
        inputDate.setLabel(Model.of("Data de Nascimento")).setRequired(true);
        
        inputRazao.setLabel(Model.of("Razao Social")).setRequired(true).add(StringValidator.maximumLength(30));
        inputCNPJ.setLabel(Model.of("CNPJ")).setRequired(true).add(StringValidator.exactLength(14)).add(new NumberFieldValidator(inputCNPJ.getLabel().toString()));
        inputInscricao.setLabel(Model.of("Inscricao Estadual")).setRequired(true).add(StringValidator.exactLength(12)).add(new NumberFieldValidator(inputInscricao.getLabel().toString()));

        inputEmail.setLabel(Model.of("E-mail")).setRequired(true).add(EmailAddressValidator.getInstance());
        inputAtivo.setLabel(Model.of("Ativo")).setRequired(true);

        FeedbackPanel error = new FeedbackPanel("feedbackError", new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR));
        error.setOutputMarkupId(true);
        add(error);

        AjaxButton botaoSubmit = new AjaxButton("botaoSubmit", form) {
            @Override
            public void onError(AjaxRequestTarget target, Form<?> formulario){
                target.add(error);
            }

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> fomulario){
                Monitorador m = form.getModelObject();
                m.setTipo(monitorador.getTipo());

                Monitorador response = servicoM.adicionarOuEditar(m);


                if(response.getId()==null){
                    if(response.getTipo().equals("Física")){
                        if(response.getCpf()==null) error("CPF já cadastrado.");
                        else error("RG já cadastrado.");
                    }
                    else{
                        if(response.getCnpj()==null) error("CNPJ já cadastrado.");
                        else error("Inscrição Estadual já cadastrado.");
                    }

                    target.add(error);
                }
                else setResponsePage(ListaMonitoradores.class);
            }
        };

        AjaxButton botaoVoltar = new AjaxButton("botaoVoltar", form) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form){
                setResponsePage(ListaMonitoradores.class);
            }
        };

        botaoVoltar.setDefaultFormProcessing(false);
        form.add(botaoSubmit, botaoVoltar);

    }
}
