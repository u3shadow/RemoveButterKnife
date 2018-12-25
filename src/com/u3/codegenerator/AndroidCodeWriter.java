package com.u3.codegenerator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.EverythingGlobalScope;
import com.u3.filechains.ClickMehtod;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaolei on 2016/6/17.
 */
public class AndroidCodeWriter extends  WriteCommandAction.Simple {
    PsiClass mClass;
    private PsiElementFactory mFactory;
    List<String> code;
    Map<ClickMehtod,List<String>> clickMap;
    Project mProject;
    public AndroidCodeWriter(Project project, PsiFile file) {
        super(project, file);
        mProject = project;
    }
    public void setData( List<String> code,Map<ClickMehtod,List<String>> clickMap){
        this.code = code;
        this.clickMap = clickMap;
    }
    public void setEnvData(PsiClass psiClass, PsiElementFactory mFactory){
        mClass = psiClass;
        this.mFactory = mFactory;
    }

    @Override
    protected void run(){
        GenCodeContext codeContext = new GenCodeContext(mClass, mFactory);
        String type = mClass.getSuperClassType().toString();
            if (type.contains("Activity")){
                codeContext.setStrategy(new ActivityStrategy(code,clickMap));
            }else if (type.contains("Fragment")) {
                codeContext.setStrategy(new FragmentStrategy(code,clickMap));
            }else if (type.contains("ViewHolder")||type.contains("Adapter<ViewHolder>")) {
                codeContext.setStrategy(new AdapterStrategy(code,clickMap));
            }else {
                codeContext.setStrategy(new CustomViewStrategy(code,clickMap));
            }
            codeContext.executeStrategy();
    }
}
