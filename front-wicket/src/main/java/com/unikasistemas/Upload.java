package com.unikasistemas;


import com.unikasistemas.model.entities.Monitorador;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.lang.Bytes;

import java.io.File;
import java.util.List;

public class Upload extends BasePage {
    final private FileUploadField fileUploadField;

    public Upload(){
        add(new FeedbackPanel("feedback"));

        WebMarkupContainer container = new WebMarkupContainer("content");
        container.setOutputMarkupPlaceholderTag(true);
        container.setOutputMarkupId(true);
        container.setVisible(false);

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
                        container.setVisible(true);
                    }
                }
            }
        };

        form.setMultiPart(true);
        form.setMaxSize(Bytes.megabytes(5));

        form.add(fileUploadField = new FileUploadField("fileUploadField"));
        add(form);

        List<Monitorador> dados = null;
        final PageableListView<Monitorador> list = new PageableListView<Monitorador>("lista",dados,10) {
            @Override
            protected void populateItem(final ListItem<Monitorador> item) {
                final Monitorador m = item.getModelObject();
                boolean type = m.getTipo().equals("Física");

                item.add(new Label("tipo", m.getTipo()));
                item.add(new Label("nomeRazao", type ? m.getNome():m.getRazaoSocial()));
                item.add(new Label("cpfCnpj", type ? m.getCpf():m.getCnpj()));
                item.add(new Label("rgIns", type ? m.getRg():m.getInscricaoEstadual()));

            }
        };

        container.add(list);

        add(container);

    }
}
