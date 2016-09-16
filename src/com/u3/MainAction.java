package com.u3;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.impl.SystemDock;
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
    Project project;
    PsiFile file;
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
        Document document = editor.getDocument();
        new DeleteAction(project,file,document,mClass).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
