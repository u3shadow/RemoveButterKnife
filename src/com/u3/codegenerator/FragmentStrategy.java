package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiStatement;

import java.util.List;

class FragmentStrategy implements GenCodeStrategy{
    private List<String> code;
    public FragmentStrategy(List<String> code){
        this.code = code;
    }
    @Override
    public void genFindView(PsiClass mClass, PsiElementFactory mFactory) {
        PsiMethod onCreateView = mClass.findMethodsByName("onCreateView", false)[0];
        for (PsiStatement statement : onCreateView.getBody().getStatements()) {
            String returnValue = statement.getText();
            if (returnValue.contains("R.layout")||returnValue.contains("LayoutInflater.from")) {
                insertCode(mClass,mFactory,statement,code);
                break;
            }
        }
    }

    @Override
    public void genOnClick(PsiClass mClass, PsiElementFactory mFactory) {

    }
}
