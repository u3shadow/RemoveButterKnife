package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;

public interface GenCodeStrategy {
    void genCode(PsiClass mClass, PsiElementFactory mFactory);
}
