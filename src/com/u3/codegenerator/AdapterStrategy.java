package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiStatement;

import java.util.ArrayList;
import java.util.List;

class AdapterStrategy implements GenCodeStrategy{
    private List<String> code;
    public AdapterStrategy(List<String> code){
        this.code = code;
    }
    @Override
    public void genFindView(PsiClass mClass, PsiElementFactory mFactory) {
        PsiMethod[] methods = mClass.getAllMethods();
        for (PsiMethod method:methods) {
            if (method.getParameterList().getParameters().length == 1) {
                for (PsiParameter parameter : method.getParameterList().getParameters()) {
                    String type = parameter.getType().toString();
                    if (type != null && type.equals("PsiType:View")) {
                        String viewName = parameter.getName();
                        for (PsiStatement statement : method.getBody().getStatements()) {
                            String returnValue = statement.getText();
                            if (returnValue.contains("super(" + viewName + ")")) {
                                List<String> codes = new ArrayList<>();
                                for (String s : code) {
                                    StringBuilder stringBuilder = new StringBuilder(s);
                                    stringBuilder.insert(s.indexOf("find"),viewName+".");
                                    codes.add(stringBuilder.toString());
                                }
                                insertCode(mClass,mFactory,statement,codes);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {

    }
}
