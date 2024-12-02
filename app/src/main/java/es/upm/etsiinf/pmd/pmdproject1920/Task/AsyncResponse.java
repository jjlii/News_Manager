package es.upm.etsiinf.pmd.pmdproject1920.Task;

import java.util.List;

import es.upm.etsiinf.pmd.pmdproject1920.model.Article;

public interface AsyncResponse {
    void processFinish(List<Article> articles);
}
