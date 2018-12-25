package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import com.u3.filechains.ClickMehtod;

import java.util.List;
import java.util.Map;

public class CustomViewStrategy extends GenCodeStrategy{
    public CustomViewStrategy(List<String> code, Map<ClickMehtod,List<String>> clickMap){
        super(code,clickMap);
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
        PsiStatement statement = findInflateStatement(mClass);
        for (ClickMehtod method:clickMap.keySet()){
            StringBuilder methodString = getMethodInvokeString(method);
            for(String id:clickMap.get(method)){
                String code = getOnClickCode(methodString, id);
                insertOnclickCode(mClass, mFactory, statement, code);
            }
        }
    }
}
