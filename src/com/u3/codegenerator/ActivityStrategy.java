package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.PsiStatement;

import java.util.List;

public class ActivityStrategy implements GenCodeStrategy{
    private List<String> code;
    public ActivityStrategy(List<String> code){
        this.code = code;
    }
    @Override
    public void genCode(PsiClass mClass, PsiElementFactory mFactory) {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
