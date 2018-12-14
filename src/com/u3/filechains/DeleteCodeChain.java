package com.u3.filechains;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;

public class DeleteCodeChain extends BaseChain {
   private Document document;
   private Project project;
    public DeleteCodeChain(Document document, Project project){
        this.document = document;
        this.project = project;
    }
    @Override
    public void process() {
         for (int i = 0; i < deleteLineNumbers.size(); i++) {
                int deleteStart = document.getLineStartOffset(deleteLineNumbers.get(i));
                int deleteEnd = document.getLineEndOffset(deleteLineNumbers.get(i));
                document.deleteString(deleteStart, deleteEnd);
            }
            PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
            manager.commitDocument(document);
    }
}
