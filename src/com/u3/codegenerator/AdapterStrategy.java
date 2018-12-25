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
        PsiMethod method = findMethod(mClass);
        String viewName = findViewName(method);
        for (PsiStatement statement : method.getBody().getStatements()) {
            String returnValue = statement.getText();
            if (returnValue.contains("super(" + viewName + ")")) {
                List<String> codes = addViewName(viewName);
                insertFindViewCode(mClass,mFactory,statement,codes);
                break;
            }
        }
    }
    private PsiMethod findMethod(PsiClass mClass){
       PsiMethod[] methods = mClass.getAllMethods();
       PsiMethod result = null;
        for (PsiMethod method:methods) {
                for (PsiParameter parameter : method.getParameterList().getParameters()) {
                    String type = parameter.getType().toString();
                    if (type != null && type.equals("PsiType:View")) {
                      result =  method;
                      break;
                    }
                }
        }
        return result;
    }
    private String findViewName(PsiMethod method){
        String result = "";
        for (PsiParameter parameter:method.getParameterList().getParameters()){
            if (parameter.getType().toString().equals("PsiType:View")){
                result = parameter.getName();
            }
        }
        return  result;
    }
    private List<String> addViewName(String viewName) {
        List<String> codes = new ArrayList<>();
        for (String s : code) {
            StringBuilder stringBuilder = new StringBuilder(s);
            stringBuilder.insert(s.indexOf("find"),viewName+".");
            codes.add(stringBuilder.toString());
        }
        return codes;
    }

    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {

    }
}
