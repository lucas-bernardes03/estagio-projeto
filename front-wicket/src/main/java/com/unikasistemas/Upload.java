package com.unikasistemas;


import com.unikasistemas.model.entities.Monitorador;
import com.unikasistemas.model.servicos.MonitoradorServico;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Bytes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Upload extends BasePage {
    final private FileUploadField fileUploadField;
    final private MonitoradorServico servico = new MonitoradorServico();

    public Upload(){
        FeedbackPanel error = new FeedbackPanel("errorFb", FeedbackMessage::isError);
        FeedbackPanel warning = new FeedbackPanel("warningFb", FeedbackMessage::isInfo);
        add(error, warning);

        WebMarkupContainer container = new WebMarkupContainer("content");
        container.setOutputMarkupPlaceholderTag(true);
        container.setOutputMarkupId(true);
        container.setVisible(false);

        final ModalWindow modalWindow = new ModalWindow("modalWindow");
        container.add(modalWindow);

        List<Monitorador> finalList = new ArrayList<>();

        Form<Void> fsalvar = new Form<>("fsalvar");
        final PageableListView<Monitorador> list = new PageableListView<Monitorador>("lista", new ArrayList<>(),10) {
            @Override
            protected void populateItem(final ListItem<Monitorador> item) {
                final Monitorador m = item.getModelObject();
                String camposInvalidos = servico.validateFields(m);
                if(camposInvalidos.equals("")) finalList.add(m);
                boolean type = m.getTipo().equals("Física");

                item.add(new Label("tipo", m.getTipo()));
                item.add(new Label("nomeRazao", type ? m.getNome():m.getRazaoSocial()));
                item.add(new Label("cpfCnpj", type ? m.getCpf():m.getCnpj()));
                item.add(new Label("rgIns", type ? m.getRg():m.getInscricaoEstadual()));
                item.add(new Label("end", m.getEnderecos().get(0).getEndereco()));
                item.add(new Label("bairro",  m.getEnderecos().get(0).getBairro()));
                item.add(new Label("cidade",  m.getEnderecos().get(0).getCidade()));
                item.add(new Label("estado",  m.getEnderecos().get(0).getEstado()));
                item.add(new Label("telefone",  m.getEnderecos().get(0).getTelefone()));
                item.add(new Label("invalidos", camposInvalidos));

            }
        };

        final Form<?> form = new Form<Void>("form"){
            @Override
            protected void onSubmit(){
                final FileUpload uploadedFile = fileUploadField.getFileUpload();
                if(uploadedFile != null){
                    File newFile = new File("src/main/resources/"+uploadedFile.getClientFileName());

                    if(newFile.exists()) newFile.delete();

                    try{
                        newFile.createNewFile();
                        uploadedFile.writeTo(newFile);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        List<Monitorador> monitoradores = servico.parseExcel(newFile);
                        list.setList(monitoradores);
                        if(monitoradores.size()>0) fsalvar.setVisible(true);
                        container.setVisible(true);
                        info("AVISO: Monitoradores com dados não preenchidos não serão listados.");
                        info("AVISO: Monitoradores com campos inválidos ou com dados já existentes no sistema não serão adicionados.");
                    }
                }
            }
        };

        form.setMultiPart(true);
        form.setMaxSize(Bytes.megabytes(5));

        form.add(fileUploadField = new FileUploadField("fileUploadField"));
        add(form);

        IModel<File> model = new AbstractReadOnlyModel<File>() {
            @Override
            public File getObject() {
                return servico.excelModel();
            }
        };
        AjaxButton salvar = new AjaxButton("salvar", fsalvar) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                for(Monitorador m : finalList) servico.addExcel(m);

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
        };

        fsalvar.setVisible(false);
        fsalvar.setOutputMarkupId(true);

        add(fsalvar);
        fsalvar.add(salvar);

        DownloadLink modelo = new DownloadLink("dlink", model);
        modelo.setDeleteAfterDownload(true);
        form.add(modelo);

        container.add(list);
        add(container);

    }
}
