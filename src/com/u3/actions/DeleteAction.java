package com.u3.actions;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.u3.codegenerator.FindViewByIdWriter;
import com.u3.filechains.BaseChain;
import com.u3.filechains.FindAPIUseChain;
import com.u3.filechains.FindBindAnnotationChain;
import com.u3.filechains.FindImportChain;

import java.util.*;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class DeleteAction extends  WriteCommandAction.Simple{
    List<Integer> deleteLineNumbers;
    Project project;
    PsiFile file;
    String[] currentDoc;
    PsiClass mClass;
    private PsiElementFactory mFactory;
    Document document;
    Map<String,String> nameAndIdMap = new LinkedHashMap<>();
    BaseChain findAPIChain, findBindChain, findImportChain;

    public DeleteAction(Project project, PsiFile file,Document document, PsiClass psiClass){
        super(project, file);
        this.document = document;
        currentDoc= document.getText().split("\n");
        this.project = project;
        this.file = file;
        this.mClass = psiClass;
        mFactory = JavaPsiFacade.getElementFactory(project);
        deleteLineNumbers = new ArrayList<>();
    }
    @Override
    protected void run(){
        try {
            findImportChain = new FindImportChain();
            findBindChain = new FindBindAnnotationChain();
            findAPIChain = new FindAPIUseChain();
            findImportChain.setNext(findBindChain);
            findBindChain.setNext(findAPIChain);
            findImportChain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);
            for (int i = 0; i < deleteLineNumbers.size(); i++) {
                int deleteStart = document.getLineStartOffset(deleteLineNumbers.get(i));
                int deleteEnd = document.getLineEndOffset(deleteLineNumbers.get(i));
                document.deleteString(deleteStart, deleteEnd);
            }
            PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
            manager.commitDocument(document);
            createFindViewByIdCode();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    List<String> code;
    private void createFindViewByIdCode(){
        code = new ArrayList<>();
        for (Map.Entry<String,String> entry:nameAndIdMap.entrySet()){
            String codes;
            codes = entry.getKey() + "findViewById("+entry.getValue()+");";
            code.add(codes);
        }
        try {
            new FindViewByIdWriter(project, file, mClass, code, mFactory).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
