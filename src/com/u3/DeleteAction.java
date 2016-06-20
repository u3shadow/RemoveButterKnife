package com.u3;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class DeleteAction extends  WriteCommandAction.Simple{
    List<Integer> tod;
    Project project;
    PsiFile file;
    PsiClass mClass;
    private PsiElementFactory mFactory;
    Document document;
    Map<String,String> nameidmap = new HashMap<>();
    public DeleteAction(Project project, PsiFile file,Document document, List<Integer> tod,Map<String,String> map,PsiClass psiClass){
        super(project, file);
        this.document = document;
        this.tod = tod;
        nameidmap.putAll(map);
        this.project = project;
        this.file = file;
        this.mClass = psiClass;
        mFactory = JavaPsiFacade.getElementFactory(project);
    }
    @Override
    protected void run(){
        try {
            for (int i = 0; i < tod.size(); i++) {
                int deleteStart = document.getLineStartOffset(tod.get(i));
                int deleteEnd = document.getLineEndOffset(tod.get(i));
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
        for (Map.Entry<String,String> entry:nameidmap.entrySet()){
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
