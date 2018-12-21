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
        PsiMethod onCreateView = mClass.findMethodsByName("onCreateView", false)[0];
        for (PsiStatement statement : onCreateView.getBody().getStatements()) {
            String returnValue = statement.getText();
            if (returnValue.contains("R.layout")||returnValue.contains("LayoutInflater.from")) {
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
    }

    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {

    }
}
