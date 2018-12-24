package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiStatement;

import java.util.List;

public interface GenCodeStrategy {
    default void genCode(PsiClass mClass, PsiElementFactory mFactory){
        try {
            genFindView(mClass, mFactory);
            genOnClick(mClass, mFactory);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    default void insertCode(PsiClass mClass, PsiElementFactory mFactory, PsiStatement statement, List<String> code) {
        for (int i = code.size() - 1; i >= 0; i--) {
            StringBuffer buffer = new StringBuffer(code.get(i));
            try {
                statement.addAfter(mFactory.createStatementFromText(
                        buffer.toString(), mClass), statement);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
    void genFindView(PsiClass mClass, PsiElementFactory mFactory);
    void genOnClick(PsiClass mClass, PsiElementFactory mFactory);
}
