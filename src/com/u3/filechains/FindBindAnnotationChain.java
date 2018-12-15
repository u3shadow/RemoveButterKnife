package com.u3.filechains;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindBindAnnotationChain extends BaseChain {
    @Override
    public void process() {
        String pattern = "^@(BindView|InjectView|Bind)\\((R.id.*)|(R2.id.*)\\)$";
        Pattern r = Pattern.compile(pattern);
        for (int i = 0;i < currentDoc.length;i++){
            Matcher m = r.matcher(currentDoc[i].trim());
            currentDoc[i] = currentDoc[i].trim();
            if (m.find()) {
                String id = currentDoc[i].substring(currentDoc[i].indexOf("(")+1,currentDoc[i].length()-1);
                String[] s2 = currentDoc[i+1].trim().split(" ");
                String name = s2[1].substring(0,s2[1].length() - 1)+" = "+"("+s2[0]+")";
                nameAndIdMap.put(name,id);
                deleteLineNumbers.add(i);
            }
        }
    }
}
