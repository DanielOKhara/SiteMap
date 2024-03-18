package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveAction;

public class FactorialTask extends RecursiveAction {
    CopyOnWriteArraySet<String> urls;
    Parser parser;

    public FactorialTask(Parser parser, CopyOnWriteArraySet<String> urls) {
        this.parser = parser;
        this.urls = urls;
    }

    @Override
    protected void compute() {
        List<FactorialTask> tasks = new ArrayList<>();
        for (String url : urls) {
            FactorialTask task = new FactorialTask(parser, parser.getUrls(url));
            task.fork();
        }
        for (FactorialTask task : tasks) {
            task.join();
        }
    }
}
