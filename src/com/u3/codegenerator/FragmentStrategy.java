package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;
import com.u3.filechains.ClickMehtod;

import java.util.List;
import java.util.Map;

class FragmentStrategy extends GenCodeStrategy{
    public FragmentStrategy(List<String> code,Map<ClickMehtod,List<String>> clickMap){
        super(code,clickMap);
    }
    @Override
    public void genFindView(PsiClass mClass, PsiElementFactory mFactory) {
                PsiStatement statement = findOnCreateStatement(mClass,mFactory);
               insertFindViewCode(mClass,mFactory,statement,code);
    }
    private PsiStatement findOnCreateStatement(PsiClass mClass, PsiElementFactory mFactory){
        PsiStatement result = null;
        PsiMethod onCreateView = mClass.findMethodsByName("onCreateView", false)[0];
        for (PsiStatement statement : onCreateView.getBody().getStatements()) {
            String returnValue = statement.getText();
            if (returnValue.contains("R.layout")||returnValue.contains("LayoutInflater.from")) {
                result = statement;
                break;
            }
        }
        return result;
    }

    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {
        PsiStatement statement = findOnCreateStatement(mClass,mFactory);
        for (ClickMehtod method:clickMap.keySet()){
            StringBuilder methodString = getMethodInvokeString(method);
            for(String id:clickMap.get(method)){
                String code = getOnClickCode(methodString, id);
                insertOnclickCode(mClass, mFactory, statement, code);
            }
        }
    }
}
