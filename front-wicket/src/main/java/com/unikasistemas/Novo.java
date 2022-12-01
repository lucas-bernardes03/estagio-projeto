package com.unikasistemas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
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

public class Novo extends BasePage{  
    private boolean type;
    private final MonitoradorServico servicoM = new MonitoradorServico();

    private Monitorador response;
    public Novo(){
        this(new Monitorador());
    }

    protected Novo(Monitorador monitorador){
        add(new Label("novo","Registrar Monitorador"));
        CompoundPropertyModel<Monitorador> compoundPropertyModelMonitorador = new CompoundPropertyModel<>(monitorador);
        
        final WebMarkupContainer container = new WebMarkupContainer("divC");
        add(container);

        final ModalWindow modalWindow = new ModalWindow("modalWindow");
        container.add(modalWindow);
        Form<Monitorador> form = new Form<Monitorador>("formularioNovo",compoundPropertyModelMonitorador){};
            
        add(form);

        final TextField<String> inputNome = new TextField<>("nome");
        inputNome.setVisible(false);
        inputNome.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputRazao = new TextField<>("razaoSocial");
        inputRazao.setVisible(false);
        inputRazao.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputCPF = new TextField<>("cpf");
        inputCPF.setVisible(false);
        inputCPF.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputCNPJ = new TextField<>("cnpj");
        inputCNPJ.setVisible(false);        
        inputCNPJ.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputEmail = new TextField<>("email");
        inputEmail.setVisible(false);
        inputEmail.setOutputMarkupPlaceholderTag(true);

        final TextField<String> inputRG = new TextField<>("rg");
        inputRG.setVisible(false);
        inputRG.setOutputMarkupPlaceholderTag(true);  
        
        final TextField<String> inputInscricao = new TextField<>("inscricaoEstadual");
        inputInscricao.setVisible(false);
        inputInscricao.setOutputMarkupPlaceholderTag(true);
        
        final Label dataLabel = new Label("dataLabel", Model.of("Data de Nascimento"));
        dataLabel.setVisible(false);
        dataLabel.setOutputMarkupPlaceholderTag(true);

        final DateTextField inputDate = new DateTextField("dataNascimento", "yyyy-MM-dd");
        inputDate.setVisible(false);
        inputDate.setOutputMarkupPlaceholderTag(true);
        
        final Label ativoLabel = new Label("ativoLabel",Model.of("Ativo? "));
        ativoLabel.setVisible(false);
        ativoLabel.setOutputMarkupPlaceholderTag(true);

        final CheckBox inputAtivo = new CheckBox("ativo");
        inputAtivo.setVisible(false);
        inputAtivo.setOutputMarkupPlaceholderTag(true);

        form.add(inputNome,inputRazao,inputCPF,inputCNPJ,inputEmail,inputRG,inputInscricao,inputDate,inputAtivo,dataLabel,ativoLabel);

        inputNome.setLabel(Model.of("Nome Completo")).setRequired(true).add(StringValidator.maximumLength(30));
        inputCPF.setLabel(Model.of("CPF")).setRequired(true).add(StringValidator.exactLength(11)).add(new NumberFieldValidator(inputCPF.getLabel().toString()));
        inputRG.setLabel(Model.of("RG")).setRequired(true).add(StringValidator.exactLength(7)).add(new NumberFieldValidator(inputRG.getLabel().toString()));
        inputDate.setLabel(Model.of("Data de Nascimento")).setRequired(true);
        
        inputRazao.setLabel(Model.of("Razao Social")).setRequired(true).add(StringValidator.maximumLength(30));
        inputCNPJ.setLabel(Model.of("CNPJ")).setRequired(true).add(StringValidator.exactLength(14)).add(new NumberFieldValidator(inputCNPJ.getLabel().toString()));
        inputInscricao.setLabel(Model.of("Inscricao Estadual")).setRequired(true).add(StringValidator.exactLength(12)).add(new NumberFieldValidator(inputInscricao.getLabel().toString()));

        inputEmail.setLabel(Model.of("E-mail")).setRequired(true).add(EmailAddressValidator.getInstance());
        inputAtivo.setLabel(Model.of("Ativo")).setRequired(true);
        
        final FeedbackPanel error = new FeedbackPanel("feedbackError", new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR));
        error.setOutputMarkupId(true);
        add(error);

        final AjaxButton botaoSubmit = new AjaxButton("botaoSubmit") {
            @Override
            public void onError(AjaxRequestTarget target, Form<?> form){
                target.add(error);
            }

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> formulario){
                Monitorador m = form.getModelObject();

                if(type) m.setTipo("Física");
                else m.setTipo("Jurídica");

                System.out.println("Monitorador: " + m);
                response = servicoM.adicionarOuEditar(m);

                if(response.getId()==null){
                    if(type){
                        if(response.getCpf()==null) error("CPF já cadastrado.");
                        else error("RG já cadastrado.");
                    }
                    else{
                        if(response.getCnpj()==null) error("CNPJ já cadastrado.");
                        else error("Inscrição Estadual já cadastrado.");
                    }

                    target.add(error);
                }
                else {
                    modalWindow.setContent(new AcaoConcluida(modalWindow.getContentId(), modalWindow));
                    modalWindow.setInitialHeight(150);
                    modalWindow.setInitialWidth(400);
                    modalWindow.show(target);
                    modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                        @Override
                        public void onClose(AjaxRequestTarget target){
                            setResponsePage(ListaMonitoradores.class);
                        }
                    });

                }
            }
        };

        botaoSubmit.setVisible(false);
        botaoSubmit.setOutputMarkupPlaceholderTag(true);

        AjaxButton botaoFisica = new AjaxButton("botaoFisica", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                inputNome.setVisible(true);            
                inputRazao.setVisible(false);
                inputCPF.setVisible(true);
                inputCNPJ.setVisible(false);
                inputRG.setVisible(true);
                inputInscricao.setVisible(false);
                dataLabel.setVisible(true);
                inputDate.setVisible(true);

                if(!inputEmail.isVisible()){
                    inputEmail.setVisible(true);
                    ativoLabel.setVisible(true);
                    inputAtivo.setVisible(true);
                    botaoSubmit.setVisible(true);
                }

                type = true;
                target.add(inputNome,inputRazao,inputCPF,inputCNPJ,inputEmail,inputRG,inputInscricao,inputDate,dataLabel,inputAtivo,ativoLabel,botaoSubmit);
            }
        };

        AjaxButton botaoJuridica = new AjaxButton("botaoJuridica",form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                inputNome.setVisible(false);            
                inputRazao.setVisible(true);
                inputCPF.setVisible(false);
                inputCNPJ.setVisible(true);
                inputRG.setVisible(false);
                inputInscricao.setVisible(true);
                dataLabel.setVisible(false);
                inputDate.setVisible(false);

                if(!inputEmail.isVisible()){
                    inputEmail.setVisible(true);
                    ativoLabel.setVisible(true);
                    inputAtivo.setVisible(true);
                    botaoSubmit.setVisible(true);
                }

                type = false;
                target.add(inputNome,inputRazao,inputCPF,inputCNPJ,inputEmail,inputRG,inputInscricao,inputDate,dataLabel,inputAtivo,ativoLabel,botaoSubmit);
            }
        };

        botaoFisica.setDefaultFormProcessing(false);
        botaoJuridica.setDefaultFormProcessing(false);
        form.add(botaoFisica,botaoJuridica,botaoSubmit);

    }

}
