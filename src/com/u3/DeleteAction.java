package com.u3;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class DeleteAction extends  WriteCommandAction.Simple{
    List<Integer> tod;
    Project project;
    PsiFile file;
    String[] s1;
    PsiClass mClass;
    private PsiElementFactory mFactory;
    Document document;
    Map<String,String> nameidmap = new LinkedHashMap<>();
    Map<Integer,String> tcmap = new LinkedHashMap<>();
    public DeleteAction(Project project, PsiFile file,Document document, PsiClass psiClass){
        super(project, file);
        this.document = document;
        s1= document.getText().split("\n");
        this.project = project;
        this.file = file;
        this.mClass = psiClass;
        mFactory = JavaPsiFacade.getElementFactory(project);
        tod = new ArrayList<>();
    }
    private void deleteButterKnife(){
        //delete butterknife use
        for (int i = 0; i < s1.length; i++) {
            if (s1[i].trim().indexOf("ButterKnife") == 0) {
                tod.add(i);
            }
        }
    }
    private void deleteImport() {
        //delete import
        String is1 = "import butterknife.Bind;";
        String is3 = "import butterknife.InjectView;";
        String is2 = "import butterknife.ButterKnife;";
        String is4 = "import butterknife.BindView;";
        for (int i = 0;i < s1.length;i++){
            if (s1[i].equals(is1)||s1[i].equals(is2)||s1[i].equals(is3)||s1[i].equals(is4)){
                tod.add(i);
            }
        }
    }
    private void deleteAnnotationAndGetIdName()
    {
        String pattern = "^@(BindView|InjectView|Bind)\\(R.id.*\\)$";
        Pattern r = Pattern.compile(pattern);
        for (int i = 0;i < s1.length;i++){
            Matcher m = r.matcher(s1[i].trim());
            s1[i] = s1[i].trim();
            if (m.find()) {
                String id = s1[i].substring(s1[i].indexOf("(")+1,s1[i].length()-1);
                String[] s2 = s1[i+1].trim().split(" ");
                String name = s2[1].substring(0,s2[1].length() - 1)+" = "+"("+s2[0]+")";
                nameidmap.put(name,id);
                tod.add(i);
            }
        }
    }
    private void replaceAnnotationAndGetIdName()
    {
        String pattern = "@(BindView|InjectView|Bind)\\(R.id.*\\)*;";
        Pattern r = Pattern.compile(pattern);
        int j;
        for (j = 0; j < s1.length; j++){
            Matcher m = r.matcher(s1[j]);
            if (m.find( )) {
                String id = s1[j].substring(s1[j].indexOf("(")+1,s1[j].indexOf(")"));
                String s2 = s1[j].substring(s1[j].indexOf(")")+1).trim();
                String name = s2.split(" ")[1];
                name = name.substring(0,name.length()-1);
                String type = s2.split(" ")[0];
                tcmap.put(j,type + " "+name);
                name = name+" = "+"("+type+")";
                nameidmap.put(name,id);
                System.out.print(type + "--"+name);
            }
        }
    }
    @Override
    protected void run(){
        try {
            deleteImport();
            replaceAnnotationAndGetIdName();
            deleteAnnotationAndGetIdName();
            deleteButterKnife();
            for (Map.Entry<Integer,String> entry:tcmap.entrySet()){
                int deleteStart = document.getLineStartOffset(entry.getKey());
                int deleteEnd = document.getLineEndOffset(entry.getKey());
                document.replaceString(deleteStart, deleteEnd,"\t"+entry.getValue()+";");
            }
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
