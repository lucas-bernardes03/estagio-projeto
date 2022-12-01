package com.unikasistemas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ErrorLevelFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.StringValidator;

import com.unikasistemas.model.entities.Endereco;
import com.unikasistemas.model.entities.Monitorador;
import com.unikasistemas.model.servicos.EnderecoServico;
import com.unikasistemas.validators.NumberFieldValidator;
import com.unikasistemas.validators.PhoneValidator;


public class EditarEndereco extends BasePage{
    private final EnderecoServico servicoE = new EnderecoServico();

    public EditarEndereco(final Endereco endereco, final Monitorador monitorador){
        add(new Label("lNEndereco",Model.of("Novo Endereco")));

        final IModel<Boolean> selected = new Model<Boolean>();
        final RadioGroup<Boolean> group = new RadioGroup<>("ativo", selected);
        
        group.add(new Radio<Boolean>("principal", new Model<Boolean>(true)));
        group.add(new Radio<Boolean>("secundario", new Model<Boolean>(false)));

        CompoundPropertyModel<Endereco> compoundPropertyModelEndereco = new CompoundPropertyModel<Endereco>(endereco);
        Form<Endereco> form = new Form<Endereco>("formAddEndereco", compoundPropertyModelEndereco){};
        add(form);

        final TextField<String> inputEndereco = new TextField<>("endereco");
        final TextField<String> inputNumero = new TextField<>("numero");
        final TextField<String> inputCep = new TextField<>("cep");
        final TextField<String> inputBairro = new TextField<>("bairro");
        final TextField<String> inputTelefone = new TextField<>("telefone");
        final TextField<String> inputCidade = new TextField<>("cidade");
        final TextField<String> inputEstado = new TextField<>("estado");

        form.add(inputEndereco,inputNumero,inputCep,inputBairro,inputTelefone,inputCidade,inputEstado,group);

        inputEndereco.setLabel(Model.of("Rua")).setRequired(true).add(StringValidator.maximumLength(30));
        inputNumero.setLabel(Model.of("Numero")).setRequired(true).add(StringValidator.maximumLength(6)).add(new NumberFieldValidator(inputNumero.getLabel().toString()));
        inputCep.setLabel(Model.of("CEP")).setRequired(true).add(StringValidator.exactLength(8)).add(new NumberFieldValidator(inputCep.getLabel().toString()));
        inputBairro.setLabel(Model.of("Bairro")).setRequired(true).add(StringValidator.maximumLength(30));
        inputTelefone.setLabel(Model.of("Telefone")).setRequired(true).add(new PhoneValidator());
        inputCidade.setLabel(Model.of("Cidade")).setRequired(true);
        inputEstado.setLabel(Model.of("Estado")).setRequired(true);
        group.setLabel(Model.of("Tipo de Endereco")).setRequired(true);

        final FeedbackPanel error = new FeedbackPanel("feedbackError", new ErrorLevelFeedbackMessageFilter(FeedbackMessage.ERROR));
        add(error);

        AjaxButton botaoVoltar = new AjaxButton("botaoVoltar", form) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form){
                setResponsePage(new Enderecos(monitorador));
            }
        };

        AjaxButton botaoSalvar = new AjaxButton("botaoSalvar", form) {
            @Override
            public void onError(AjaxRequestTarget target, Form<?> form){target.add(error);}

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> formulario){
                Endereco e = form.getModelObject();

                e.setMonitorador(monitorador);
                e.setPrincipal(selected.getObject());

                servicoE.Editar(e);

                System.out.println("Endereco: " + e);
                setResponsePage(new Enderecos(monitorador));
            }
        };

        botaoVoltar.setDefaultFormProcessing(false);
        form.add(botaoVoltar, botaoSalvar);

    }
}
