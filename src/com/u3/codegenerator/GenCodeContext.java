package com.u3.codegenerator;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;

public class GenCodeContext {
    private GenCodeStrategy strategy;
    public GenCodeContext(){
    }
    public void setStrategy(GenCodeStrategy strategy){
        this.strategy = strategy;
    }
    public void executeStrategy(PsiClass mClass, PsiElementFactory mFactory){
        strategy.genCode(mClass,mFactory);
    }
}
