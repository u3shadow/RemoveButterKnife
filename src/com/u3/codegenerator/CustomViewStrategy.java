package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;

import java.util.List;

public class CustomViewStrategy implements GenCodeStrategy{
    private List<String> code;
    public CustomViewStrategy(List<String> code){
        this.code = code;
    }
    @Override
    public void genFindView(PsiClass mClass, PsiElementFactory mFactory) {
         PsiStatement statement = findInflateStatement(mClass);
         insertFindViewCode(mClass, mFactory, statement,code);
    }
    private PsiStatement findInflateStatement(PsiClass mClass){
        PsiStatement result = null;
        PsiMethod[] methods = mClass.getAllMethods();
        for (PsiMethod method:methods) {
            for (PsiStatement statement : method.getBody().getStatements()) {
                String returnValue = statement.getText();
                if (returnValue.contains("R.layout") || returnValue.contains("LayoutInflater.from(context).inflate")) {
                    result = statement;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {

    }
}
