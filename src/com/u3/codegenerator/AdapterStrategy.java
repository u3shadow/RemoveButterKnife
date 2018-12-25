package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiStatement;
import com.u3.filechains.ClickMehtod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class AdapterStrategy extends GenCodeStrategy{
            PsiMethod method;
         String viewName;
    public AdapterStrategy(List<String> code, Map<ClickMehtod,List<String>> clickMap){
        super(code,clickMap);
    }
    @Override
    public void genFindView(PsiClass mClass, PsiElementFactory mFactory) {
        method = findMethod(mClass);
        viewName = findViewName(method);
        List<String> codes = addViewName(viewName);
        PsiStatement statement = findSuperStatement(method,viewName);
        insertFindViewCode(mClass,mFactory,statement,codes);
    }
    private PsiStatement findSuperStatement(PsiMethod method,String viewName){
        PsiStatement result = null;
        for (PsiStatement statement : method.getBody().getStatements()) {
            String returnValue = statement.getText();
            if (returnValue.contains("super(" + viewName + ")")) {
                result = statement;
                break;
            }
        }
        return result;
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
        method = findMethod(mClass);
        viewName = findViewName(method);
        PsiStatement statement = findSuperStatement(method,viewName);
        for (ClickMehtod method:clickMap.keySet()){
            StringBuilder methodString = getMethodInvokeString(method);
            for(String id:clickMap.get(method)){
                String code = getOnClickCode(methodString, id);
                insertOnclickCode(mClass, mFactory, statement, code);
            }
        }
    }
}
