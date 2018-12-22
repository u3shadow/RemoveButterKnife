package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;

public interface GenCodeStrategy {
    default void genCode(PsiClass mClass, PsiElementFactory mFactory){
        try {
            genFindView(mClass, mFactory);
            genOnClick(mClass, mFactory);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void genFindView(PsiClass mClass, PsiElementFactory mFactory);
    void genOnClick(PsiClass mClass, PsiElementFactory mFactory);
}
