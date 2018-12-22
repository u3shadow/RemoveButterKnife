package com.u3.codegenerator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.List;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class AndroidCodeWriter extends  WriteCommandAction.Simple {
    PsiClass mClass;
    private PsiElementFactory mFactory;
    List<String> code;
    Project mProject;
    public AndroidCodeWriter(Project project, PsiFile file, PsiClass psiClass, List<String> code, PsiElementFactory mFactory) {
        super(project, file);
        mClass = psiClass;
        this.code = code;
        this.mFactory = mFactory;
        mProject = project;
    }

    @Override
    protected void run(){
            GenCodeContext codeContext = new GenCodeContext();
            codeContext.setStrategy(new ActivityStrategy(code));
            codeContext.executeStrategy(mClass,mFactory);
            codeContext.setStrategy(new FragmentStrategy(code));
            codeContext.executeStrategy(mClass,mFactory);
            codeContext.setStrategy(new CustomViewStrategy(code));
            codeContext.executeStrategy(mClass,mFactory);
    }
}
