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

/**
 * Created by xiaolei on 2016/6/14.
 */
public class MainAction extends BaseGenerateAction {
    protected PsiClass mClass;
    Project project;
    PsiFile file;

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
            mClass = getTargetClass(editor, file);
            Document document = editor.getDocument();
            new ReplaceAndDeleteAction(project, file, document).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
