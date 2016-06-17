package com.u3;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.EverythingGlobalScope;

import java.util.List;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class FindViewByIdWriter extends  WriteCommandAction.Simple {
    PsiClass mClass;
    private PsiElementFactory mFactory;
    List<String> code;
    Project mProject;
    protected FindViewByIdWriter(Project project, PsiFile file, PsiClass psiClass, List<String> code, PsiElementFactory mFactory) {
        super(project, file);
        mClass = psiClass;
        this.code = code;
        this.mFactory = mFactory;
        mProject = project;
    }

    @Override
    protected void run() throws Throwable {
        generateInjects(mProject);
    }
    protected void generateInjects(Project mProject) {
        PsiClass activityClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Activity", new EverythingGlobalScope(mProject));
        PsiClass fragmentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Fragment", new EverythingGlobalScope(mProject));
        PsiClass supportFragmentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.support.v4.app.Fragment", new EverythingGlobalScope(mProject));

        // Check for Activity class
        try {
            PsiMethod onCreate = mClass.findMethodsByName("onCreate", false)[0];
            for (PsiStatement statement : onCreate.getBody().getStatements()) {
                // Search for setContentView()
                if (statement.getFirstChild() instanceof PsiMethodCallExpression) {
                    PsiReferenceExpression methodExpression
                            = ((PsiMethodCallExpression) statement.getFirstChild())
                            .getMethodExpression();
                    // Insert ButterKnife.inject()/ButterKnife.bind() after setContentView()
                    if (methodExpression.getText().equals("setContentView")) {
                        for (int i = code.size() - 1; i >= 0; i--) {
                            onCreate.getBody().addAfter(mFactory.createStatementFromText(
                                    code.get(i) + "\n", mClass), statement);
                        }
                        break;
                    }
                }
            }
            // Check for Fragment class
        } catch (Exception e){
            e.printStackTrace();
            PsiMethod onCreateView = mClass.findMethodsByName("onCreateView", false)[0];
            for (PsiStatement statement : onCreateView.getBody().getStatements()) {
                if (statement instanceof PsiReturnStatement) {
                    String returnValue = ((PsiReturnStatement) statement).getReturnValue().getText();
                    if (returnValue.contains("R.layout")) {
                        onCreateView.getBody().addBefore(mFactory.createStatementFromText("android.view.View view = " + returnValue + ";", mClass), statement);
                        for (int i = 0; i < code.size(); i++) {
                            onCreateView.getBody().addBefore(mFactory.createStatementFromText(
                                    code.get(i), mClass), statement);
                        }
                        statement.replace(mFactory.createStatementFromText("return view;", mClass));
                    } else {
                        // Insert ButterKnife.inject()/ButterKnife.bind() before returning a view for a fragment
                        for (int i = 0; i < code.size(); i++) {
                            onCreateView.getBody().addBefore(mFactory.createStatementFromText(
                                    code.get(i), mClass), statement);
                        }
                    }
                    break;
                }
            }
        }
    }
}
