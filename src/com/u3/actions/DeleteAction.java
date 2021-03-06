package com.u3.actions;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.u3.codegenerator.AndroidCodeWriter;
import com.u3.filechains.BaseChain;
import com.u3.filechains.ClickMehtod;
import com.u3.filechains.DetectAPIUseChain;
import com.u3.filechains.DetectBindChain;
import com.u3.filechains.DetectImportChain;
import com.u3.filechains.DetectOnClickChain;

import java.util.*;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class DeleteAction extends  WriteCommandAction.Simple{
    private Project project;
    private PsiFile file;
    private String[] currentDoc;
    private PsiClass mClass;
    private PsiElementFactory mFactory;
    private Document document;
    private Map<String,String> nameAndIdMap = new LinkedHashMap<>();
    private Map<ClickMehtod,List<String>> clickMap = new LinkedHashMap<>();
    private BaseChain findAPIChain, findBindChain, findImportChain,findClickChain;
    private List code = new ArrayList();
    private List<Integer> deleteLineNumbers = new ArrayList<>();

    public DeleteAction(Project project, PsiFile file,Document document, PsiClass psiClass){
        super(project, file);
        this.document = document;
        currentDoc= document.getText().split("\n");
        this.project = project;
        this.file = file;
        this.mClass = psiClass;
        mFactory = JavaPsiFacade.getElementFactory(project);
    }
    @Override
    protected void run(){
            findImportChain = new DetectImportChain();
            findBindChain = new DetectBindChain();
            findAPIChain = new DetectAPIUseChain();
            findClickChain = new DetectOnClickChain(clickMap);

            findImportChain.setNext(findBindChain);
            findBindChain.setNext(findAPIChain);
            findAPIChain.setNext(findClickChain);

            findImportChain.handle(currentDoc,deleteLineNumbers,nameAndIdMap);

            deleteCode();
            genFindViewCode();
            insertCodeToFile();
    }

    private void deleteCode() {
        for (int i = 0; i < deleteLineNumbers.size(); i++) {
            int deleteStart = document.getLineStartOffset(deleteLineNumbers.get(i));
            int deleteEnd = document.getLineEndOffset(deleteLineNumbers.get(i));
            document.deleteString(deleteStart, deleteEnd);
        }
        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        manager.commitDocument(document);
    }

    private void genFindViewCode() {
          for (Map.Entry<String,String> entry:nameAndIdMap.entrySet()){
            String codes;
            codes = entry.getKey() + "findViewById("+entry.getValue()+");";
            code.add(codes);
        }
    }

    private void insertCodeToFile(){
        try {
           AndroidCodeWriter codeWriter = new AndroidCodeWriter(project, file);
           codeWriter.setEnvData(mClass,mFactory);
           codeWriter.setData(code,clickMap);
           codeWriter.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
