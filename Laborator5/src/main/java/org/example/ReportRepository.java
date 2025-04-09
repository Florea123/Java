package org.example;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.awt.Desktop;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReportRepository implements Command{
    @Override
    public void execute(BufferedReader reader, Repository repo) throws IOException {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init();

        Template template = velocityEngine.getTemplate("src/main/resources/templates/report.vm");

        VelocityContext context = new VelocityContext();
        context.put("images", repo.getImages());

        File reportFile = new File("repository_report.html");
        try (Writer writer = new FileWriter(reportFile)) {
            template.merge(context, writer);
        }

        Desktop.getDesktop().open(reportFile);
    }
}
