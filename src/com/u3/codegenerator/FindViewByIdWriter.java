package com.u3.codegenerator;

import com.intellij.ide.ui.AppearanceOptionsTopHitProvider;
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
    public FindViewByIdWriter(Project project, PsiFile file, PsiClass psiClass, List<String> code, PsiElementFactory mFactory) {
        super(project, file);
        mClass = psiClass;
        this.code = code;
        this.mFactory = mFactory;
        mProject = project;
    }

    @Override
    protected void run(){
        try {
            generateInjects(mProject);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void generateInjects(Project mProject) {
        try {
        PsiClass activityClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Activity", new EverythingGlobalScope(mProject));
        PsiClass fragmentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.app.Fragment", new EverythingGlobalScope(mProject));
        PsiClass supportFragmentClass = JavaPsiFacade.getInstance(mProject).findClass(
                "android.support.v4.app.Fragment", new EverythingGlobalScope(mProject));

        // Check for Activity class

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

            PsiMethod onCreateView = mClass.findMethodsByName("onCreateView", false)[0];
            for (PsiStatement statement : onCreateView.getBody().getStatements()) {
                    String returnValue = statement.getText();
                    if (returnValue.contains("R.layout")) {
                        String viewName = returnValue.trim().split(" ")[1]+".";
                        for (int i = 0; i < code.size(); i++) {
                            StringBuffer buffer = new StringBuffer(code.get(i));
                            int num = buffer.indexOf(")");
                            buffer.insert(num+1,viewName);
                            try {
                                statement.addAfter(mFactory.createStatementFromText(
                                        buffer.toString(), mClass), statement);
                            }catch (Exception e1){}
                        }
                        break;
                    }
                }
            e.printStackTrace();
        }
    }
}
