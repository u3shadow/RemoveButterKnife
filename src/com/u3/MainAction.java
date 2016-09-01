package com.u3;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilBase;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaolei on 2016/6/14.
 */
public class MainAction extends BaseGenerateAction {
    protected PsiClass mClass;
    String[] s1;
    Map<String,String> nameidmap;
    Project project;
    PsiFile file;
    List<Integer> tod;
    private PsiElementFactory mFactory;

    public MainAction() {
        super(null);
    }

    public MainAction(CodeInsightActionHandler handler) {
        super(handler);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        try {
        project = event.getData(PlatformDataKeys.PROJECT);
        Editor editor = event.getData(PlatformDataKeys.EDITOR);
         file = PsiUtilBase.getPsiFileInEditor(editor, project);
        mFactory = JavaPsiFacade.getElementFactory(project);
        mClass = getTargetClass(editor,file);
        tod = new ArrayList<>();
        Document document = editor.getDocument();
        nameidmap = new HashMap<>();
        String s = document.getText();
        s1 = s.split("\n");
        deleteImport();
        deleteButterKnife();
        deleteAnnotationAndGetIdName();
        new DeleteAction(project,file,document,tod,nameidmap,mClass).execute();
        }catch (Exception e){
            e.printStackTrace();
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
    private void deleteButterKnife(){
        //delete butterknife use
        for (int i = 0; i < s1.length; i++) {
            if (s1[i].trim().indexOf("ButterKnife") == 0) {
                tod.add(i);
            }
        }
    }
    private void deleteAnnotationAndGetIdName()
    {
        String pattern = "@(BindView|InjectView|Bind)\\(R.id.*\\)";
        Pattern r = Pattern.compile(pattern);
        for (int i = 0;i < s1.length;i++){
            Matcher m = r.matcher(s1[i]);
            if (m.find( )) {
                String id = s1[i].substring(s1[i].indexOf("(")+1,s1[i].length()-1);
                String[] s2 = s1[i+1].split(" ");
                String name = s2[s2.length - 1].substring(0,s2[s2.length - 1].length()-1)+" = "+"("+s2[s2.length-2]+")";
                nameidmap.put(name,id);
                tod.add(i);
            }
        }

    }

}
